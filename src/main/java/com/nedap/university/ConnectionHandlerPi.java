package com.nedap.university;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionHandlerPi extends Thread {
    private InetAddress address;
    private int port;
    private DatagramSocket socket;
    private InputHandler inputhandler;
    private int state = 0; //0 no connection made, 1 connection made;

    int TTL;//TODO insert time to live field?;

    public ConnectionHandlerPi(DatagramPacket input){
        this.port = input.getPort();
        this.address = input.getAddress();
        inputhandler = new InputHandler(this);
        try {
            socket = new DatagramSocket(port);
            reply();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void  run(){
        byte[] buffer = new byte[512];
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            DatagramPacket reply = inputhandler.getReply(packet);
            socket.send(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reply()throws IOException {

        System.out.println("Reply send!");
        byte[] buffer = "I'm the BestServerInTheWorld/Tribute2".getBytes();
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);

    }

    public boolean isConnected(){
        return state == 1;
    }


}
