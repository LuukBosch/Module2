package Packet;

import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Packet {
    private LTPHeader header;
    private byte[] data;

    public Packet(){
        header = new LTPHeader();
    }

    public Packet(byte[] packet){
        header = new LTPHeader(Arrays.copyOfRange(packet, 0, 15));
        if(packet.length > LTPHeader.SIZE) {
            data = Arrays.copyOfRange(packet, 15, packet.length);
        }
    }


    public void addData(byte[] data){
        this.data = data;
    }

    public LTPHeader getHeader(){
        return header;
    }

    public byte[] getData(){
        return data;
    }

    public byte[] getPacket(){
        int len_header = header.getHeader().length;
        if(data != null) {
            int len_data = data.length;
            byte[] packet = new byte[len_header + len_data];
            System.arraycopy(header.getHeader(), 0, packet, 0, len_header);
            System.arraycopy(data, 0, packet, len_header, len_data);
            return packet;
        }
        else{
            return header.getHeader();
        }

    }



    public void setChecksum(){ //TODO checksum in getPacket()?
        Checksum checksum = new CRC32();
        // update the current checksum with the specified array of bytes
        checksum.update(getPacket(), 0, getPacket().length);
        // get the current checksum value
        long checksumValue = checksum.getValue();
        header.setChecksum(checksumValue);


    }


}
