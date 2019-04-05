package com.nedap.university;


import client.ConnectionHandler;

import java.net.DatagramPacket;

public class InputHandler {
    ConnectionHandlerPi connectionHandler;

    public InputHandler(ConnectionHandlerPi connectionHandler){
        this.connectionHandler = connectionHandler;
    }

    public DatagramPacket getReply(DatagramPacket input){
        if(!connectionHandler.isConnected()) {
            System.out.println(new String(input.getData()));
        }
        return null;
    }
}
