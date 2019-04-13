package LTP;

import Packet.Packet;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PacketBuilder {
    private Queue<BufferedInputStream>  filesToSend = new LinkedList<>();
    public PacketBuilder(){

    }

    public Packet getSynMessage() {
        Packet synMessage = new Packet();
        synMessage.getHeader().setSeqNum(0);
        synMessage.getHeader().setSynFlag();
        return  synMessage;
    }

    public Packet getSynAckMessage(Packet input){
        Packet synAckMessage = new Packet();
        synAckMessage.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        synAckMessage.getHeader().setSeqNum(0);
        synAckMessage.getHeader().setSynFlag();
        synAckMessage.getHeader().setAckFlag();
        return synAckMessage;
    }

    public Packet getAckMessage(Packet input){
        Packet ackMessage = new Packet();

        if(input.getHeader().getSynFlag() || input.getHeader().getFinFlag()){
            ackMessage.getHeader().setAckNum(input.getHeader().getSeqNum() + 1);
        } else{
            ackMessage.getHeader().setAckNum(input.getHeader().getSeqNum());
        }
        ackMessage.getHeader().setSeqNum(input.getHeader().getAckNum());
        ackMessage.getHeader().setAckFlag();
        return ackMessage;
    }



}
