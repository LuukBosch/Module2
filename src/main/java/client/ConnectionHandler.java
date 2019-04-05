package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ConnectionHandler extends Thread{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private SetupHandler setupHandler;
    public ConnectionHandler(int port, InetAddress address){
        this.address = address;
        this.port = port;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("new ConnectionHandler Created!");
        setupConnection();
    }

    public void setupConnection(){
        setupHandler = new SetupHandler(port, address, socket);
        setupHandler.startSetup();
        while(!setupHandler.isConnected()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //Todo remove duplicate code
    public void run(){
        while(true) {
            byte[] buffer = new byte[512];
            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length);
            System.out.println("Listening!");
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
