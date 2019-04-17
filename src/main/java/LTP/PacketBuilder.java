package LTP;

import Packet.Packet;


import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Class that creates some standard messages that are needed the connection and disconnecting phase.
 */
public class PacketBuilder {
    private Queue<BufferedInputStream>  filesToSend = new LinkedList<>();

    /**
     * Creates message with only syn flag set, ackNum = 0 and seqNum = 1
     * @return
     */
    public Packet getSynMessage() {
        Packet synMessage = new Packet();
        synMessage.getHeader().setSeqNum(0);
        synMessage.getHeader().setSynFlag();
        return  synMessage;
    }

    /**
     * Creates message with syn and ack flag set, ackNum = 1 and seqNum = 0;
     * @param input
     * @return
     */
    public Packet getSynAckMessage(Packet input){
        Packet synAckMessage = new Packet();
        synAckMessage.getHeader().setAckNum(1);
        synAckMessage.getHeader().setSeqNum(0);
        synAckMessage.getHeader().setSynFlag();
        synAckMessage.getHeader().setAckFlag();
        return synAckMessage;
    }

    /**
     * * Creates message with ack flag set, ackNum = 1 and seqNum = 1;
     * @param input
     * @return
     */
    public Packet getAckMessage(Packet input){
        Packet ackMessage = new Packet();
        ackMessage.getHeader().setSeqNum(1);
        ackMessage.getHeader().setAckNum(0);
        ackMessage.getHeader().setAckFlag();
        return ackMessage;
    }


    /**
     * * Creates message with fin flag set, ackNum = 0 and seqNum = 0;
     * @return
     */
    public Packet getFinMessage(){
        Packet finMessage = new Packet();
        finMessage.getHeader().setFinFlag();
        return finMessage;

    }

    /**
     * * Creates message with fin and ack flags set, ackNum = 0 and seqNum = 0;
     * @return
     */
    public Packet getFinAckMessage(){
        Packet finAckMessage = new Packet();
        finAckMessage.getHeader().setFinFlag();
        finAckMessage.getHeader().setAckFlag();
        return finAckMessage;
    }



}
