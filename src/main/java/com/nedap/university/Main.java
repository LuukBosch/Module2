package com.nedap.university;

import TUI.TUI;
import application.Application;

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


    public static void main(String[] args) throws IOException {
        Application test = new Application("test", 7531);
        TUI tui = new TUI(test);
        tui.start();

    }

}


