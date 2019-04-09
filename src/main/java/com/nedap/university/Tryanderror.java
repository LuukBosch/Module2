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
           System.out.println(Arrays.equals(test1,test2));
           System.out.println(Arrays.toString(test1));
            System.out.println(DataHandler.PATH);
           System.out.println(DataHandler.getFileList());
           System.out.println(DataHandler.getFileList().split(" ")[1]);


        }
    }

