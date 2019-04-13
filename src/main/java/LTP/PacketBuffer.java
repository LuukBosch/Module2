package LTP;

import Packet.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class PacketBuffer {


    private LTP ltp;
    private Queue<Packet> buffer = new LinkedList<Packet>();
    private int packetSize = 1024;
    private int bufferSize = 200;
    private HashMap<String, BufferedInputStream> files = new HashMap<>();
    boolean lastpacket;


    public PacketBuffer(LTP ltp){
        this.ltp = ltp;
    }



    public void addToBuffer(byte[] data){
        System.out.println(new String(data));
        Packet packet = new Packet();
        packet.addData(data);
        buffer.add(packet);


        refillQueue();
    }

    public void addFilestoSend(String file, BufferedInputStream stream){
        files.put(file, stream);
        refillQueue();
    }

    public void refillQueue(){
        while(buffer.size() < bufferSize && !files.isEmpty()){
            for(String file: files.keySet()){
                BufferedInputStream bis = files.get(file);
                byte[] data;
                try {
                    if(bis.available() > packetSize){
                        data = new byte[packetSize];
                    } else{
                        data = new byte[bis.available()];
                        files.remove(file);
                        lastpacket = true;
                    }
                    bis.read(data);
                    String prefix = file +"/";
                    int len_prefix = prefix.getBytes().length;
                    int len_data =  data.length;
                    byte[] fullData = new byte[len_prefix + len_data];
                    System.arraycopy(prefix.getBytes(), 0, fullData, 0, len_prefix);
                    System.arraycopy(data, 0, fullData, len_prefix, len_data);
                    Packet packet = new Packet();
                    packet.addData(fullData);
                    buffer.add(packet);
                    if(lastpacket){
                        Packet last = new Packet();
                        last.addData((file + "/" + "LAST").getBytes());//TODO change last can occur in file
                        buffer.add(last);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        while (ltp.getSendQueue().size() < 10000 && !buffer.isEmpty()) {
            ltp.getSendQueue().add(buffer.remove());
        }

    }




}
