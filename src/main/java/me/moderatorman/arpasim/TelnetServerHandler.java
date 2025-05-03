package me.moderatorman.arpasim;

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
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        try
        {
            ProgramIO programIO = new ProgramIO(ctx);
            Objects.requireNonNull(ProgramManager.getProgram("loginbanner")).execute(programIO, new String[]{});

            String ip = programIO.getIP();
            Session session = UserManager.getSession(ip);
            if (session != null) // user has existing session
                ctx.write("Welcome back, " + session.getUser().getName() + "!\n");
            else UserManager.createSession(ip); // create a new unauthenticated session

            ctx.writeAndFlush("> ");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
    {
        String label = msg.split(" ")[0];
        AbstractProgram program = ProgramManager.getProgram(label);
        if (program != null)
        {
            String[] args = msg.split(" ");
            String[] args2 = new String[args.length - 1];
            System.arraycopy(args, 1, args2, 0, args.length - 1);
            program.execute(new ProgramIO(ctx), args2);
        } else {
            ctx.write("Unknown command: " + label + "\n");
        }
        ctx.writeAndFlush("> ");
    }
}