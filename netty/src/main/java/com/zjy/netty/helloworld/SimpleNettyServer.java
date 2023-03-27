package com.zjy.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author zjy
 * @date 2022/5/18
 */
public class SimpleNettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println(".....服务器 is ready...");
            //绑定端口号，提供服务
            ChannelFuture channelFuture = serverBootstrap.bind(8083).sync();
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    System.out.println("监听端口成功");
                } else {
                    System.out.println("监听端口失败");
                }
            });


            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static class NettyServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead——server receive msg:" + msg);
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("channelRead——客户端消息：" + byteBuf.toString(io.netty.util.CharsetUtil.UTF_8));
            System.out.println("channelRead——客户端地址：" + ctx.channel().remoteAddress());

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            //writeAndFlush 是 write + flush
            //将数据写入到缓存，并刷新
            //一般讲，我们对这个发送的数据进行编码
            ctx.writeAndFlush(Unpooled.copiedBuffer("channelReadComplete——hello client \n", io.netty.util.CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }


}
