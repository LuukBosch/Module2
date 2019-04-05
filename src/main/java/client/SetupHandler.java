package client;

import Packet.Packet;
import Packet.LTPHeader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SetupHandler {
    private DatagramSocket socket;
    private int port;
    private InetAddress address;
    private boolean isConnectionSet = false;


    public SetupHandler(int port, InetAddress address, DatagramSocket socket){
        this.socket = socket;
        this.port = port;
        this.address = address;
    }

    public void startSetup(){
        System.out.println("send syn phase");
        try {
            sendSyn();
            //receiveSynAck();
            //sendAck();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendSyn() throws IOException {
        Packet packet = new Packet();
        packet.getHeader().setSynFlag();
        packet.getHeader().setAckNum(0);
        packet.getHeader().setSeqNum(0);
        System.out.println("packet length is:  " + packet.getPacket().length);
        System.out.println("to address" + address);
        DatagramPacket datagrampacket
                = new DatagramPacket(packet.getPacket(), packet.getPacket().length, address, port);
        socket.send(datagrampacket);


    }

    public boolean isConnected(){
        return isConnectionSet;
    }


}
