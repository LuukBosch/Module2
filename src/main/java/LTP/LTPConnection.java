package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;


import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class LTPConnection {
    /*
    private IOHandler ioHandler;
    private PacketBuilder packetBuilder;//Static?
    private InetAddress address;
    private int port;
    private int connectionNum;
    private NasProtocolHandler nasProtocolHandler;
    public boolean connecting = true;
    private boolean waitingForSynAck = false;
    private int expectedAck;
    private int lastSeq;
    private Queue<byte[]> TCPqueue;
    private boolean allowedToSend = true;
    ArrayList<DatagramPacket> unAcked;



    public LTPConnection(NasProtocolHandler nasProtocolHandler, IOHandler ioHandler, int connectionNum, InetAddress address, int port){
        this.address = address;
        this.port = port;
        this.ioHandler = ioHandler;
        this.connectionNum = connectionNum;
        this.nasProtocolHandler = nasProtocolHandler;
        this.packetBuilder = new PacketBuilder();
        this.TCPqueue =  new LinkedList<>();
        unAcked = new ArrayList<>();
    }


    public void handleMessage(Packet input) {
        if (connecting) {
            handleSetup(Packet input);
            if (input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) {       //SYN Message
                if (connecting) {
                    sendSynAck(input);
                }
            } else if (input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //SYN/ack Message
                if (waitingForSynAck) {
                    sendAck(input);
                    nasProtocolHandler.connected(address, port);
                    connecting = false;
                }
            }
        } else {
            if (input.getHeader().getAckNum() == expectedAck || (input.getHeader().getAckNum() == 0 && connecting)) {
                else
                if (!input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //ACK message
                    if (connecting) {
                        System.out.println("added");
                        nasProtocolHandler.connected(address, port);
                        connecting = false;
                    } else {
                        if (input.getData().length == 0) {
                            if (unacked.contains(input.getHeader().getAckNum())) {
                                unacked.remove(input);
                                addToQueue(packetBuilder.getNextPacket());
                            } else {

                            }
                            PacketBuilder.getNext();
                            unacked.add()

                        } else {
                            sendAck(input);
                            nasProtocolHandler.receive(address, port, input.getData());
                            allowedToSend = true;
                        }
                    }
                } else if (!input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && input.getHeader().getFinFlag()) { //FinMessage

                } else {
                    System.out.println("helaas");
                }
            } else {
                System.out.println("unexpedted acknum");
                System.out.println("expected acknum was: " + expectedAck);
            }
        }
    }


    public void send(String data){
        Packet packet = packetBuilder.dataPacket(data.getBytes(), expectedAck, lastSeq, connectionNum);
        expectedAck = packet.getHeader().getSeqNum();
        addToQueue(packet);
    }

    public void send(BufferedInputStream stream, String file){

    }

    public void checkQue(){
        if(allowedToSend && !TCPqueue.isEmpty()){
            Packet packet = packetBuilder.dataPacket(TCPqueue.remove(), expectedAck, lastSeq, connectionNum);
            expectedAck = packet.getHeader().getSeqNum();
            System.out.println("expected ack set to: " + expectedAck);
            allowedToSend = false;
            addToQueue(packet);
        }

    }


    public void setupConnection(InetAddress address, int port){
        Packet syn = packetBuilder.getSynMessage();
        expectedAck = syn.getHeader().getSeqNum() + 1;
        connectionNum = syn.getHeader().getConnectionNum();
        waitingForSynAck = true;
        addToQueue(syn);

    }

    public void addToUnack(DatagramPacket packet){
        unAcked.add(packet);
    }

    public void sendSynAck(Packet input){
        Packet synAck = packetBuilder.getSynAckMessage(input);
        expectedAck = synAck.getHeader().getSeqNum() + 1;
        lastSeq = synAck.getHeader().getAckNum();
        addToQueue(synAck);
    }

    public void sendAck(Packet input){
        Packet ack = packetBuilder.getAckMessage(input);
        expectedAck = ack.getHeader().getSeqNum();
        lastSeq = ack.getHeader().getAckNum();
        addToQueue(ack);
    }



    public int getConnectionNum(){
        return connectionNum;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
    */
}
