package Packet;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class LTPHeaderTest {
    LTPHeader header;
    LTPHeader header2;

    @Before
    public void Setup(){
        header = new LTPHeader();
        byte[] byteheader = new byte[11];
        byteheader[3] = 100;
        byteheader[7] = 100;
        byteheader[8] = 3;
        byteheader[9] = 15;
        byteheader[10] = 15;
        header2 = new LTPHeader(byteheader);

    }


    @Test
    public void setFinFlag() {
        header.setFinFlag();
        assertTrue(header.getFinFlag());
    }

    @Test
    public void setAckFlag() {
        header.setAckFlag();
        assertTrue(header.getAckFlag());
    }

    @Test
    public void setSynFlag() {
        header.setSynFlag();
        assertTrue(header.getSynFlag());
    }

    @Test
    public void setAckNum() {
        header.setAckNum(100);
        assertEquals(100, header.getAckNum());
    }

    @Test
    public void setAckNum1() {
        byte[] acknum = ByteBuffer.allocate(4).putInt(100).array();
        header.setAckNum(acknum);
        assertEquals(100, header.getAckNum());

    }

    @Test
    public void setSeqNum() {
        header.setSeqNum(100);
        assertEquals(100, header.getSeqNum());
    }

    @Test
    public void setSeqNum1() {
        byte[] seqnum = ByteBuffer.allocate(4).putInt(100).array();
        header.setSeqNum(seqnum);
        assertEquals(100, header.getSeqNum());
    }

    @Test
    public void setConnectionNum(){
        header.setConnectionNum(100);
        assertEquals(100, header.getConnectionNum());
    }
    @Test
    public void setConnectionNum2(){
        byte[] temp = new byte[2];
        temp[1] = 100;
        header.setConnectionNum(temp);
        assertEquals(100, header.getConnectionNum());


    }

    @Test
    public void getFlags() {
        header.setFinFlag();
        header.setAckFlag();
        header.setSynFlag();
        assertEquals(7, header.getFlags().toByteArray()[0]);
    }

    @Test
    public void getHeader() {
        byte[] byteheader = new byte[11];
        byteheader[3] = 100;
        byteheader[7] = 100;
        byteheader[8] = 3;
        byteheader[9] = 15;
        byteheader[10] = 15;
        header.setSynFlag();
        header.setAckFlag();
        header.setAckNum(100);
        header.setSeqNum(100);
        header.setConnectionNum(3855);
        assertArrayEquals(byteheader, header.getHeader());
        assertArrayEquals(byteheader, header2.getHeader());
        assertTrue(header2.getAckFlag());
        assertTrue(header2.getSynFlag());
        assertFalse(header2.getFinFlag());
    }


    @Test
    public void getSynFlag() {
        assertFalse(header.getSynFlag());
        header.setSynFlag();
        assertTrue(header.getSynFlag());
    }

    @Test
    public void getAckFlag() {
        assertFalse(header.getAckFlag());
        header.setAckFlag();
        assertTrue(header.getAckFlag());
    }

    @Test
    public void getFinFlag() {
        assertFalse(header.getFinFlag());
        header.setFinFlag();
        assertTrue(header.getFinFlag());
    }
}