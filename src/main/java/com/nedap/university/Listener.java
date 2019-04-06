package com.nedap.university;

import client.ConnectionHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Listener extends Thread {
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
            byte[] buffer = new byte[9];
            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length);
            System.out.println("Listening!");
            try {
                socket.receive(packet);
                handleInput(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(DatagramPacket packet) throws IOException {
        if(isHelloMessage(packet.getData())){
            System.out.println("hello received!");
            reply(packet.getPort(), packet.getAddress());
        } else if(isSynMessage(packet.getData())){
            System.out.println("Syn received");
            ConnectionHandlerPi handler = new ConnectionHandlerPi(packet);
        }

    }
    private boolean isSynMessage(byte[] payload){
        return payload[8] == 1;

    }

    private boolean isHelloMessage(byte[] payload){
        return "Hello".equals(new String(Arrays.copyOfRange(payload, 0, 5)));
    }


    private void reply(int port, InetAddress address)throws IOException {
        System.out.println("Reply send!");
        byte[] buffer = "I'm the BestServerInTheWorld/Tribute2".getBytes();
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}

