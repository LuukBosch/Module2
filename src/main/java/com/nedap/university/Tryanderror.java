package com.nedap.university;

import Packet.LTPHeader;

import java.util.Arrays;
import java.util.Random;

public class Tryanderror {

        public static void main(String[] args) {

            Random random = new Random();
            byte[] hallo = "Hello".getBytes();
            System.out.println(hallo);
            System.out.println(new String(hallo));
            System.out.println(new String(Arrays.copyOfRange(hallo, 1,4)));
            System.out.println(random.nextInt());

            LTPHeader luuk = new LTPHeader();
            luuk.setAckNum(100);
            System.out.println(luuk.getAckNum());
            Integer test = 1;



        }
    }

