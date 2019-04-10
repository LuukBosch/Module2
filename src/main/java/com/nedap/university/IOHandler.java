package com.nedap.university;


import LTP.LTPHandler;

import java.io.IOException;
import java.net.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;

import static java.net.InetAddress.getByName;


public class IOHandler {
    private static final int TIMEOUT = 10;
    private String name;
    private DatagramSocket socket;
    private InputHandler inputHandler;
    private LTPHandler ltpHandler;
    private boolean stopped = false;

    private Queue<DatagramPacket> sendQueue = new LinkedList<>();

    //TODO autocloseable
    public IOHandler(LTPHandler ltpHandler, String name, int port) throws SocketException {
        this.ltpHandler = ltpHandler;
        socket = new DatagramSocket(port);
        socket.setBroadcast(true);
        socket.setSoTimeout(TIMEOUT);
        inputHandler = new InputHandler(this, ltpHandler);
        this.name = name;
    }

    public void startSendingreceive(){
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

    public String getName(){
        return name;
    }

    public InputHandler getInputHandler(){
        return inputHandler;
    }

    public void stop(){
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