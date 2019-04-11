package com.nedap.university;

import Data.DataHandler;
import Packet.LTPHeader;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Tryanderror {

        public static void main(String[] args) {




            long num1 = 3859300516l;
            long num2 = -435666780l;
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(num1));
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(num2));
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(4294967295l));
            System.out.println("CRC32 checksum after mod string is: " + Long.toBinaryString(num2 &  4294967295l));
            System.out.println("CRC32 checksum after mod string is: " + (num2 &  4294967295l));


            long num3 = -1884803971l;
            long num4 = 2410163325l;
            int c = 4;




            System.out.println("CRC32 checksum for input string is: " + Integer.toBinaryString( -1884803971));
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(num3));
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(num4));
            System.out.println("CRC32 checksum for input string is: " + Long.toBinaryString(4294967295l));
            System.out.println("CRC32 checksum after mod string is: " + Long.toBinaryString(num3 &  4294967295l));
            System.out.println("CRC32 checksum after mod string is: " + (num4 &  4294967295l));

            byte[] hello = "hello".getBytes();
            System.out.println(hello.equals("hello"));

        }
    }

