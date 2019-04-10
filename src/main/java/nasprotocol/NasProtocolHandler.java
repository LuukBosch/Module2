package nasprotocol;

import LTP.LTPHandler;
import application.Application;

import java.net.InetAddress;

public class NasProtocolHandler {
    private LTPHandler ltpHandler;
    Application application;

    public NasProtocolHandler(Application application, String name, int port){
        this.application = application;
        ltpHandler = new LTPHandler(this, name, port);
    }

    public void getFile(String file){
        //ltpHandler.send("GET/" + file);
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

    public void receive(String file, byte[] data){
        application.receiveMessage(file, data);
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

    public void receiveBroadCast(InetAddress address, int port, String message){
        application.receiveBroadcast(address, port, message);
    }





}
