package LTP;

import Packet.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.CheckedInputStream;

/**
 * The PacketBuffer class is used to add Packets to the sendQueue of the LTP layer. This prevents the Opening and closing of a InputStream for every packet that is requested by the IO layer.
 */
public class PacketBuffer {
    private LTP ltp;
    private int packetSize = 60000;
    private int bufferSize = 5;
    private HashMap<String, BufferedInputStream> files = new HashMap<>();
    private HashMap<String, BufferedInputStream> pausedStreams = new HashMap<>();
    private HashMap<String, CheckedInputStream> checkedinputStreams = new HashMap<>();
    boolean lastpacket;


    public PacketBuffer(LTP ltp){
        this.ltp = ltp;
    }


    /**
     * adds packets to the LTP sendqueue. Assumed is that only limited amounts of data are provided.
     * @param data
     */
    public void addToBuffer(byte[] data){
        System.out.println(new String(data));
        Packet packet = new Packet();
        packet.addData(data);
        ltp.getSendQueue().add(packet);
        refillQueue();
    }

    /**
     *
     * @param file
     * @param stream
     * @param check
     */
    public void addFilestoSend(String file, BufferedInputStream stream, CheckedInputStream check){
        files.put(file, stream);
        checkedinputStreams.put(file, check);
        refillQueue();
    }

    /**
     *
     */
    public void refillQueue(){
        ArrayList<String> removed = new ArrayList<>();
        while(ltp.getSendQueue().size() < bufferSize && !files.isEmpty()) {
            if (!files.isEmpty()) {
                for (String file : files.keySet()) {
                    BufferedInputStream bis = files.get(file);
                    byte[] data;
                    try {
                        if (bis.available() > (packetSize - (file.length() + 1))){
                            data = new byte[packetSize - (file.length() + 1)];
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
                        ltp.getSendQueue().add(packet);
                        if (lastpacket) {
                            Packet last = new Packet();
                            last.addData((file + "/" + "LAST" + "/" + checkedinputStreams.get(file).getChecksum().getValue()).getBytes());//TODO change last can occur in file
                            ltp.getSendQueue().add(last);
                            removed.add(file);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            cleanup(removed);
        }

    }

    /**
     * @param file
     */
    public void pauseStream(String file){
        System.out.println("paused: " + file);
            pausedStreams.put(file, files.get(file));
            files.remove(file);

    }

    /**
     * @param file
     */
    public void resumeStream(String file){
        System.out.println("resumed: " + file);
        files.put(file, pausedStreams.get(file));
        pausedStreams.remove(file);
        refillQueue();
    }


    public void clear(){
        for(String file: files.keySet()){
            try {
                files.get(file).close();
                checkedinputStreams.get(file).close();
                if(pausedStreams.containsKey(file)){
                    pausedStreams.get(file).close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        files.clear();
        checkedinputStreams.clear();
    }

    public void cleanup(ArrayList<String> toBeRemoved){
        for(String file: toBeRemoved){
            try {
                System.out.println("to be removed is:" + file);
                System.out.println("key set is: " + files.keySet().toString());
                if(files.containsKey(file)) {
                    files.get(file).close();
                    files.remove(file);
                }
                if(checkedinputStreams.containsKey(file)) {
                    checkedinputStreams.get(file).close();
                    checkedinputStreams.remove(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
