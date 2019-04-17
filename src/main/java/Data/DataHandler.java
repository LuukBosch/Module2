package Data;

import com.nedap.university.config;

import java.io.File;
import java.util.ArrayList;


public class DataHandler {

    private ArrayList<String> files = new ArrayList<>();
    private File[] listOfFiles;


    public DataHandler(){
        File folder = new File(config.PATH);
        listOfFiles = folder.listFiles();
        for(File file: listOfFiles){
            files.add(file.getName());
        }
    }

    public static String getFileList(){
        File folder = new File(config.PATH);
        File[] listOfFiles = folder.listFiles();
        String files = "";
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if( i == 0){
                    files += listOfFiles[i].getName();
                }
                else if(i > 0) {
                    files += " " + listOfFiles[i].getName();
                }
            }
        }
        return files;
    }

    public File getFile(String file){
        for(File data: listOfFiles){
            if(data.getName().equals(file)){
                return data;
            }

        }
        return null;
    }
    public boolean hasFile(String file){
        return files.contains(file);
    }
}
