package com.zjy.netty.stickybag.solution;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * 解决了粘包/拆包的 客户端
 */
public class SolutionStickyClient {
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
                            pipeline.addLast(new MyMessageEncoder()); //加入编码器
                            pipeline.addLast(new MyMessageDecoder()); //加入解码器
                            pipeline.addLast(new MyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 8083).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

        private int count;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //使用客户端发送10条数据 "今天天气冷，吃火锅" 编号

            for (int i = 0; i < 5; i++) {
                String mes = "今天天气冷，吃火锅";
                byte[] content = mes.getBytes(StandardCharsets.UTF_8);
                int length = mes.getBytes(StandardCharsets.UTF_8).length;

                //创建协议包对象
                MessageProtocol messageProtocol = new MessageProtocol();
                messageProtocol.setLen(length);
                messageProtocol.setContent(content);
                ctx.writeAndFlush(messageProtocol);
            }
        }

        //    @Override
        protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

            int len = msg.getLen();
            byte[] content = msg.getContent();

            System.out.println("客户端接收到消息如下");
            System.out.println("长度=" + len);
            System.out.println("内容=" + new String(content, StandardCharsets.UTF_8));
            System.out.println("客户端接收消息数量=" + (++this.count));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("异常消息=" + cause.getMessage());
            ctx.close();
        }
    }

}