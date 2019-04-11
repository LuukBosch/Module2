package application;

import java.net.InetAddress;
import java.util.ArrayList;

public class NasServer {

    private InetAddress address;
    private int port;
    private String name;
    private ArrayList<String> files= new ArrayList<>();

    public NasServer(InetAddress address, int port, String name, String files){
        this.name = name;
        this.address = address;
        this.port = port;
        setFiles(files);
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setFiles(String files) {
        String[] filesarray = files.split(" ");
        for(String file: filesarray){
            this.files.add(file);
        }
    }

    public ArrayList<String> getFiles() {
        return files;
    }
}
