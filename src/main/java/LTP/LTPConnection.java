package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;
import sun.jvm.hotspot.debugger.Address;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class LTPConnection {
    private IOHandler ioHandler;
    private PacketBuilder packetBuilder;//Static?
    private InetAddress address;
    private int port;
    private int connectionNum;
    private NasProtocolHandler nasProtocolHandler;
    public boolean connecting = true;
    private int expectedAck;
    private int lastSeq;





    public LTPConnection(NasProtocolHandler nasProtocolHandler, IOHandler ioHandler, int connectionNum, InetAddress address, int port){
        this.address = address;
        this.port = port;
        this.ioHandler = ioHandler;
        this.connectionNum = connectionNum;
        this.nasProtocolHandler = nasProtocolHandler;
        this.packetBuilder = new PacketBuilder();
    }




    public void handleMessage(Packet input){

        if(input.getHeader().getAckNum() == expectedAck || input.getHeader().getAckNum() == 0) {
            if (input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) {       //SYN Message
                if (connecting) {
                    sendSynAck(input);
                }
            } else if (input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //SYN/ack Message
                if (connecting) {
                    sendAck(input);
                    nasProtocolHandler.connected(address, port);
                }
            } else if (!input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //ACK message
                if (connecting) {
                    System.out.println("added");
                    nasProtocolHandler.connected(address, port);
                    connecting = false;
                } else {
                    last
                    nasProtocolHandler.receive(address, port, input.getData());
                }
            } else if (!input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && input.getHeader().getFinFlag()) { //FinMessage

            } else {
                System.out.println("helaas");
            }
        }
    }

    public void send(String data){
        Packet packet = packetBuilder.dataPacket(data, expectedAck, lastSeq, connectionNum);
        expectedAck = packet.getData().length;
        addToQueue(packet);
    }


    public void setupConnection(InetAddress address, int port){
        Packet syn = packetBuilder.getSynMessage();
        expectedAck = syn.getHeader().getSeqNum() + 1;
        connectionNum = syn.getHeader().getConnectionNum();
        addToQueue(syn);

    }


    public void sendSynAck(Packet input){
        Packet synAck = packetBuilder.getSynAckMessage(input);
        expectedAck = synAck.getHeader().getSeqNum() + 1;
        lastSeq = synAck.getHeader().getAckNum();
        addToQueue(synAck);
    }

    public void sendAck(Packet input){
        Packet ack = packetBuilder.getAckMessage(input);
        addToQueue(ack);

    }

    public void addToQueue(Packet packet){
        byte[] packetBytes = packet.getPacket();
        DatagramPacket packetDatagram = new DatagramPacket(packetBytes, packetBytes.length, address, port);
        ioHandler.addToSendQueue(packetDatagram);

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
}
