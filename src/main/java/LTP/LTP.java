package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import com.nedap.university.InputHandler;
import nasprotocol.NasProtocolHandler;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * the LTP class is responsible for the reliable and in order delivery of data from higher layers.
 */
public class LTP {
    private NasProtocolHandler nasProtocolHandler;
    private int status;
    private final static int UNCONNECTED = 0;
    private final static int SYNSEND = 2;
    private final static int SYNACKSEND = 3;
    private final static int CONNECTED = 4;
    private int latestSeqReceived = 1;
    private int expectedAck = 1;
    private int connectedPort;
    private InetAddress connectedAddress;
    private PacketBuilder packetBuilder = new PacketBuilder();
    private PacketBuffer packetBuffer;
    private ArrayList<Packet> unAcked = new ArrayList<>();
    private Queue<Packet> sendQueue = new LinkedList<>();
    private Queue<Packet> retransmissionQueue = new LinkedList<>();
    private Queue<DatagramPacket> broadcastQueue = new LinkedList<>();
    private InputHandler inputHandler;
    private IOHandler ioHandler;


    /**
     *Creates a LTP object
     * @param nasProtocolHandler
     * @param name
     * @param port
     */
    public LTP(NasProtocolHandler nasProtocolHandler, String name, int port){
        this.nasProtocolHandler = nasProtocolHandler;
        ioHandler = new IOHandler(this, name, port);
        status = UNCONNECTED;
        packetBuffer = new PacketBuffer(this);
    }


    /**
     * Redirects a incomming DatagramPacket based upon the LTP header.
     * @param input Incoming DatagramPacket
     */
    public void directMessage(DatagramPacket input) {
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        Packet inputPacket = new Packet(data);
        if (checkUnacked(inputPacket) && checkCorrupted(inputPacket)) {
            if(inputPacket.getHeader().getFinFlag() && inputPacket.getHeader().getAckFlag()){
                handledisconnect();
            } else if(inputPacket.getHeader().getFinFlag()){
                handledisconnect();
                sendQueue.add(packetBuilder.getFinAckMessage());
            }
            packetBuffer.refillQueue();
            switch (status) {
                case UNCONNECTED:
                    if(inputPacket.getHeader().getSynFlag()){
                        handleSyn(input.getAddress(), input.getPort(), inputPacket);
                    }
                    break;
                case SYNSEND:
                    if((inputPacket.getHeader().getAckFlag() && inputPacket.getHeader().getSynFlag() && input.getAddress().equals(connectedAddress))) {
                        nasProtocolHandler.connected(connectedAddress, connectedPort);
                        handleSynAck(inputPacket);
                    }
                    break;
                case SYNACKSEND:
                    if(input.getAddress().equals(connectedAddress) && inputPacket.getHeader().getAckFlag()){
                        nasProtocolHandler.connected(connectedAddress, connectedPort);
                        status = CONNECTED;
                    }
                    break;
                case CONNECTED:
                    if(input.getAddress().equals(connectedAddress) ){
                        latestSeqReceived = inputPacket.getHeader().getSeqNum();
                        if(inputPacket.getData().length != 0) {
                            if (sendQueue.isEmpty()) {
                                sendQueue.add(new Packet());
                            }
                            //System.out.println(inputPacket.getHeader().getSeqNum());
                            nasProtocolHandler.receive(inputPacket.getData());
                         }
                        }
                    break;
            }
        } else{
            System.out.println("niet goed!");
            System.out.println(checkCorrupted(inputPacket));
            if(latestSeqReceived == inputPacket.getHeader().getSeqNum() &&  inputPacket.getData().length != 0 &&status == CONNECTED){
                Packet packet = new Packet();
                packet.getHeader().setAckFlag();
                sendQueue.add(packet);
            }
        }
    }


