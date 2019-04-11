package com.nedap.university;


import Data.DataHandler;
import LTP.LTPConnection;
import LTP.LTPHandler;
import Packet.Packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InputHandler {
    private IOHandler ioHandler;
    private LTPHandler ltpHandler;



    public InputHandler(IOHandler ioHandler, LTPHandler ltpHandler){
        this.ioHandler = ioHandler;
        this.ltpHandler = ltpHandler;
    }

    public void handleInput(DatagramPacket input) throws UnknownHostException, SocketException {
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        if("hello".equals(new String(data)) || "LDP/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            ltpHandler.receivedBroadCast(input.getAddress(), input.getPort(), data);
        } else{
            ltpHandler.directMessage(input);//TODO not direct everything. apply filter.
        }
    }


}

