package com.nedap.university;


import LTP.LTP;
import Packet.Packet;

import java.io.IOException;
import java.net.*;

import java.util.*;


public class IOHandler extends Thread{
    private static final int TIMEOUT =300;
    private String name;
    private int port;
    private InputHandler inputHandler;
    private LTP ltp;
    private boolean stopped = false;
    //private Queue<Packet> sendQueue = new LinkedList<>();
    private Queue<DatagramPacket> broadcastqueue = new LinkedList<>();
    int count;


    public IOHandler(LTP ltp, String name, int port){
        this.ltp = ltp;
        this.port = port;
        inputHandler = new InputHandler(this, ltp);
        this.name = name;
        this.start();
        count = 0;
    }

    public void run(){
            try(DatagramSocket socket = new DatagramSocket(port)) {
                while(!stopped) {
                    socket.setBroadcast(true);
                    socket.setSoTimeout(TIMEOUT);
                    this.send(socket);
                    this.receive(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void send(DatagramSocket socket) throws IOException {
        if(ltp.hasMessage()) {
            //int rand = (int)(Math.random() * 10) + 1;
            Packet packet = ltp.getMessage();
            DatagramPacket packetDatagram = new DatagramPacket(packet.getPacket(),
                    packet.getPacket().length, ltp.getConnectedAddress(), ltp.getConnectedPort());
            if (!(packet.getHeader().getAckFlag() && packet.getData().length == 0)) {
                ltp.addToUnacked(packet);
            }
            /*
            if(rand == 5){
                System.out.println("Packet Dropped!!!!!!!!!!!!!!!!!");
                packet.print();
            } else {*/
                System.out.println("Packet Send is:");
                packet.print();
                socket.send(packetDatagram);


        } else if(!ltp.getBroadcastQueue().isEmpty()){
            socket.send(ltp.getBroadcastQueue().remove());
        }
    }

    public void receive(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1500];
        DatagramPacket receivedDatagram = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(receivedDatagram);
            if(!receivedDatagram.getAddress().equals(getFirstNonLoopbackAddress())) {
                inputHandler.handleInput(receivedDatagram);
            }
        } catch(SocketTimeoutException e){
            ltp.addRetransmission();
        }
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
