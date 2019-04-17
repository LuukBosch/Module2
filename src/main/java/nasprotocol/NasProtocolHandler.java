package nasprotocol;

import LTP.LTP;
import application.Application;

import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.zip.CheckedInputStream;

/**
 * Class Directing all the NasProtocol Messages received by the LTP layer and Send by the Application layer.
 */
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

    public void postFile(String file, BufferedInputStream reader, CheckedInputStream check) {
        ltp.getPacketBuffer().addToBuffer(("POST/" + file).getBytes());
        ltp.getPacketBuffer().addFilestoSend(file, reader, check);
    }


    public void pause(String file){
        ltp.getPacketBuffer().addToBuffer(("PAUSE/" + file).getBytes());
        ltp.getPacketBuffer().pauseStream(file);
    }


    public void resume(String file){
        ltp.getPacketBuffer().addToBuffer(("RESUME/" + file).getBytes());
        //ltp.getPacketBuffer().resumeStream(file);
    }

    public void send(String file, BufferedInputStream reader, CheckedInputStream check){
        ltp.getPacketBuffer().addFilestoSend(file, reader, check);
    }

    public void receive(byte[] data){
        String[] splitMessage = (new String(data).split("/", 2));  //TODO split byte array on /?
        if("GET/".equals(new String(Arrays.copyOfRange(data, 0, 4)))){
            System.out.println("get request received");
            application.receiveGet(splitMessage[1]);
        } else if("POST/".equals(new String(Arrays.copyOfRange(data, 0, 5)))){
            application.receivePost(splitMessage[1]);

        } else if("PAUSE/".equals(new String(Arrays.copyOfRange(data, 0, 6)))) {
            System.out.println("Pause received!");
            ltp.getPacketBuffer().pauseStream(splitMessage[1]);
        } else if("RESUME/".equals(new String(Arrays.copyOfRange(data, 0, 7)))){
            System.out.println("Resume received!");
            ltp.getPacketBuffer().resumeStream(splitMessage[1]);
        }
        else{
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
        System.out.println("Broadcast send ltp  " + message);
        ltp.sendBroadCast(port, message);

    }

    public void receiveBroadCast(InetAddress address, int port, byte[] message){
        application.receiveBroadcast(address, port, message);
    }

    public void disconnect(){
        ltp.disconnect();
    }

    public void error(String string){
        application.error(string);
    }

    public void start(){
        //ltp.start();
    }



}
