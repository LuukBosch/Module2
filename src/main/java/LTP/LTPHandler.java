package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class LTPHandler {
    IOHandler ioHandler;
    //LTPConnection ltpConnection;
    ArrayList<LTPConnection> connections = new ArrayList<>();

    public LTPHandler(IOHandler ioHandler){
        this.ioHandler = ioHandler;
    }

    public void directMessage(DatagramPacket input){
        boolean connected = false;
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        Packet inputPacket = new Packet(data);
        if(checkCorrupted(inputPacket)) {
            for (LTPConnection connection : connections) {
                if (connection.getConnectionNum() == inputPacket.getHeader().getConnectionNum()) {
                    connection.handleMessage(inputPacket);
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                LTPConnection newConnection = new LTPConnection(this, input.getAddress(), input.getPort());
                connections.add(newConnection);
                newConnection.handleMessage(inputPacket);

            }
        } else{
            System.out.println("dropped!");
        }
    }

    public LTPConnection StartNewLTPConnection(InetAddress address, int port){
        LTPConnection connection = new LTPConnection(this, address, port);
        connections.add(connection);
        return connection;
    }


    public IOHandler getIoHandler(){
        return ioHandler;
    }

    public boolean checkCorrupted(Packet input){
        int receivedChecksum = input.getHeader().getChecksum();
        input.getHeader().setChecksum(0);
        Checksum checksum = new CRC32();
        checksum.update(input.getPacket(), 0, input.getPacket().length);
        int checksumValue = (int) (checksum.getValue()  &  4294967295l);
        return (checksumValue == receivedChecksum);
    }





}
