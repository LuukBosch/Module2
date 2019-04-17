package application;

import Data.DataHandler;
import nasprotocol.NasProtocolHandler;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;


/**
 * Class containing the logic of a application Using the NasProtocol
 */
public class Application {

    public boolean connected;
    public NasServer connectedNasServer;
    private String name;
    public int port;
    private NasProtocolHandler nasProtocol;
    private DataHandler dataHandler = new DataHandler();
    private ArrayList<NasServer> availableServers = new ArrayList<>();
    private HashMap<String, BufferedOutputStream> readers = new HashMap<>();
    private HashMap<String, CheckedOutputStream> checkedOutputStreams = new HashMap<>();
    private HashMap<String, Long> startTimeDownload = new HashMap<>();


    /**
     * @param name Name the application uses for broadcasting
     * @param port Port number the application uses for sending/receiving
     */
    public Application(String name, int port) {
        this.port = port;
        this.name = name;
        nasProtocol = new NasProtocolHandler(this, name, port);
        sendBroadcast(port, "hello");
        //sendBroadcast(port, "LDP/" + name + "/" + dataHandler.getFileList());
    }


    /**
     *Sends a Request for connection to a NasServer
     * @param address Address of the Server
     * @param port  Port on which the Server is listening
     */
    public void connect(InetAddress address, int port) {
        nasProtocol.setupConnection(address, port);
    }

    /**
     * Sends a Get request to the server the application is connected to.
     * @param file Name of the file to request
     */
    public void getFile(String file) {
        if (connected) {
            createOutputStream(file);
            nasProtocol.getRequest(file);
        } else{
            System.out.println("Not connected!");
        }
    }

    /**
     * Creates a CheckedoutputStream and wraps it using BufferedOutputStream
     * @param file File name
     */
    private void createOutputStream(String file) {
        try {
            CheckedOutputStream fout = new CheckedOutputStream(new FileOutputStream(System.getProperty("user.home") + "/files/" + file), new CRC32());
            checkedOutputStreams.put(file, fout);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            readers.put(file, bout);
            startTimeDownload.put(file, System.currentTimeMillis());
        } catch (FileNotFoundException e) {
            System.out.println("Can't create a outputStream for:  " + System.getProperty("user.home") + "/files/" + file);
        }

    }


    /**
     * Sends a Post request to the Connected Server
     * @param file file name
     */
    public void postFile(String file) {
        if(connected) {
            try {
                CheckedInputStream check = new CheckedInputStream(new FileInputStream(System.getProperty("user.home") + "/files/" + file), new CRC32());
                BufferedInputStream bis = new BufferedInputStream(check);
                nasProtocol.postFile(file, bis, check);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Not Connected");
        }

    }

    /**
     * Pauses the download of a certain file
     * @param file file name
     */
    public void pauseDownload(String file) {
        nasProtocol.pause(file);

    }

    /**
     * Resumes the download of a certain file
     * @param file file name
     */
    public void resumeDownload(String file) {
        nasProtocol.resume(file);
    }

    /**
     * Handles the Get request send by the connected NasServer
     * @param getRequest file name of the requested file
     */
    public void receiveGet(String getRequest) {

        if (dataHandler.hasFile(getRequest)) {
            try {
                CheckedInputStream check = new CheckedInputStream(new FileInputStream(dataHandler.getFile(getRequest).getAbsolutePath()), new CRC32());
                BufferedInputStream bis = new BufferedInputStream(check);
                nasProtocol.send(getRequest, bis, check);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     *Call CreateOutputStream when a post request is received
     * @param postRequest file name
     */
    public void receivePost(String postRequest) {
        createOutputStream(postRequest);
    }

    public void connected(InetAddress address, int port) {
        connected = true;
        connectedNasServer = getServer(address, port);
        System.out.println("Connected!");
    }


    /**
     * writes the incoming data using the correct bufferedOutputstream and closes the outputStream if final message is received.
     * @param file
     * @param data
     */
    public void receiveData(String file, byte[] data) {
        try {
            if ("LAST/".equals(new String(Arrays.copyOfRange(data, 0, 5)))) {
                System.out.println(file + "     is received!");
                System.out.println("received Checksum is:   " + new String(data).split("/")[1]);
                System.out.println("calculated checksum is:    " + checkedOutputStreams.get(file).getChecksum().getValue());
                if (Arrays.equals(Arrays.copyOfRange(data, 5, 15), ("" + checkedOutputStreams.get(file).getChecksum().getValue()).getBytes())) {
                    System.out.println("Correct File!!!");
                }
                System.out.println("Download Speed is:   " + calculateDownloadTime(file) + " MB/s");
                readers.get(file).close();
                checkedOutputStreams.get(file).close();
            } else {
                readers.get(file).write(data);
            }
        } catch (IOException e) {
            System.out.println("failed to write data for file:  " + file);
        }

    }


    /**
     * @return available Servers
     */
    public ArrayList<NasServer> getAvailableServers() {
        return availableServers;
    }


    /**
     *
     * Sends broadcast to a specific port with a specific message
     * @param port
     * @param message
     */
    public void sendBroadcast(int port, String message) {
        System.out.println("Broadcast send:  " + message);
        nasProtocol.sendBroadCast(port, message);
    }

    /**
     * Handles incomming broadcast messages
     * @param address address of incoming message
     * @param port
     * @param message broadcast message
     */
    public void receiveBroadcast(InetAddress address, int port, byte[] message) {
        String[] stringMessage = new String(message).split("/");
        if (stringMessage[0].equals("hello")) {
            System.out.println("Hello Broadcast received!");
            sendBroadcast(port, "LDP/" + name + "/" + dataHandler.getFileList());
        } else if (stringMessage[0].equals("LDP")) {
            if (stringMessage.length == 3) {
                NasServer nasServer = new NasServer(address, port, stringMessage[1], stringMessage[2]);
                availableServers.add(nasServer);
            }
        }
    }


    /**
     * Returns NasServer if server is known.
     * @param address
     * @param port
     * @return
     */
    public NasServer getServer(InetAddress address, int port) {
        for (NasServer server : availableServers) {
            if (server.getAddress().equals(address) && server.getPort() == port) {
                return server;
            }
        }
        return null;
    }

    /**
     * Calculates download speeds for the download process of a certain file
     * @param file
     * @return
     */
    private double calculateDownloadTime(String file) {
        double start = (double) startTimeDownload.get(file);
        double end = (double) System.currentTimeMillis();
        double size = new File(System.getProperty("user.home") + "/files/" + file).length();
        System.out.println("size:  " + size);
        return ((size / 1000) / (end - start));

    }

    public void error(String string) {
        System.out.println(string);
    }


    /**
     * Handles disconnect, closing all the readers
     */
    public void disconnect() {
        readers.forEach((k, v) -> {
            try {
                v.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        checkedOutputStreams.forEach((k, v) -> {
            try {
                v.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        startTimeDownload.clear();
        nasProtocol.disconnect();
    }

    public void exit(){
        nasProtocol.exit();
    }
}
