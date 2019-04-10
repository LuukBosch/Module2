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

    }



    public static void main(String[] args) throws IOException {
        Main main = new Main(8888, "computer");
        main.broadcastHello();
        main.tui.start();
        while(!main.exit){

        }

    }

    public void broadcastHello() throws UnknownHostException {

    }

    public IOHandler getIoHandler(){
        return ioHandler;
    }
}


