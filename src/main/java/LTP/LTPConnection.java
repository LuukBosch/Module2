package LTP;

import Packet.Packet;


import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class LTPConnection {
    private LTPHandler ltpHandler;
    private PacketBuilder packetBuilder;
    private InetAddress address;
    private int port;
    private int connectionNum;
    private boolean sending = false;
    private boolean receiving = false;
    public boolean fileToRequest;
    public String desiredFile;


    public LTPConnection(LTPHandler ltpHandler, InetAddress address, int port){
        this.address = address;
        this.port = port;
        this.ltpHandler = ltpHandler;
        this.packetBuilder = new PacketBuilder();
    }

    public void handleMessage(Packet input){
        if(input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()){       //SYN Message
            connectionNum = input.getHeader().getConnectionNum();
            sendSynAck(input);
        } else if(input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()){ //SYN/ack Message
            System.out.println(fileToRequest);
            if(fileToRequest){
               sendFileRequest(input);
            }
        } else if(!input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //ACK message
            handleAckMessage(input);
        } else if(!input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && input.getHeader().getFinFlag()){ //FinMessage

        } else{
            System.out.println("helaas");
        }
    }

    public void handleAckMessage(Packet input){
        if("GET/".equals(new String(Arrays.copyOfRange(input.getData(), 0, 4)))){
            sending = true;


        } else if("POST/".equals(new String(Arrays.copyOfRange(input.getData(), 0, 5)))){
            receiving = true;

        } else if(sending){

        } else if(receiving){

        }

    }

    public void getFile(String file){
        fileToRequest = true;
        desiredFile = file;
        Packet syn = packetBuilder.getSynMessage();
        connectionNum = syn.getHeader().getConnectionNum();
        byte[] synMessageBytes = syn.getPacket();
        DatagramPacket synDatagram = new DatagramPacket(synMessageBytes,synMessageBytes.length, address, port);
        ltpHandler.getIoHandler().addToSendQueue(synDatagram);
    }

    public void sendFileRequest(Packet input){
        fileToRequest = false;
        Packet request = packetBuilder.getFileRequest(input, desiredFile);
        byte[] requestMessageBytes = request.getPacket();
        DatagramPacket requestDatagram = new DatagramPacket(requestMessageBytes,requestMessageBytes.length, address, port);
        ltpHandler.getIoHandler().addToSendQueue(requestDatagram);
    }

    public void sendSynAck(Packet input){
        Packet synAck = packetBuilder.getSynAckMessage(input);
        byte[] synAckMessageBytes = synAck.getPacket();
        DatagramPacket synAckDatagram = new DatagramPacket(synAckMessageBytes,synAckMessageBytes.length, address, port);
        ltpHandler.getIoHandler().addToSendQueue(synAckDatagram);
    }

    public int getConnectionNum(){
        return connectionNum;
    }



}
