package me.moderatorman.arpasim;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.moderatorman.arpasim.impl.managers.UserManager;
import me.moderatorman.arpasim.impl.users.Session;

import java.net.InetSocketAddress;

public class RawByteHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception
    {
        if (!msg.isReadable())
        {
            System.out.println("No readable bytes in the message, ignoring.");
            return;
        }

        String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        Session session = UserManager.getSession(ip);

        if (session != null)
        {
            if (!session.isRunningProgram() || session.getProgramIO() == null)
            {
                System.out.println("No running program or program IO is null for session: " + session.getID());
                ctx.fireChannelRead(msg.retain());
                return;
            }

            StringBuilder input = new StringBuilder();

            while (msg.isReadable())
            {
                char c = (char) msg.readByte();

                // Handle escape sequences (e.g., arrow keys)
                if (c == '\u001b') // Start of an escape sequence
                {
                    if (msg.isReadable())
                    {
                        c = (char) msg.readByte();
                        if (c == '[' && msg.isReadable())
                        {
                            c = (char) msg.readByte();
                            input.append(c);
                        }
                    }
                } else input.append(c);
            }

            System.out.println("Submitting raw input to program IO: " + input);
            session.getProgramIO().addInput(input.toString());
        } else {
            System.out.println("Session not found for IP: " + ip);
            ctx.fireChannelRead(msg.retain());
        }
    }
}