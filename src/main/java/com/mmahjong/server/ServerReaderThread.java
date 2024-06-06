package com.mmahjong.server;

import java.io.*;
import java.net.Socket;

public class ServerReaderThread extends Thread{
    private Socket socket;
    public ServerReaderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            DataInputStream operate = new DataInputStream(is);
            while (true){
                try {
                    String playerOperate = operate.readUTF();
                    System.out.println(playerOperate);
                    sendMessageToAll(playerOperate);
                } catch (Exception e) {
                    System.out.println(socket.getRemoteSocketAddress() + "下线了");
                    Server.onLineSocket.remove(socket);
                    operate.close();
                    socket.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void sendMessageToAll(String msg) throws IOException {
        for (Socket onLineSocket : Server.onLineSocket) {
            OutputStream os = onLineSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(msg);
            dos.flush();
        }
    }
}
