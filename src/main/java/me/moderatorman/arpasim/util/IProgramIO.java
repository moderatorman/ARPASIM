package me.moderatorman.arpasim.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IProgramIO
{
    void print(String msg);
    void println(String msg);
    void flush();

    void writeAndFlush(ByteBuf buffer);

    String readInput();

    String getIP();
    void disconnect();
}
