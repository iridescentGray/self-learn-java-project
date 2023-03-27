package com.zjy.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * Netty群聊实现
 *
 * @author zjy
 * @date 2022/6/14
 */
public class NettyGroupChatServer {

    private final int port; //监听端口

    public NettyGroupChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new NettyGroupChatServer(8083).run();
    }

    //编写run方法，处理客户端的请求
    public void run() throws Exception {

        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
        DefaultChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder", new StringEncoder());

                            GroupChatServerHandler groupChatServerHandler = new GroupChatServerHandler();
                            groupChatServerHandler.setChannelGroup(channels);
                            //加入自己的业务处理handler
                            pipeline.addLast(groupChatServerHandler);

                        }
                    });

            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = b.bind(port).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //定义一个channle 组，管理所有的channel
        //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
        private ChannelGroup channelGroup;

        public void setChannelGroup(ChannelGroup channelGroup) {
            this.channelGroup = channelGroup;
        }

        //handlerAdded 表示连接建立，一旦连接，第一个被执行
        //将当前channel 加入到  channelGroup
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            //将该客户加入聊天的信息推送给其它在线的客户端

            //该方法会将 channelGroup 中所有的channel 遍历，并发送消息，我们不需要自己遍历
            channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天" + sdf.format(new java.util.Date()) + " \n");
            channelGroup.add(channel);
        }

        //断开连接, 将xx客户离开信息推送给当前在线的客户
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
            System.out.println("channelGroup size" + channelGroup.size());

        }

        //表示channel 处于活动状态, 提示 xx上线
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //这个是给服务端看的，客户端上面已经提示xxx加入群聊了
            System.out.println(ctx.channel().remoteAddress() + " 上线了~");
        }

        //表示channel 处于不活动状态, 提示 xx离线了
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + " 离线了~");
        }

        //读取数据，转发给在线的每一个客户端
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

            //获取到当前channel
            Channel channel = ctx.channel();
            //这时我们遍历channelGroup, 根据不同的情况，回送不同的消息
            channelGroup.forEach(ch -> {
                if (channel != ch) { //不是当前的channel,转发消息
                    ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
                } else {//回显自己发送的消息给自己
                    ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
                }
            });
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //关闭通道
            ctx.close();
        }
    }


}
