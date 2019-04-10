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
        return  synMessage;
    }

    public Packet getSynAckMessage(Packet input){
        Packet synAckMessage = new Packet();
        synAckMessage.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        synAckMessage.getHeader().setSeqNum(random.nextInt());
        synAckMessage.getHeader().setSynFlag();
        synAckMessage.getHeader().setAckFlag();
        synAckMessage.getHeader().setConnectionNum(input.getHeader().getConnectionNum());
        return synAckMessage;
    }

    public Packet getFileRequest(Packet input, String file){
        Packet request = new Packet();
        request.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        request.getHeader().setAckFlag();
        request.getHeader().setConnectionNum(input.getHeader().getConnectionNum());
        String payload = "GET/"+file;
        request.addData(payload.getBytes());
        request.getHeader().setSeqNum(input.getHeader().getAckNum() + payload.length());
        return request;
    }


}
