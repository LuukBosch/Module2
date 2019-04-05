package com.nedap.university;

import client.ConnectionHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class Listener extends Thread {
    private int port;
    byte[] keyword = "Hello".getBytes();
    private DatagramSocket socket;

    public Listener(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

    public void run() {
        while (true) {
            byte[] buffer = new byte[5];
            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length);
            System.out.println("Listening!");
            try {
                socket.receive(packet);
                System.out.println("Packet Received!");
                System.out.println("On port:  " + packet.getPort());
                System.out.println("with content:" + new String(packet.getData()));
                if("Hello".equals(new String(packet.getData()))){
                    System.out.println("hello received!");
                    ConnectionHandlerPi handler = new ConnectionHandlerPi(packet);
                }
                System.out.println("not hello message");
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            if (Arrays.equals(packet.getData(), keyword)) {
                ConnectionHandlerPi handler = new ConnectionHandlerPi(packet);
            }
            */
        }
    }
}

