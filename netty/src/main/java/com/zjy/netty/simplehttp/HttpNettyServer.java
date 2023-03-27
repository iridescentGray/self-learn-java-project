package com.zjy.netty.simplehttp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author zjy
 * @date 2022/5/18
 */
public class HttpNettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // .option(ChannelOption.SO_BACKLOG, 128)
                    // .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("MyHttpServerCodec", new HttpServerCodec())
                                    .addLast("MyTestHttpServerHandler", new NettyHttpServerHandler());
                        }
                    });
            System.out.println(".....HTTP服务器 is ready...");
            //绑定端口号，提供服务
            ChannelFuture channelFuture = serverBootstrap.bind(8083).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static class NettyHttpServerHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            //判断 msg 是不是 httprequest请求
            if (msg instanceof HttpRequest) {
                System.out.println("ctx 类型=" + ctx.getClass());
                System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + " TestHttpServerHandler hash=" + this.hashCode());
                System.out.println("msg 类型=" + msg.getClass());
                System.out.println("客户端地址" + ctx.channel().remoteAddress());

                ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
                //构造一个http的相应，即 httpresponse
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
                //将构建好 response返回
                ctx.writeAndFlush(response);
            }
        }
    }

}
