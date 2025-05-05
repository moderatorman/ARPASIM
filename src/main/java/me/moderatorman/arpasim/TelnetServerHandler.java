package me.moderatorman.arpasim;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.moderatorman.arpasim.impl.managers.ProgramManager;
import me.moderatorman.arpasim.impl.managers.UserManager;
import me.moderatorman.arpasim.impl.programs.*;
import me.moderatorman.arpasim.impl.users.Session;
import me.moderatorman.arpasim.util.ProgramIO;

import java.net.InetSocketAddress;
import java.util.Objects;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String>
{
    public TelnetServerHandler()
    {
        // Register programs here
        ProgramManager.registerProgram(new LoginBannerProgram());
        ProgramManager.registerProgram(new ExitProgram());
        ProgramManager.registerProgram(new HelpProgram());
        ProgramManager.registerProgram(new LoginProgram());
        ProgramManager.registerProgram(new NewUserProgram());
        ProgramManager.registerProgram(new MoreProgram());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        try
        {
            ProgramIO programIO = new ProgramIO(ctx);
            String ip = programIO.getIP();
            Session session = UserManager.getSession(ip);
            if (session != null) // user has existing session
            {
                ctx.write("Welcome back, " + session.getUser().getName() + "!\n");
                System.out.println("Session reactivated for " + session.getUser().getName() + " (IP: " + ip + ", ID: " + session.getID() + ")");
            }
            else {
                UserManager.createSession(ip); // create a new unauthenticated session
                session = UserManager.getSession(ip);
                System.out.println("New session created from " + ip + " (ID: " + (session != null ? session.getID() : "N/A") + ")");
            }

            Objects.requireNonNull(ProgramManager.getProgram("loginbanner")).execute(programIO, new String[]{});

            ctx.writeAndFlush("> ");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
    {
        String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        Session session = UserManager.getSession(ip);

        if (session != null)
        {
            if (session.isRunningProgram())
            {
                ctx.write("Channel read failed. (program already running??)\n");
                return;
            }
        } else {
            // this realistically shouldn't ever happen
            ctx.write("Channel read failed! (no session??)\n");
            ctx.disconnect();
            return;
        }

        String label = msg.split(" ")[0];
        AbstractProgram program = ProgramManager.getProgram(label);

        if (program != null)
        {
            String[] args = msg.split(" ");
            String[] args2 = new String[args.length - 1];
            System.arraycopy(args, 1, args2, 0, args.length - 1);
            ProgramIO programIO = new ProgramIO(ctx);
            Thread activeProgramThread = new Thread(() -> program.execute(programIO, args2));
            activeProgramThread.start();
            session.setRunningProgram(activeProgramThread, programIO);
            waitForThread(ctx, activeProgramThread, session);
        } else {
            if (!session.isRunningProgram())
                ctx.write("Unknown command: " + label + "\n");
            else if (session.getProgramIO() != null) {
                System.out.println("channelRead0: appending input to active program IO: " + msg);
                session.getProgramIO().addInput(msg);
            }
        }
    }

    private void waitForThread(ChannelHandlerContext ctx, Thread thread, Session session)
    {
        new Thread(() ->
        {
            try
            {
                thread.join(); // Wait for the program thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            session.setRunningProgram(null, null);
            ctx.writeAndFlush("> ");
        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        Session session = UserManager.getSession(ip);
        if (session != null)
            session.setActive(false);
        ctx.close();
    }
}