package me.moderatorman.arpasim;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializer extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        p.addLast(new RawByteHandler());
        p.addLast(new StringDecoder());
        p.addLast(new StringEncoder());
        p.addLast(new TelnetServerHandler());
    }
}
