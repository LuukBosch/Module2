package client;

import Packet.Packet;
import Packet.LTPHeader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class SetupHandler {
    private DatagramSocket socket;
    private int port;
    private InetAddress address;
    private boolean isConnectionSet = false;
    Random random = new Random();
    int seqnum;


    public SetupHandler(int port, InetAddress address, DatagramSocket socket){
        this.socket = socket;
        this.port = port;
        this.address = address;
    }

    public void startSetup(){
        System.out.println("send syn phase");
        try {
            sendSyn();
            receiveSynAck();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendSyn() throws IOException {
        Packet packet = new Packet();
        seqnum = random.nextInt();
        packet.getHeader().setSynFlag();
        packet.getHeader().setAckNum(0);
        packet.getHeader().setSeqNum(seqnum);
        System.out.println("packet length is:  " + packet.getPacket().length);
        System.out.println("to address" + address);
        DatagramPacket datagrampacket
                = new DatagramPacket(packet.getPacket(), packet.getPacket().length, address, port);
        socket.send(datagrampacket);
    }

    public void receiveSynAck() throws IOException {
        byte[] buffer = new byte[9];
        DatagramPacket synAck = new DatagramPacket(buffer, buffer.length);
        socket.receive(synAck);
        System.out.println("SynAck? packet received!");
        Packet received = new Packet(synAck.getData());

        System.out.println("ack num is:           " + Integer.toBinaryString(received.getHeader().getAckNum()));
        System.out.println("ack num is:           " + received.getHeader().getAckNum());
        System.out.println("previous seqnum was:  " + Integer.toBinaryString(seqnum));
        System.out.println("previous seqnum was:  " + seqnum);

        System.out.println("flags is:             " + synAck.getData()[8]);


        if(received.getHeader().getAckNum() == seqnum + 1 && synAck.getData()[8] == 3){ //TODO synIsSet() ect.
            System.out.println("Syn Ack received!");
            sendAck(received);
        } else{
            //sendSyn();
        }

    }

    public void sendAck(Packet synAck) throws IOException {
        Packet ack = new Packet();
        System.out.println("Acknum set to " + synAck.getHeader().getSeqNum());
        ack.getHeader().setAckNum(synAck.getHeader().getSeqNum() +1);
        ack.getHeader().setSeqNum(seqnum +1);
        ack.getHeader().setAckFlag();
        DatagramPacket datagram = new DatagramPacket(ack.getPacket(), ack.getPacket().length, address, port);
        socket.send(datagram);
        System.out.println("ack send!");
    }

    public boolean isConnected(){
        return isConnectionSet;
    }


}
