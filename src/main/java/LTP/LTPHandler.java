package LTP;

import Packet.Packet;
import com.nedap.university.IOHandler;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

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
        for(LTPConnection connection: connections){
            if(connection.getConnectionNum() == inputPacket.getHeader().getConnectionNum()){
                connection.handleMessage(inputPacket);
                connected = true;
                break;
            }
        }

        if(!connected){
            LTPConnection newConnection = new LTPConnection(this, input.getAddress(), input.getPort());
            connections.add(newConnection);
            newConnection.handleMessage(inputPacket);

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





}
