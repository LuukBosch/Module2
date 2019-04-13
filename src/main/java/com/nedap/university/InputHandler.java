package com.nedap.university;


import LTP.LTP;


import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InputHandler {
    private IOHandler ioHandler;
    private LTP ltp;



    public InputHandler(IOHandler ioHandler, LTP ltp){
        this.ioHandler = ioHandler;
        this.ltp = ltp;
    }

    public void handleInput(DatagramPacket input){
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        if("hello".equals(new String(data)) || "LDP/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            ltp.receivedBroadCast(input.getAddress(), input.getPort(), data);
        } else{
            ltp.directMessage(input);//TODO not direct everything. apply filter.
        }
    }


}

