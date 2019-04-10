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
    private ServerInfo serverInfo;
    private LTPHandler ltpHandler;



    public InputHandler(IOHandler ioHandler, LTPHandler ltpHandler){
        this.ioHandler = ioHandler;
        serverInfo = new ServerInfo();
        this.ltpHandler = ltpHandler;
    }

    public void handleInput(DatagramPacket input) throws UnknownHostException, SocketException {
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        if("hello".equals(new String(data))){
            sendName(input.getAddress(), input.getPort());
            sendFileList(input.getAddress(), input.getPort());
        } else if("LPD/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            storeServer(input.getAddress(), input.getPort(), data);
        } else if(("LPSF/".equals(new String(Arrays.copyOfRange(data, 0, 5))))){
            addfileList(input.getAddress(), data);
        } else{
            ltpHandler.directMessage(input);//TODO not direct everything. apply filter.
        }
    }

    public void sendName(InetAddress address, int port){
        byte[] hello = ("LPD/" + ioHandler.getName()).getBytes();
        DatagramPacket message = new DatagramPacket(hello, hello.length, address, port);
        ioHandler.addToSendQueue(message);
    }

    public void storeServer(InetAddress address, int port, byte[] data) throws UnknownHostException {
        if(!address.equals(InetAddress.getLocalHost())) {
            serverInfo.setServerName(new String(Arrays.copyOfRange(data, 4, data.length)));
            serverInfo.setAddress(address);
            serverInfo.setPort(port);
        }
    }

    public void sendFileList(InetAddress address, int port){
        byte[] file = ("LPSF/" + DataHandler.getFileList()).getBytes();
        DatagramPacket files = new DatagramPacket(file, file.length, address, port);
        ioHandler.addToSendQueue(files);
    }

    public void addfileList(InetAddress address, byte[] data) throws UnknownHostException, SocketException {
        if(!address.equals(ioHandler.getFirstNonLoopbackAddress())){
            serverInfo.addFile(new String(data).replaceFirst("LPSF/", ""));//TODO specify server;
        }
    }

    public ServerInfo getServerInfo(){
        return serverInfo;
    }

    public LTPHandler getLTPHandler(){
        return ltpHandler;
    }


}

