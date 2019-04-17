package com.nedap.university;

import TUI.TUI;
import application.Application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws IOException {
        Application test = new Application(config.NAME, config.PORTNUMBER);
        TUI tui = new TUI(test);
        tui.start();
    }

}


