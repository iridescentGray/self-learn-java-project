package com.zjy.netty.stickybag.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * 粘包/拆包 客户端
 */
public class StickyClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast(new MyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 8083).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        private int count;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //使用客户端发送10条数据 hello,server 编号
            for (int i = 0; i < 10; ++i) {
                ByteBuf buffer = Unpooled.copiedBuffer("" + i, StandardCharsets.UTF_8);
                ctx.writeAndFlush(buffer);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            byte[] buffer = new byte[msg.readableBytes()];
            msg.readBytes(buffer);

            String message = new String(buffer, StandardCharsets.UTF_8);
            System.out.println("客户端接收到消息=" + message);
            System.out.println("客户端接收消息数量=" + (++this.count));
            if (count > 1) {
                System.out.println("出现沾包现象");
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }

}