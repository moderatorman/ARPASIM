package me.moderatorman.arpasim;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TelnetServer
{
    private int port;

    public TelnetServer(int port)
    {
        this.port = port;
    }

    public void init()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TelnetServerInitializer());

            Channel ch = b.bind(port).sync().channel();
            System.out.println("ARPASIM telnet server running on port " + port);
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
