package me.moderatorman.arpasim;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.moderatorman.arpasim.impl.ProgramManager;
import me.moderatorman.arpasim.impl.programs.AbstractProgram;
import me.moderatorman.arpasim.impl.programs.LoginBannerProgram;
import me.moderatorman.arpasim.util.ProgramIO;

import java.util.Objects;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String>
{
    public TelnetServerHandler()
    {
        // Register programs here
        ProgramManager.registerProgram(new LoginBannerProgram());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        try
        {
            Objects.requireNonNull(ProgramManager.getProgram("loginbanner")).execute(new ProgramIO(ctx), new String[]{});
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

    private void launchUsenetInterface()
    {
        try
        {
            //
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}