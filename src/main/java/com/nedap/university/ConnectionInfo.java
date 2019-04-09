package com.nedap.university;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectionInfo {
    private boolean isConnected;
    private String serverName;
    private ArrayList<String> files;
    private InetAddress address;

    public ConnectionInfo(){
        isConnected = false;
        serverName = "Computer";
        try {
            InetAddress address = InetAddress.getByName("172.16.1.12");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        files = new ArrayList<>();
    }

    public boolean isConnected(){
        return isConnected;
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

    public void setAddress(InetAddress address){
        this.address = address;
    }
}
