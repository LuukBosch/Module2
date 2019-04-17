package application;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Class containing Information about a Server operating according to the NasProtocol
 */
public class NasServer {

    private InetAddress address;
    private int port;
    private String name;
    private ArrayList<String> files= new ArrayList<>();

    /**
     * @param address Address of the NasServer
     * @param port    Port number on which the server is listening
     * @param name    Name of the Nas server
     * @param files   Files that can be downloaded from the server
     */
    public NasServer(InetAddress address, int port, String name, String files){
        this.name = name;
        this.address = address;
        this.port = port;
        setFiles(files);
    }

    /**
     * @return Address of the NasServer
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return Port number of the NasServer
     */
    public int getPort() {
        return port;
    }

    /**
     * @return Returns the name of the NasServer
     */
    public String getName() {
        return name;
    }

    /**
     * fills the @Files
     *
     *
     * @param files String containing the file names
     */
    private void setFiles(String files) {
        String[] filesarray = files.split(" ");
        for(String file: filesarray){
            this.files.add(file);
        }
    }

    /**
     * @return  List containing all the files of the server
     */
    public ArrayList<String> getFiles() {
        return files;
    }
}
