package LTP;

import Packet.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.CheckedInputStream;

public class PacketBuffer {


    private LTP ltp;
    private Queue<Packet> buffer = new LinkedList<Packet>();
    private int packetSize = 1024;
    private int bufferSize = 20;
    private HashMap<String, BufferedInputStream> files = new HashMap<>();
    private HashMap<String, BufferedInputStream> pausedStreams = new HashMap<>();
    private HashMap<String, CheckedInputStream> checkedinputStreams = new HashMap<>();
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

    public void addFilestoSend(String file, BufferedInputStream stream, CheckedInputStream check){
        files.put(file, stream);
        checkedinputStreams.put(file, check);
        refillQueue();
    }

    public void refillQueue(){
        while(buffer.size() < bufferSize && !files.isEmpty()) {
            if (!files.isEmpty()) {
                for (String file : files.keySet()) {
                    BufferedInputStream bis = files.get(file);
                    byte[] data;
                    try {
                        if (bis.available() > packetSize) {
                            data = new byte[packetSize];
                            lastpacket = false;
                        } else {
                            data = new byte[bis.available()];
                            lastpacket = true;
                            System.out.println("last!");
                        }
                        bis.read(data);
                        String prefix = file + "/";
                        int len_prefix = prefix.getBytes().length;
                        int len_data = data.length;
                        byte[] fullData = new byte[len_prefix + len_data];
                        System.arraycopy(prefix.getBytes(), 0, fullData, 0, len_prefix);
                        System.arraycopy(data, 0, fullData, len_prefix, len_data);
                        Packet packet = new Packet();
                        packet.addData(fullData);
                        buffer.add(packet);
                        if (lastpacket) {
                            Packet last = new Packet();
                            last.addData((file + "/" + "LAST" + "/" + checkedinputStreams.get(file).getChecksum().getValue()).getBytes());//TODO change last can occur in file
                            buffer.add(last);
                            bis.close();
                            checkedinputStreams.get(file).close();
                            files.remove(file);
                            checkedinputStreams.remove(file);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        if (ltp.getSendQueue().size() < 1 && !buffer.isEmpty()) {
            ltp.getSendQueue().add(buffer.remove());
        }

    }

    public void pauseStream(String file){
        System.out.println("paused: " + file);
            pausedStreams.put(file, files.get(file));
            files.remove(file);
    }

    public void resumeStream(String file){
        System.out.println("resumed: " + file);
        files.put(file, pausedStreams.get(file));
        pausedStreams.remove(file);
    }




}