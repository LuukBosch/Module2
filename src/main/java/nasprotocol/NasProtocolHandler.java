package nasprotocol;

import LTP.LTPHandler;
import application.Application;

import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;

public class NasProtocolHandler {
    private LTPHandler ltpHandler;
    Application application;

    public NasProtocolHandler(Application application, String name, int port){
        this.application = application;
        ltpHandler = new LTPHandler(this, name, port);
    }

    public void getRequest(InetAddress address, int port, String file){
        ltpHandler.send(address, port, "GET/" + file);
    }



    public void PostFile(String file) {
        //ltpHandler.send("POST/" + file);
    }


    public void Pauze(String file){
        //ltpHandler.send("PAUZE/" + file);
    }


    public void Resume(String file){
        //ltpHandler.send("RESUME/" + file);
    }

    public void send(InetAddress address, int port, File file){
        LTPHandler.send(address, port, file);

    }

    public void receive(InetAddress address, int port, byte[] data){
        String[] splitMessage = (new String(data).split("/"));  //TODO split byte array on /?
        if("GET/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            application.receiveGet(address, port, splitMessage[1]);

        } else if("POST/".equals(new String(Arrays.copyOfRange(data, 0, 5)))){
            application.receivePost(address, port, splitMessage[1]);

        } else{
            //application.receiveMessage(address, port, splitMessage[0], splitMessage[1].getBytes());
        }
    }

    public void setupConnection(InetAddress address, int port){
        ltpHandler.startConnection(this, address, port);
    }

    public void connected(InetAddress address, int port){
        application.connected(address, port);
    }

    public void sendBroadCast(int port, String message){
        ltpHandler.sendBroadCast(port, message);
    }

    public void receiveBroadCast(InetAddress address, int port, byte[] message){
        application.receiveBroadcast(address, port, message);
    }





}
