package com.nedap.university;

import Data.DataHandler;
import Packet.LTPHeader;
import Packet.Packet;

import java.io.*;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Tryanderror {
    static FileInputStream fin;
    static ArrayList<byte[]> buffer = new ArrayList<>();
    static private Queue<byte[]> TCPqueue = new LinkedList<>();

    public static void main(String args[]) throws IOException {

        Packet ackMessage = new Packet();
        ackMessage.getHeader().setSeqNum(1);
        ackMessage.getHeader().setAckNum(1);
        ackMessage.getHeader().setAckFlag();
        ackMessage.setChecksum();
        System.out.println("kud.mp4/".getBytes().length);


        System.out.println(Integer.toBinaryString(ackMessage.getHeader().getChecksum()));

    }
}


