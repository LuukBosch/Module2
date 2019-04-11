package LTP;

import Packet.Packet;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

public class PacketBuilder {
    Random random;
    public PacketBuilder(){
        random = new Random();

    }

    public Packet getSynMessage() {
        Packet synMessage = new Packet();
        synMessage.getHeader().setSeqNum(random.nextInt());
        synMessage.getHeader().setSynFlag();
        synMessage.getHeader().setConnectionNum(random.nextInt() & 0xFFFF);//TODO prevent double connectionNum, Generate connection LTPConnection???
        synMessage.setChecksum();
        System.out.println("Checksum Send is: " + synMessage.getHeader().getChecksum());
        return  synMessage;
    }

    public Packet getSynAckMessage(Packet input){
        Packet synAckMessage = new Packet();
        synAckMessage.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        synAckMessage.getHeader().setSeqNum(random.nextInt());
        synAckMessage.getHeader().setSynFlag();
        synAckMessage.getHeader().setAckFlag();
        synAckMessage.getHeader().setConnectionNum(input.getHeader().getConnectionNum());
        synAckMessage.setChecksum();
        return synAckMessage;
    }

    public Packet getAckMessage(Packet input){
        Packet ackMessage = new Packet();
        ackMessage.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        ackMessage.getHeader().setSeqNum(input.getHeader().getAckNum());
        ackMessage.getHeader().setAckFlag();
        ackMessage.getHeader().setConnectionNum(input.getHeader().getConnectionNum());
        ackMessage.setChecksum();
        return ackMessage;
    }

    public Packet dataPacket(String data, int seq, int ack, int connectionNum){
        Packet dataMessage = new Packet();
        dataMessage.getHeader().setAckNum(ack);
        dataMessage.getHeader().setSeqNum(seq);
        dataMessage.getHeader().setConnectionNum(connectionNum);
        dataMessage.getHeader().setAckFlag();
        dataMessage.addData(data.getBytes());
        dataMessage.setChecksum();
        return dataMessage;
    }


}
