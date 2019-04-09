package Data;

import java.io.File;

public class DataHandler {
    public static final String PATH = System.getProperty("user.home") + "/files";


    public DataHandler(){

    }

    public static String getFileList(){
        File folder = new File(PATH);
        System.getProperty("user.home");
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

}
