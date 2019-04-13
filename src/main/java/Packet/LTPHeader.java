package Packet;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class LTPHeader {
    public static final int SIZE =13;
    private byte[] header = new byte[13];
    private int seqNum;
    private int ackNum;
    private int checksum;
    //private int connectionNum;
    private BitSet flags = new BitSet(3);

    public LTPHeader() {
        this.seqNum = 0;
        this.ackNum = 0;
        //this.connectionNum = 0;
    }

    public LTPHeader(byte[] header){
        setSeqNum(Arrays.copyOfRange(header, 0, 4));
        setAckNum(Arrays.copyOfRange(header, 4, 8));
        setFlags(header[8]);
        //setConnectionNum(Arrays.copyOfRange(header, 9, 11));
        setChecksum(Arrays.copyOfRange(header, 9, 13));
    }

    //---------------------Setters--------------------------------

    public void setFlag(int flag) {
        flags.set(flag);
        header[8] = flags.toByteArray()[0];
    }

    public void setFlags(byte flag){
        if((((int)flag >> 0) & 1) == 1){
            flags.set(0);
        }
        if((((int)flag >> 1) & 1) == 1){
            flags.set(1);
        }
        if((((int)flag >> 2) & 1) == 1){
            flags.set(2);
        }

        header[8] = flag;

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

    public void setChecksum(long checksum){
        this.checksum = (int) checksum;
        header[9] = (byte) ((this.checksum & -16777216) >> 24);
        header[10] = (byte) ((this.checksum & 16711680) >> 16);
        header[11] = (byte) ((this.checksum & 65280) >> 8);
        header[12] = (byte) (this.checksum & 255);

    }

    public void setChecksum(byte[] checksum){
        this.checksum = ByteBuffer.wrap(checksum).getInt();
        setChecksum(this.checksum);
    }

    /*
    public void setConnectionNum(int connectionNum){
        this.connectionNum = connectionNum;
        header[9] = (byte) ((this.connectionNum >> 8) & 0xff);
        header[10] = (byte) (this.connectionNum & 0xff);
    }

    public void setConnectionNum(byte[] connectionNum){
        this.connectionNum = ((connectionNum[0] & 0xff) << 8) | (connectionNum[1] & 0xff);
        setConnectionNum(this.connectionNum);
    }
    */
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

    public int getChecksum(){
        return checksum;
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

    /*
    public int getConnectionNum(){
        return connectionNum;
    }
    */







}
