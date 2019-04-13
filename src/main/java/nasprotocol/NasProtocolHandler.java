package nasprotocol;

import LTP.LTP;
import application.Application;

import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.util.Arrays;

public class NasProtocolHandler {
    private LTP ltp;
    Application application;

    public NasProtocolHandler(Application application, String name, int port){
        this.application = application;
        ltp = new LTP(this, name, port);
    }

    public void getRequest(String file) {
        System.out.println("added to buffer");
        ltp.getPacketBuffer().addToBuffer(("GET/" + file).getBytes());
    }



    public void PostFile(String file) {
        //ltp.send("POST/" + file);
    }


    public void Pauze(String file){
        //ltp.send("PAUZE/" + file);
    }


    public void Resume(String file){
        //ltp.send("RESUME/" + file);
    }

    public void send(BufferedInputStream reader, String file){
        ltp.getPacketBuffer().addFilestoSend(file, reader);
    }

    public void receive(byte[] data){
        String[] splitMessage = (new String(data).split("/", 2));  //TODO split byte array on /?
        if("GET/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            System.out.println("get request received");
            application.receiveGet(splitMessage[1]);
        } else if("POST/".equals(new String(Arrays.copyOfRange(data, 0, 5)))){
            application.receivePost(splitMessage[1]);

        } else{
            application.receiveData(splitMessage[0], Arrays.copyOfRange(data, splitMessage[0].getBytes().length+1, data.length));
        }
    }

    public void setupConnection(InetAddress address, int port){
        ltp.startConnection(address, port);
    }

    public void connected(InetAddress address, int port){
        application.connected(address, port);
    }

    public void sendBroadCast(int port, String message){
        ltp.sendBroadCast(port, message);
    }

    public void receiveBroadCast(InetAddress address, int port, byte[] message){
        application.receiveBroadcast(address, port, message);
    }





}
