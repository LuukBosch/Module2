package application;

import Data.DataHandler;
import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Application {
    private String name;
    private int port;
    private NasProtocolHandler nasProtocol;
    private DataHandler dataHandler;
    private ArrayList<NasServer> connections = new ArrayList<>();
    private ArrayList<NasServer> availableServers = new ArrayList<>();



    public Application(String name, int port){
        this.name = name;
        this.port = port;
        nasProtocol = new NasProtocolHandler(this, name, port);
        sendBroadcast(8888, "hello");
        dataHandler = new DataHandler();
    }

    public void connect(InetAddress address, int port){
        nasProtocol.setupConnection(address, port);
    }

    public void getFile(String file){
        //nasProtocol.getRequest(file);
    }

    public void postFile(String file){
        //nasProtocol.postRequest(file);
    }

    public void pauzeDownload(String file){
        //nasProtocol.pauzeRequest(file);

    }

    public void resumeDownload(String file){
        //nasProtocol.resumeRequest(file);

    }

    public void connected(InetAddress address, int port){
        connections.add(getServer(address, port));

    }

    public void receiveMessage(String file, byte[] packet){

    }

    public ArrayList<NasServer> getConnections() {
        return connections;
    }

    public ArrayList<NasServer> getAvailableServers() {
        return availableServers;
    }

    public void sendBroadcast(int port, String message){
        nasProtocol.sendBroadCast(port, message);
    }

    public void receiveBroadcast(InetAddress address, int port, byte[] message){
        String[] stringMessage = new String(message).split("/");
        if(stringMessage[0].equals("hello")){
            sendBroadcast( port, "LDP/" + name + "/" + dataHandler.getFileList());
        } else if(stringMessage[0].equals("LDP")) {
            if (!isKnown(stringMessage[1])) {
                NasServer nasServer = new NasServer(address, port, stringMessage[1], stringMessage[2]);
                availableServers.add(nasServer);
            }
        }
    }

    public NasServer getServer(InetAddress address, int port) {
        for (NasServer server : availableServers) {
            if (server.getAddress().equals(address) && server.getPort() == port) {
                return server;
            }
        }
        return null;
    }

    public boolean isKnown(String name){ //TODO niet allen op naam?
        for(NasServer server: availableServers){
            if(server.getName().equals(name)){
                return true;
            }
          }
        return false;
        }
    }


