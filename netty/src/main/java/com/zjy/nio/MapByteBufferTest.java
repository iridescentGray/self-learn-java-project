package com.zjy.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zjy
 * @date 2022/5/6
 */
public class MapByteBufferTest {

    /**
     * 使用MappedByteBuffer 直接堆外修改文件
     *
     * @param args args
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {

        RandomAccessFile rw = new RandomAccessFile("/Users/zjy/Desktop/test.txt", "rw");
        FileChannel channel1 = rw.getChannel();

        MappedByteBuffer map = channel1.map(FileChannel.MapMode.READ_WRITE, 0, 10);
        map.put(0, (byte) 'p');

        rw.close();
    }

}
