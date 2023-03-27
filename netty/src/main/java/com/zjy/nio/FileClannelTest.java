package com.zjy.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author zjy
 * @date 2022/5/6
 */
public class FileClannelTest {

    // /**
    //  * 向文件中写入
    //  * @param args args
    //  * @throws Exception exception
    //  */
    // public static void main(String[] args) throws Exception {
    //     File file = new File("/Users/zjy/Desktop/test.txt");
    //     FileOutputStream fileOutputStream = new FileOutputStream(file);
    //     FileChannel channel = fileOutputStream.getChannel();
    //
    //     ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //     String str = "hello world nio";
    //     byteBuffer.put(str.getBytes());
    //     byteBuffer.flip();
    //
    //     channel.write(byteBuffer);
    //     channel.close();
    // }

    /**
     * 读取文件中的内容
     * @param args args
     * @throws Exception exception
     */
    // public static void main(String[] args) throws Exception {
    //     File file = new File("/Users/zjy/Desktop/test.txt");
    //     FileInputStream fileInputStream = new FileInputStream(file);
    //     FileChannel channel = fileInputStream.getChannel();
    //
    //     ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
    //     channel.read(byteBuffer);
    //     System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8) );
    //
    //     channel.close();
    // }


    /**
     * 拷贝文件-使用统一个buffer
     * @param args args
     * @throws Exception exception
     */
    // public static void main(String[] args) throws Exception {
    //
    //     File file = new File("/Users/zjy/Desktop/test.txt");
    //     File file2 = new File("/Users/zjy/Desktop/test2.txt");
    //     FileInputStream fileInputStream = new FileInputStream(file);
    //     FileOutputStream file2OutputStream = new FileOutputStream(file2);
    //     FileChannel channel = fileInputStream.getChannel();
    //     FileChannel channel2 = file2OutputStream.getChannel();
    //
    //     ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //
    //     while (true) {
    //         //对clear方法的理解：当buffer中的数据已经被消费过一次，可以使用此方法清空buffer中已被消费过的数据
    //         byteBuffer.clear();
    //
    //         int read = channel.read(byteBuffer);
    //         if (read==-1) {
    //             break;
    //         }
    //         //对flip方法的理解：设置limit = position  position = 0;  代表把当前buffer，置为可读状态
    //         //
    //         byteBuffer.flip();
    //         channel2.write(byteBuffer);
    //     }
    //
    //     channel.close();
    //     channel2.close();
    // }


    /**
     * 拷贝文件-使用transferFrom API
     *
     * @param args args
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {

        File file = new File("/Users/zjy/Desktop/test.txt");
        File file2 = new File("/Users/zjy/Desktop/test2.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream file2OutputStream = new FileOutputStream(file2);
        FileChannel channel = fileInputStream.getChannel();
        FileChannel channel2 = file2OutputStream.getChannel();

        channel2.transferFrom(channel, 0, channel.size());

        channel.close();
        channel2.close();
    }

}
