package com.nedap.university;

import TUI.TUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    private boolean exit = false;
    private IOHandler ioHandler;
    private String name;
    private static final String broadcastMessage = "hello";
    private TUI tui;

    public Main(int port, String name) {
        this.name = name;
        try {
            ioHandler = new IOHandler(port, name);
            tui = new TUI(this, ioHandler.getInputHandler().getServerInfo());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        Main main = new Main(8888, "computer");
        main.broadcastHello();
        main.tui.start();
        while(!main.exit){
            main.ioHandler.receive();
            main.ioHandler.send();
        }

    }
    public void broadcastHello() throws UnknownHostException {
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket broadcast
                = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("172.16.1.255"), 8888);
        ioHandler.addToSendQueue(broadcast);
    }

    public IOHandler getIoHandler(){
        return ioHandler;
    }
}


