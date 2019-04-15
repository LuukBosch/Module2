package application;

import Data.DataHandler;
import nasprotocol.NasProtocolHandler;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;


public class Application {
    private String name;
    private NasProtocolHandler nasProtocol;
    private DataHandler dataHandler;
    private NasServer connectedNasServer;
    private ArrayList<NasServer> availableServers = new ArrayList<>();
    private HashMap<String, BufferedOutputStream> readers;
    private HashMap<String, CheckedOutputStream> checkedoutputStreams = new HashMap<>();

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
            CheckedOutputStream fout = null;
            try {
                fout = new CheckedOutputStream(new FileOutputStream(System.getProperty("user.home") + "/files/" + file), new CRC32());
                checkedoutputStreams.put(file, fout);
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
            CheckedInputStream check = new CheckedInputStream(new FileInputStream(System.getProperty("user.home") + "/files/" + file), new CRC32());
            BufferedInputStream bis = new BufferedInputStream(check);
            nasProtocol.postFile(file, bis, check);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void pauseDownload(String file){
        nasProtocol.pause(file);

    }

    public void resumeDownload(String file){
        nasProtocol.resume(file);
    }

    public void receiveGet(String getRequest){
        if(dataHandler.hasFile(getRequest)){
            try {
                CheckedInputStream check = new CheckedInputStream(new FileInputStream(dataHandler.getFile(getRequest).getAbsolutePath()), new CRC32());
                BufferedInputStream bis = new BufferedInputStream(check);
                nasProtocol.send(getRequest, bis, check);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else{

        }
    }



    public void receivePost(String postRequest){
        try {
            CheckedOutputStream fout = new CheckedOutputStream(new FileOutputStream(System.getProperty("user.home") + "/files/" + postRequest), new CRC32());
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            readers.put(postRequest, bout);
            checkedoutputStreams.put(postRequest, fout);
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
            if(new String(data).split("/")[0].equals("LAST")){
                System.out.println("last message!");
                readers.get(file).close();
                System.out.println("received Checksum is:   " + new String(data).split("/")[1]);
                System.out.println("calculated checksum is:    "  + checkedoutputStreams.get(file).getChecksum().getValue());
            } else {
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


