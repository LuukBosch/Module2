package application;

import Data.DataHandler;
import nasprotocol.NasProtocolHandler;
import java.net.InetAddress;
import java.util.ArrayList;


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

    public void getFile(String file ,NasServer server){
        nasProtocol.getRequest(server.getAddress(), server.getPort(), file);
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

    public void receiveGet(InetAddress address, int port, String getRequest){
        if(dataHandler.hasFile(getRequest)){
            nasProtocol.send(address, port, dataHandler.getFile(getRequest));
        } else{

        }
    }

    public void receivePost(InetAddress address, int port, String getRequest){
        // start download procedure
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


