package com.zjy.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zjy
 * @date 2022/5/6
 */
public class UnStableNioServer {

    static ServerSocketChannel serverSocketChannel;
    static Selector selector;

    public static void main(String[] args) throws Exception {

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new java.net.InetSocketAddress(8082));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(selector.isOpen());

        while (true) {
            if (selector.select(3000) == 0) {
                System.out.println("服务器等待了3秒，无新事件");
                continue;
            }
            selector.selectedKeys().forEach(key -> {
                try {
                    handler(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    //处理客户端连接事件
    public static void handler(SelectionKey key) throws Exception {
        printSelectKey(key);
        if (key.isAcceptable()) {
            System.out.println("建立新的SocketChannel");
            //该该客户端生成一个 SocketChannel，并设置为非阻塞
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                System.out.println("" +
                        " 生成了一个 socketChannel " + socketChannel.hashCode());
                //让socketChannel关注selector中的read事件
                socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
            }
        }

        if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            channel.read(byteBuffer);
            System.out.println("收到客户端的消息：" + new String(byteBuffer.array()));
        }
        selector.selectedKeys().remove(key);
    }

    /**
     * 打印SelectKey的状态
     */
    public static void printSelectKey(SelectionKey key) {
        System.out.println("有新的事件");
        System.out.println("key.isAcceptable()=" + key.isAcceptable());
        System.out.println("key.isConnectable()=" + key.isConnectable());
        System.out.println("key.isReadable()=" + key.isReadable());
        System.out.println("key.isWritable()=" + key.isWritable());
        System.out.println("key.isValid()=" + key.isValid());
    }


}
