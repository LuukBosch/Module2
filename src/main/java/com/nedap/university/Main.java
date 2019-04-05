package com.nedap.university;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {
    private static DatagramSocket socket = null;
    public static void main(String[] args) throws IOException {
        Listener test = new Listener(8888);
        test.run();
    }
}


