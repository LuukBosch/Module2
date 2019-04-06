package com.nedap.university;


import Packet.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class ConnectionHandlerPi extends Thread {
    private InetAddress address;
    private int port;
    private DatagramSocket socket;
    private InputHandler inputhandler;
    Random random = new Random();
    private int state = 0; //0 no connection made, 1 connection made;

    int TTL;//TODO insert time to live field?;

    public ConnectionHandlerPi(DatagramPacket input){
        this.port = input.getPort();
        this.address = input.getAddress();
        inputhandler = new InputHandler(this);
        try {
            socket = new DatagramSocket(port);
            sendSynAck(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){
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

    public void sendSynAck(DatagramPacket packet) throws IOException {
        Packet synPacket = new Packet(packet.getData());
        Packet synAckpacket = new Packet();
        synAckpacket.getHeader().setSynFlag();
        synAckpacket.getHeader().setAckFlag();
        synAckpacket.getHeader().setSeqNum(random.nextInt());
        synAckpacket.getHeader().setAckNum(synPacket.getHeader().getSeqNum()+1);
        byte[] buffer = synAckpacket.getPacket();
        DatagramPacket datagram
                = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(datagram);
        System.out.println("Synack send!");
    }



}
