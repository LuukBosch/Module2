package Packet;

public class Packet {
    private LTPHeader header;
    private byte[] data;

    public Packet(){
        header = new LTPHeader();
    }

    public void addData(byte[] data){
        this.data = new byte[data.length];
        this.data = data;
    }

    public LTPHeader getHeader(){
        return header;
    }

    public byte[] getPacket(){
        int len_header = header.getHeader().length;
        if(data != null) {
            int len_data = data.length;
            byte[] packet = new byte[len_header + len_data];
            System.arraycopy(header, 0, packet, 0, len_header);
            System.arraycopy(data, 0, packet, len_header, len_data);
            insertChecksum();
            return packet;
        }
        else{
            return header.getHeader();
        }

    }

    public void insertChecksum(){

    }


}
