package com.nedap.university;

import Data.DataHandler;
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
            byte[] test1 = new byte[4];
            byte[] test2 = new byte[4];
            test1[3] = 8;
            test2[3] = 8;
            String expr = "LDF/jfojfdjofdjjaofdjakfldjafkl jfkdlajfkldajf kldkjlf jjklfd j";
            System.out.println(expr.replace("LDF/", ""));
           System.out.println(Integer.toBinaryString(42328));
           System.out.println(Integer.toBinaryString(-23208));
            byte[] test3 = new byte[2];
            test3[0] = (byte) ((42328 & 0xFF00) >> 8);
            test3[1] = (byte) (42328 & 0xFF);
            System.out.println(Arrays.toString(test3));
            int temp = 0;
            temp += ((test3[0] & 0xFF00)<< 8);
            temp += (test3[1] & 0x00FF);
            System.out.println(temp);
            System.out.println(Integer.toBinaryString(temp));
        }
    }

