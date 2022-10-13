package com.gmail.at.kotamadeo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Server {
    public static void main(String[] args) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 12345));
            while (true) {
                try (SocketChannel socketChannel = serverChannel.accept()) {
                    ByteBuffer inputBuffer = allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        int byteCount = socketChannel.read(inputBuffer);
                        if (byteCount == -1) {
                            break;
                        }
                        String msg = new String(inputBuffer.array(), 0, byteCount, UTF_8);
                        inputBuffer.clear();
                        socketChannel.write(wrap(("Без пробелов: " + msg.replaceAll("\\s+", "")).getBytes(UTF_8)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
