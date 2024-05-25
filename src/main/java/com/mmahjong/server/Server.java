package com.mmahjong.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static ArrayList<Socket> onLineSocket = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("----------服务端启动----------");
        ServerSocket serverSocket = new ServerSocket(8888);
        // 分发消息
        while (true) {
            Socket socket = serverSocket.accept();
            onLineSocket.add(socket);
            System.out.println(socket.getRemoteSocketAddress() + "上线了");
            System.out.println(onLineSocket);
            if (onLineSocket.size()==4){
                for (Socket onLineSocket : Server.onLineSocket) {
                    OutputStream os = onLineSocket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeUTF("GameStart");
                    dos.flush();
                }
            }
            // GameStart();
            new ServerReaderThread(socket).start();
        }

    }

}
