package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class LTP {
    private IOHandler ioHandler;
    private ArrayList<LTPConnection> connections = new ArrayList<>();
    private Random random = new Random();
    private NasProtocolHandler nasProtocolHandler;
    private int status;
    private final static int UNCONNECTED = 0;
    private final static int SYNSEND = 2;
    private final static int SYNACKSEND = 3;
    private final static int CONNECTED = 4;
    private int latestSeqReceived = 1;
    private int latestSeqSend = 1;
    private int connectedPort;
    private InetAddress connectedAddress;
    private PacketBuilder packetBuilder = new PacketBuilder();
    private PacketBuffer packetBuffer;
    private ArrayList<Packet> unAcked = new ArrayList<>();
    private Queue<Packet> sendQueue = new LinkedList<>();
    private Queue<DatagramPacket> broadcastQueue = new LinkedList<>();

    public LTP(NasProtocolHandler nasProtocolHandler, String name, int port){
        this.nasProtocolHandler = nasProtocolHandler;
        ioHandler = new IOHandler(this, name, port);
        status = UNCONNECTED;
        packetBuffer = new PacketBuffer(this);
    }

    public void directMessage(DatagramPacket input) {

        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        Packet inputPacket = new Packet(data);
        //System.out.println("packet received is.");
        //inputPacket.print();
        if (checkUnacked(inputPacket) && checkCorrupted(inputPacket)) {
            switch (status) {
                case UNCONNECTED:
                    if(inputPacket.getHeader().getSynFlag()){
                        handleSyn(input.getAddress(), input.getPort(), inputPacket);
                    }
                    break;
                case SYNSEND:
                    if((inputPacket.getHeader().getAckFlag() && inputPacket.getHeader().getSynFlag() && input.getAddress().equals(connectedAddress))){
                        nasProtocolHandler.connected(connectedAddress,connectedPort);
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
                    if(input.getAddress().equals(connectedAddress)){
                        latestSeqReceived = inputPacket.getHeader().getSeqNum();
                        latestSeqSend = inputPacket.getHeader().getAckNum();
                        if(inputPacket.getData().length != 0) {
                            nasProtocolHandler.receive(inputPacket.getData());
                            if (sendQueue.isEmpty()) {
                                sendQueue.add(new Packet());
                                }
                            }
                        }
                    break;
            }
        } else{
            System.out.println("Niet goed!");
            System.out.println(checkCorrupted(inputPacket));

        }
    }

    public void startConnection(InetAddress address, int port){
        connectedAddress = address;
        connectedPort = port;
        Packet packet = packetBuilder.getSynMessage();
        sendQueue.add(packet);
        status = SYNSEND;
    }

    public void handleSyn(InetAddress address, int port, Packet input){
        connectedAddress = address;
        connectedPort = port;
        Packet packet = packetBuilder.getSynAckMessage(input);
        sendQueue.add(packet);
        addToUnacked(packet);
        status = SYNACKSEND;
    }

    public void handleSynAck(Packet input) {
        Packet packet = packetBuilder.getAckMessage(input);
        sendQueue.add(packet);
        status = CONNECTED;
    }



    public void addToUnacked(Packet packet){
        unAcked.add(packet);
    }

    public boolean checkUnacked(Packet input){
        if(unAcked.isEmpty()){
            return true;
        } else {
            for(int i = 0; i<unAcked.size(); i++){
                if (unAcked.get(i).getHeader().getSeqNum() == input.getHeader().getAckNum()) {
                    unAcked.remove(unAcked.get(i));
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

    public void addRetransmission(){
        if(!unAcked.isEmpty()) {
            System.out.println("retransmission!!!!!!!!!!!!");
            unAcked.get(0).print();
            sendQueue.add(unAcked.get(0));
            unAcked.remove(0);
        }
    }

//////////////////////////////////////////////BROADCAST//////////////////////////////////////////////////////////

    public void sendBroadCast(int port, String message){
        try {
            InetAddress address = InetAddress.getByName("172.16.1.255");
            byte[] buffer = message.getBytes();
            DatagramPacket broadcast = new DatagramPacket(buffer, buffer.length, address, port);
            broadcastQueue.add(broadcast);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    public void receivedBroadCast(InetAddress address, int port, byte[] message){
        nasProtocolHandler.receiveBroadCast(address, port, message);
    }

//////////////////////////////////////////////CHECKSUM///////////////////////////////////////////////////////////
    public boolean checkCorrupted(Packet input){
        int receivedChecksum = input.getHeader().getChecksum();
        input.getHeader().setChecksum(0);
        Checksum checksum = new CRC32();
        checksum.update(input.getPacket(), 0, input.getPacket().length);
        int checksumValue = (int) (checksum.getValue()  & 0xffffffffL);
        return (checksumValue == receivedChecksum);
    }


    public boolean hasMessage(){
        return !sendQueue.isEmpty();
    }

    public Packet getMessage(){
        if(status != CONNECTED){
            packetBuffer.refillQueue();
            Packet packet = sendQueue.remove();
            packet.getHeader().setChecksum(0);
            packet.setChecksum();
            return packet;
        } else {
            Packet packet = sendQueue.remove();
            packet.getHeader().setAckNum(latestSeqReceived);
            packet.getHeader().setSeqNum(latestSeqSend + packet.getData().length);
            packet.getHeader().setAckFlag();
            packet.getHeader().setChecksum(0);
            packet.setChecksum();
            packetBuffer.refillQueue();
            return packet;
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

    public int getStatus() {
        return status;
    }

    public Queue<Packet> getSendQueue() {
        return sendQueue;
    }

    public PacketBuffer getPacketBuffer() {
        return packetBuffer;
    }
}
