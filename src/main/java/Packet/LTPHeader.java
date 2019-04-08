package Packet;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class LTPHeader {
    public static final int SIZE = 9;
    private static final int NUMS_BITS = 32;
    private byte[] header = new byte[9];
    private int seqNum;
    private int ackNum;
    private BitSet flags;

    public LTPHeader() {
        flags = new BitSet(3);
        this.seqNum = (int) (Math.random() * (Math.pow(2, NUMS_BITS) + 1));
        this.ackNum = 0;
    }

    public LTPHeader(byte[] header){
        setSeqNum(Arrays.copyOfRange(header, 0, 4));
        setAckNum(Arrays.copyOfRange(header, 4, 8));
        setFlags(header[8]);
    }

    //---------------------Setters--------------------------------


    public void setChecksum(){

    }

    public void setFlag(int flag) {
        flags.set(flag);
        header[8] = flags.toByteArray()[0];
    }

    public void setFlags(byte flags){
        header[8] = flags;
    }

    public void setFinFlag() {
        setFlag(2);
    }

    public void setAckFlag() {
        setFlag(1);
    }

    public void setSynFlag() {
        setFlag(0);
    }
    //TODO largest int number wrap around fix.
    //TODO maybe add incr function.
    public void setAckNum(int ackNum) {
        this.ackNum = ackNum;
        header[4] = (byte) ((this.ackNum & -16777216) >> 24);
        header[5] = (byte) ((this.ackNum & 16711680) >> 16);
        header[6] = (byte) ((this.ackNum & 65280) >> 8);
        header[7] = (byte) (this.ackNum & 255);
        //ByteBuffer.allocate(4).putInt(value).array();
    }

    public void setAckNum(byte[] ackNum){
        this.ackNum = ByteBuffer.wrap(ackNum).getInt();
        setAckNum(this.ackNum);

    }
    public void setSeqNum(byte[] seqNum){
        this.seqNum = ByteBuffer.wrap(seqNum).getInt();
        setSeqNum(this.seqNum);
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
        header[0] = (byte) ((this.seqNum & -16777216) >> 24);
        header[1] = (byte) ((this.seqNum & 16711680) >> 16);
        header[2] = (byte) ((this.seqNum & 65280) >> 8);
        header[3] = (byte) (this.seqNum & 255);
    }


    //---------------------Getters--------------------------------
    public BitSet getFlags() {
        return flags;
    }

    public byte[] getHeader() {
        return header;
    }
    public int getAckNum(){
        return ackNum;
    }

    public int getSeqNum(){
        return seqNum;
    }

    public boolean getSynFlag(){
        return flags.get(0);

    }

    public boolean getAckFlag(){
        return flags.get(1);

    }

    public boolean getFinFlag(){
        return flags.get(2);
    }




}
