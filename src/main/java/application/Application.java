package application;

import com.nedap.university.IOHandler;
import nasprotocol.NasProtocolHandler;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    private String name;
    private int port;
    private NasProtocolHandler nasProtocol;
    private HashMap<InetAddress, Integer> connections = new HashMap<>();


    public Application(String name, int port){
        this.name = name;
        this.port = port;
        nasProtocol = new NasProtocolHandler(this, name, port);
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
        connections.put(address, port);

    }

    public void receiveMessage(String file, byte[] packet){

    }

    public void sendBroadcast(int port, String message){
        nasProtocol.sendBroadCast(port, message);
    }

    public void receiveBroadcast(InetAddress address, int port, String message){
        //Add to available servers;

    }



}
