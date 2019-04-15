package application;

import Data.DataHandler;
import nasprotocol.NasProtocolHandler;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


public class Application {
    private String name;
    private NasProtocolHandler nasProtocol;
    private DataHandler dataHandler;
    private NasServer connectedNasServer;
    private ArrayList<NasServer> availableServers = new ArrayList<>();
    private HashMap<String, BufferedOutputStream> readers;
    private boolean connected;


    public Application(String name, int port){
        this.name = name;
        nasProtocol = new NasProtocolHandler(this, name, port);
        sendBroadcast(port, "hello");
        dataHandler = new DataHandler();
        readers = new HashMap<>();
    }

    public void connect(InetAddress address, int port){
        nasProtocol.setupConnection(address, port);
    }

    public void getFile(String file) {
        if (connected) {
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(System.getProperty("user.home") + "/files/" + file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            readers.put(file, bout);
            nasProtocol.getRequest(file);
        }
    }

    public void postFile(String file){
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(System.getProperty("user.home") + "/files/" + file));
            nasProtocol.postFile(bis, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void pauzeDownload(String file){
        //nasProtocol.pauzeRequest(file);

    }

    public void resumeDownload(String file){
        //nasProtocol.resumeRequest(file);
    }

    public void receiveGet(String getRequest){
        if(dataHandler.hasFile(getRequest)){
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataHandler.getFile(getRequest).getAbsolutePath()));
                System.out.println("bufferd made");
                nasProtocol.send(bis, getRequest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else{

        }
    }



    public void receivePost(String postRequest){
        try {
            FileOutputStream fout = new FileOutputStream(System.getProperty("user.home") + "/files/" + postRequest);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            readers.put(postRequest, bout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connected(InetAddress address, int port){
        connected = true;
        connectedNasServer = getServer(address, port);
    }

    public void receiveMessage(String file, byte[] packet){

    }

    public void receiveData(String file, byte[] data){
        try {
            if(new String(data).equals("LAST")){
                System.out.println("last message!");
                readers.get(file).close();
            } else {
                //System.out.println("writing data");
                //System.out.println("size data" +data.length);
                readers.get(file).write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            if (stringMessage.length == 3 && !isKnown(stringMessage[1])) {
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

    public boolean isKnown(String name){
        for(NasServer server: availableServers){
            if(server.getName().equals(name)){
                return true;
            }
          }
        return false;
        }
    }


