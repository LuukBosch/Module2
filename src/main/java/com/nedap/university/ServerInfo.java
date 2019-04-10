package com.nedap.university;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerInfo {
    private String serverName;
    private ArrayList<String> files;
    private InetAddress address;
    private int port;

    public ServerInfo(){
        files = new ArrayList<>();
    }


    public String getServerName(){
        return serverName;
    }

    public ArrayList getFiles(){
        return files;
    }

    public void addFile(String file){
        String[] data = file.split(" ");
        for(int i = 0; i < data.length; i++){
            files.add(data[i]);
        }
    }

    public void setServerName(String name){
        serverName = name;

    }

    public void setPort(int port){
        this.port = port;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }

    public InetAddress getAddress(){
        return address;
    }

    public int getPort(){
        return port;
    }
}