    /**
     * Checks if the incoming packet is valid according to the LTP protocol
     * @param input
     * @return
     */
    public boolean checkUnacked(Packet input){
        if((unAcked.isEmpty() && latestSeqReceived != input.getHeader().getSeqNum())){
            return true;
        }
        if((input.getHeader().getSynFlag() && !input.getHeader().getAckFlag()) || input.getHeader().getFinFlag() || (input.getHeader().getFinFlag() && input.getHeader().getAckFlag())){
            return true;
        }
        else {
            for(int i = 0; i<unAcked.size(); i++){
                if (unAcked.get(i).getHeader().getSeqNum() == input.getHeader().getAckNum()) {
                    unAcked.remove(i);
                    return true;
                } else if(input.getHeader().getSynFlag() && input.getHeader().getAckFlag()
                        && unAcked.get(i).getHeader().getSeqNum() +1 == input.getHeader().getAckNum()){
                    unAcked.remove(unAcked.get(i));
                    System.out.println("syn removed!");
                    return true;
                } else if(status == SYNACKSEND && input.getHeader().getAckFlag() && unAcked.get(i).getHeader().getSeqNum() + 1 == input.getHeader().getAckNum()){
                    unAcked.remove(unAcked.get(i));
                    System.out.println("synack removed!");
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Sends a Syn message to a specific address and port number and changes status
     * @param address
     * @param port
     */
    public void startConnection(InetAddress address, int port){
        connectedAddress = address;
        connectedPort = port;
        Packet packet = packetBuilder.getSynMessage();
        sendQueue.add(packet);
        status = SYNSEND;
    }

    /**
     * Sends a SynAck message to a specific address and port number and changes status
     * @param address
     * @param port
     * @param input
     */
    public void handleSyn(InetAddress address, int port, Packet input){
        connectedAddress = address;
        connectedPort = port;
        Packet packet = packetBuilder.getSynAckMessage(input);
        sendQueue.add(packet);
        status = SYNACKSEND;
    }


    /**
     * Finishes the connection setup by sending a Ack and changing connection status
     * @param input
     */
    public void handleSynAck(Packet input) {
        Packet packet = packetBuilder.getAckMessage(input);
        sendQueue.add(packet);
        status = CONNECTED;
    }


    /**
     * Adds a unAcked message to the retransmission protocol
     */
    public void addRetransmission(){
        if(!unAcked.isEmpty()) {
            //System.out.println("retransmission!!!!!!!!!!!!");
            if(!unAcked.isEmpty()) {
                unAcked.get(0).print();
                retransmissionQueue.add(unAcked.get(0));
                unAcked.remove(0);
            }
        }
    }


    /**
     * Creates a broadcast datagram and adds this datagram to the broadcastQueue.
     * @param port
     * @param message
     */
    public void sendBroadCast(int port, String message){
        try {
            InetAddress address = InetAddress.getByName("172.16.1.255");
            byte[] buffer = message.getBytes();
            DatagramPacket broadcast = new DatagramPacket(buffer, buffer.length, address, port);
            System.out.println("added to broadcast queue");
            broadcastQueue.add(broadcast);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    /**
     * Directs a broadcast message to the NasProtocol layer
     * @param address
     * @param port
     * @param message
     */
    public void receivedBroadCast(InetAddress address, int port, byte[] message){
        nasProtocolHandler.receiveBroadCast(address, port, message);
    }

    /**
     * Compares the checksum of the incomming packet with a self calculated checksum
     * @param input
     * @return
     */
//////////////////////////////////////////////CHECKSUM///////////////////////////////////////////////////////////
    public boolean checkCorrupted(Packet input){
        int receivedChecksum = input.getHeader().getChecksum();
        input.getHeader().setChecksum(0);
        Checksum checksum = new CRC32();
        checksum.update(input.getPacket(), 0, input.getPacket().length);
        int checksumValue = (int) (checksum.getValue()  & 0xffffffffL);
        return (checksumValue == receivedChecksum);
    }


    /**
     * Returns true if there are messages in the queue for sending
     * @return
     */
    public boolean hasMessage(){
        return !sendQueue.isEmpty() || !retransmissionQueue.isEmpty();
    }

    /**
     * returns a message based upon priority(Retransmissions have a higher priority than ordaniry messages).
     * @return
     */
    public Packet getMessage() {
        Packet packet;
        //System.out.println("retransmission Queue Size is:   " + retransmissionQueue.size());
        if(!retransmissionQueue.isEmpty()){
            packet = retransmissionQueue.remove();
            return packet;
        }  else if(status != CONNECTED) {
            packet = sendQueue.remove();
            packet.setChecksum();
            return packet;
        } else{
            packet = sendQueue.remove();
            packet.getHeader().setAckFlag();
            packet.getHeader().setAckNum(latestSeqReceived);
            packet.getHeader().setSeqNum(expectedAck + packet.getData().length);
            packet.setChecksum();
            expectedAck = packet.getHeader().getSeqNum();
            return packet;
            }
        }

    /**
     * Adds a packet to the unAcked list
     * @param packet
     */
    public void addToUnacked(Packet packet){
        if (!(packet.getHeader().getAckFlag() && !packet.getHeader().getSynFlag() && packet.getData().length == 0)) {
            unAcked.add(packet);
        }

    }
    public Queue<DatagramPacket> getBroadcastQueue() {
        return broadcastQueue;
    }

    public int getConnectedPort() {
        return connectedPort;
    }

    public InetAddress getConnectedAddress() {
        return connectedAddress;
    }

    public Queue<Packet> getSendQueue() {
        return sendQueue;
    }

    public PacketBuffer getPacketBuffer() {
        return packetBuffer;
    }


    public void disconnect(){
        handledisconnect();
        Packet packet = packetBuilder.getFinMessage();
        sendQueue.add(packet);
    }

    public void handledisconnect(){
        status = UNCONNECTED;
        latestSeqReceived = 1;
        expectedAck = 1;
        unAcked.clear();
        sendQueue.clear();
        retransmissionQueue.clear();
        broadcastQueue.clear();
        packetBuffer.clear();
    }

}
