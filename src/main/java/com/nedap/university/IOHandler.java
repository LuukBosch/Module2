package com.nedap.university;


import LTP.LTPHandler;
import Packet.Packet;

import java.io.IOException;
import java.net.*;

import java.util.*;

import static java.net.InetAddress.getByName;


public class IOHandler extends Thread{
    private static final int TIMEOUT = 10;
    private String name;
    private int port;
    private InputHandler inputHandler;
    private LTPHandler ltpHandler;
    private DatagramSocket socket;
    private boolean stopped = false;
    private Queue<DatagramPacket> sendQueue = new LinkedList<>();


    public IOHandler(LTPHandler ltpHandler, String name, int port) throws SocketException {
        this.ltpHandler = ltpHandler;
        this.port = port;
        socket = new DatagramSocket(port);
        socket.setBroadcast(true);
        socket.setSoTimeout(TIMEOUT);
        inputHandler = new InputHandler(this, ltpHandler);
        this.name = name;
        this.start();
    }

    public void run(){
        while(!stopped) {
            try {
                this.receive();
                this.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void send() throws IOException {
        if(!sendQueue.isEmpty()) {
            socket.send(sendQueue.remove());
        }
    }

    public void receive() throws IOException {
        byte[] buffer = new byte[50];
        DatagramPacket receivedDatagram = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(receivedDatagram);
            if(!receivedDatagram.getAddress().equals(getFirstNonLoopbackAddress())) {
                inputHandler.handleInput(receivedDatagram);
            }
        } catch(SocketTimeoutException e){
        }
    }


    public void addToSendQueue(DatagramPacket packet){
        sendQueue.add(packet);
    }




    public void exit(){
        stopped = true;
    }


    public static InetAddress getFirstNonLoopbackAddress() throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        return addr;
                    }
                }
            }
        }
        return null;
    }


}
