package Packet;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PacketTest {
    byte[] byteheader;
    Packet packet;
    Packet test;
    @Before
    public void Setup(){
        byteheader = new byte[11];
        byteheader[3] = 100;
        byteheader[7] = 100;
        byteheader[8] = 3;
        byteheader[9] = 15;
        byteheader[10] = 15;
        packet = new Packet(byteheader);
        test = new Packet();

    }

    @Test
    public void addData() {
        test.addData("hallo".getBytes());
        test.getPacket();
    }

    @Test
    public void getHeader() {
    }

    @Test
    public void getPacket() {
        System.out.println(Arrays.toString(packet.getHeader().getHeader()));
        assertArrayEquals(packet.getPacket(), byteheader);
    }

    @Test
    public void insertChecksum() {
    }
}