package ru.job4j.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    public static void main(String[] args) {
        FileChannel fileChannel;
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            fileChannel = (FileChannel) Files.newByteChannel(Paths.get("data/nio.txt"));
            while ((fileChannel.read(buffer)) != -1) {
                buffer.flip();
                CharBuffer decode = decoder.decode(buffer);
                while (decode.hasRemaining()) {
                    System.out.print(decode.get());
                }
                buffer.compact();
            }
            buffer.flip();
            CharBuffer decode = decoder.decode(buffer);
            while (decode.hasRemaining()) {
                System.out.print(decode.get());
            }
            CharBuffer flushBuffer = CharBuffer.allocate(1024);
            decoder.flush(flushBuffer);
            flushBuffer.flip();
            while (flushBuffer.hasRemaining()) {
                System.out.print(flushBuffer.get());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
