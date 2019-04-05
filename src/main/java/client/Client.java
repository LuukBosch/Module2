package client;

import java.io.IOException;
import java.net.*;

public class Client {
    private DatagramSocket socket;
    boolean connected = false;
    private DatagramPacket receivedHello;

    public Client(){
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        Client client = new Client();
        while(!client.isConnected()) {
            client.broadcast("Hello", InetAddress.getByName("255.255.255.255"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.receiveHello();
         }
        ConnectionHandler connectionHandler = new ConnectionHandler(client.getHello().getPort(), client.getHello().getAddress());
        }




    public void broadcast(
            String broadcastMessage, InetAddress address) throws IOException {

        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, 8888);
        socket.send(packet);
    }

    public void receiveHello() throws IOException {
            byte[] buffer = new byte[37];
            DatagramPacket receivedpacket
                    = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivedpacket);
            System.out.println(new String(receivedpacket.getData()));
            if (new String(receivedpacket.getData()).equals("I'm the BestServerInTheWorld/Tribute2")) {
                connected = true;
                socket.close();
                receivedHello = receivedpacket;

            }



    }

    public DatagramPacket getHello(){
        return receivedHello;
    }

    public boolean isConnected(){
        return connected;
    }
}
