package com.zjy.netty.stickybag.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 粘包/拆包 服务器
 */
public class StickyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast(new MyServerHandler());

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8083).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

        private int count;

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

            byte[] buffer = new byte[msg.readableBytes()];
            msg.readBytes(buffer);

            //将buffer转成字符串
            String message = new String(buffer, StandardCharsets.UTF_8);
            System.out.println("服务器接收到数据 " + message);
            System.out.println("服务器接收到消息量=" + (++this.count));
            if (count > 1) {
                System.out.println("出现沾包现象");
            }

            //服务器回送数据给客户端, 回送一个随机id ,
            ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID() + " \n", StandardCharsets.UTF_8);
            ctx.writeAndFlush(responseByteBuf);
        }
    }

}