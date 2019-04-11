package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;


import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class LTPConnection {
    private IOHandler ioHandler;
    private PacketBuilder packetBuilder;//Static?
    private InetAddress address;
    private int port;
    private int connectionNum;
    private NasProtocolHandler nasProtocolHandler;
    public boolean connected = false;





    public LTPConnection(NasProtocolHandler nasProtocolHandler, IOHandler ioHandler, int connectionNum, InetAddress address, int port){
        this.address = address;
        this.port = port;
        this.ioHandler = ioHandler;
        this.connectionNum = connectionNum;
        this.nasProtocolHandler = nasProtocolHandler;
        this.packetBuilder = new PacketBuilder();
    }

    public void handleMessage(Packet input){
        if(input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()){       //SYN Message
            sendSynAck(input);
        } else if(input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()){ //SYN/ack Message
            
            //nasProtocolHandler.connected(address, port);
        } else if(!input.getHeader().getSynFlag() && input.getHeader().getAckFlag() && !input.getHeader().getFinFlag()) { //ACK message
            //handleAckMessage(input);
        } else if(!input.getHeader().getSynFlag() && !input.getHeader().getAckFlag() && input.getHeader().getFinFlag()){ //FinMessage

        } else{
            System.out.println("helaas");
        }
    }

    public void setupConnection(InetAddress address, int port){
        Packet syn = packetBuilder.getSynMessage();
        connectionNum = syn.getHeader().getConnectionNum();
        byte[] synMessageBytes = syn.getPacket();
        DatagramPacket synDatagram = new DatagramPacket(synMessageBytes,synMessageBytes.length, address, port);
        ioHandler.addToSendQueue(synDatagram);
    }


    public void sendSynAck(Packet input){
        Packet synAck = packetBuilder.getSynAckMessage(input);
        byte[] synAckMessageBytes = synAck.getPacket();
        DatagramPacket synAckDatagram = new DatagramPacket(synAckMessageBytes,synAckMessageBytes.length, address, port);
        ioHandler.addToSendQueue(synAckDatagram);
    }

    public int getConnectionNum(){
        return connectionNum;
    }



}
