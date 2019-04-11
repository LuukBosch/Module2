package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class LTPHandler {
    IOHandler ioHandler;
    ArrayList<LTPConnection> connections = new ArrayList<>();
    Random random = new Random();
    NasProtocolHandler nasProtocolHandler;

    public LTPHandler(NasProtocolHandler nasProtocolHandler, String name, int port){
        try {
            this.nasProtocolHandler = nasProtocolHandler;
            ioHandler = new IOHandler(this, name, port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void directMessage(DatagramPacket input){
        boolean connected = false;
        byte[] data = new byte[input.getLength()];
        System.arraycopy(input.getData(), input.getOffset(), data, 0, input.getLength());
        Packet inputPacket = new Packet(data);
        System.out.println(new String(data));
        if(checkCorrupted(inputPacket)) {
            for (LTPConnection connection : connections) {
                if (connection.getConnectionNum() == inputPacket.getHeader().getConnectionNum()) {
                    connection.handleMessage(inputPacket);
                    connected = true;
                    break;
                }
            }
            if (!connected && inputPacket.getHeader().getSynFlag()) {
                System.out.println("new connection!");
                LTPConnection newConnection = new LTPConnection(nasProtocolHandler, ioHandler, inputPacket.getHeader().getConnectionNum(), input.getAddress(), input.getPort());
                connections.add(newConnection);
                newConnection.handleMessage(inputPacket);
            }
        } else{
            System.out.println("dropped!");
        }
    }

    public LTPConnection getConnection(InetAddress address, int port){
        for(LTPConnection connection: connections){
            if(connection.getAddress().equals(address) && connection.getPort() == port){
                return connection;
            }
        }
        return null;
    }

    public void send(InetAddress address, int port, String data){
        LTPConnection connection = getConnection(address, port);
        if(connection != null){
            connection.send(data);
        } else{
            //Error message
            System.out.println("No connection");
        }
    }

    public void startConnection(NasProtocolHandler nasProtocolHandler, InetAddress address, int port){
        int connectionNum = (random.nextInt() & 0xFFFF);
        LTPConnection connection = new LTPConnection(nasProtocolHandler, ioHandler, connectionNum, address, port);
        connections.add(connection);
        connection.setupConnection(address, port);
    }

    public void sendBroadCast(int port, String message){
        byte[] buffer = message.getBytes();
        try {
            DatagramPacket broadcast = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("172.16.1.255"), port);
            ioHandler.addToSendQueue(broadcast);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void receivedBroadCast(InetAddress address, int port, byte[] message){
        nasProtocolHandler.receiveBroadCast(address, port, message);
    }


    public boolean checkCorrupted(Packet input){
        int receivedChecksum = input.getHeader().getChecksum();
        input.getHeader().setChecksum(0);
        Checksum checksum = new CRC32();
        checksum.update(input.getPacket(), 0, input.getPacket().length);
        int checksumValue = (int) (checksum.getValue()  & 0xffffffffL);
        return (checksumValue == receivedChecksum);
    }





}
