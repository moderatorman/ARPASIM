package me.moderatorman.arpasim.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProgramIO implements IProgramIO
{
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
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
    public void writeAndFlush(ByteBuf buffer)
    {
        ctx.writeAndFlush(buffer);
    }

    @Override
    public String readInput()
    {
        try
        {
            return inputQueue.take();
        } catch (InterruptedException e) {
            return "";
        }
    }

    public void addInput(String input)
    {
        System.out.println("Input added to queue: " + input);
        inputQueue.offer(input);
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
