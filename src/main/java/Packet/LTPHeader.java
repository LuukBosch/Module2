package Packet;


import java.util.BitSet;

public class LTPHeader {

    private static final int NUMS_BITS = 32;
    private byte[] header = new byte[9];
    private long seqNum;
    private long ackNum;
    private BitSet flags;

    public LTPHeader() {
        flags = new BitSet(3);
        this.seqNum = (long) (Math.random() * (Math.pow(2, NUMS_BITS) + 1));
        this.ackNum = 0;
    }

    public void setFinFlag() {
        flags.set(2);
    }

    public void setAckFlag() {
        flags.set(1);
    }

    public void setSynFlag() {
        flags.set(0);
    }

    public void setAckNum(int ackNum) {
        this.ackNum = ackNum;
    }

    public long getAckNum(){
        return ackNum;
    }

    public long getSeqNum(){
        return seqNum;
    }
    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public BitSet getFlags() {
        return flags;
    }

    public byte[] getHeader() {
        setSeq();
        setAck();
        setFlags();
        return header;
    }

    public void setAck() {
        header[4] = (byte) ((this.ackNum & -16777216) >> 24);
        header[5] = (byte) ((this.ackNum & 16711680) >> 16);
        header[6] = (byte) ((this.ackNum & 65280) >> 8);
        header[7] = (byte) (this.ackNum & 255);
    }

    public void setSeq() {
        header[0] = (byte) ((this.seqNum & -16777216) >> 24);
        header[1] = (byte) ((this.seqNum & 16711680) >> 16);
        header[2] = (byte) ((this.seqNum & 65280) >> 8);
        header[3] = (byte) (this.seqNum & 255);
    }

    public void setChecksum(){

    }

    public void setFlags() {
        header[8] = flags.toByteArray()[0];
    }


}
