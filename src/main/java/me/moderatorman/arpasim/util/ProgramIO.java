package me.moderatorman.arpasim.util;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class ProgramIO implements IProgramIO
{
    private ChannelHandlerContext ctx;

    public ProgramIO(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void print(String msg)
    {
        ctx.write(msg);
    }

    @Override
    public void println(String msg)
    {
        ctx.write(msg + "\r\n");
    }

    @Override
    public void flush()
    {
        ctx.flush();
    }

    @Override
    public String getIP()
    {
        return ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public void disconnect()
    {
        ctx.disconnect();
    }
}
