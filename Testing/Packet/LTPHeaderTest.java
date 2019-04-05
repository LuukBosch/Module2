package Packet;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LTPHeaderTest{
    LTPHeader testheader;

    @Before
    public void setUp(){
       testheader = new LTPHeader();
    }
    @Test
    public void testFlags() {

        testheader.setFinFlag();
        assertEquals(4, testheader.getFlags().toByteArray()[0]);
        testheader.setAckFlag();
        assertEquals(6, testheader.getFlags().toByteArray()[0]);
        testheader.setSynFlag();
        assertEquals(7, testheader.getFlags().toByteArray()[0]);

    }

    @Test
    public void setAckNum() {
        testheader.setAcknum(500);
        assertEquals(500, testheader.getAckNum());
    }

    @Test
    public void setSeqNum() {
        testheader.setSeqNum(500);
        assertEquals(500, testheader.getSeqNum());
    }


}