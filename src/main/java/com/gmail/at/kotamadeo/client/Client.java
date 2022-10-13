package com.gmail.at.kotamadeo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Client {
    private static final int SLEEP_TIME = 2;

    public static void main(String[] args) {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 12345);
        try (SocketChannel socketChannel = SocketChannel.open()) {
            try (socketChannel; Scanner scanner = new Scanner(System.in)) {
                socketChannel.connect(socketAddress);
                ByteBuffer inputBuffer = allocate(2 << 10);
                String msg;
                while (true) {
                    System.out.println("Введите сообщение для сервера:");
                    msg = scanner.nextLine();
                    if ("exit".equals(msg)) {
                        break;
                    }
                    socketChannel.write(wrap(msg.getBytes(UTF_8)));
                    SECONDS.sleep(SLEEP_TIME);
                    int bytesCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, bytesCount, UTF_8).trim());
                    inputBuffer.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}