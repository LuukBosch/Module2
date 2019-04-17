package TUI;

import application.Application;
import application.NasServer;

import java.util.Scanner;

public class TUI extends Thread {
    private Application application;
    private static int EXIT = 10;


    public TUI(Application application) {
        this.application = application;
    }

    public void run() {
        printlogo();
        int choice = -1;
        while (choice != EXIT) {
            displayMenu();
            choice = readIntWithPrompt("Enter choice:");
            executeChoice(choice);
        }
    }

    private void displayMenu() {
        System.out.println();
        System.out.println(" Enter the number denoting the action");
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("▐  Available servers.................1 ▍");
        System.out.println("▐  Available files...................2 ▍");
        System.out.println("▐  Start connection..................3 ▍");
        System.out.println("▐  Get File..........................4 ▍");
        System.out.println("▐  Post file.........................5 ▍");
        System.out.println("▐  Pause download....................6 ▍");
        System.out.println("▐  Resume download...................7 ▍");
        System.out.println("▐  Disconnect........................8 ▍");
        System.out.println("▐  SendBroadCast.....................9 ▍");
        System.out.println("▐  Exit..............................10 ▍");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void executeChoice(int choice) {
            if (choice == 1) {
                displayServers();
            } else if (choice == 2) {
                displayfiles();
            } else if (choice == 3) {
                String server = readStringWithPrompt("To which server do you want to connect?");
                startconnection(server);
            }else if (choice == 4) {
                String file = readStringWithPrompt("what file do you want?");
                application.getFile(file);
            } else if (choice == 5) {
                String file = readStringWithPrompt("what file do you want to post?");
                application.postFile(file);
            } else if (choice == 6) {
                String file = readStringWithPrompt("which download do you want to pause?");
                application.pauseDownload(file);
            } else if (choice == 7) {
                String file = readStringWithPrompt("which download do you want to resume?");
                application.resumeDownload(file);
            } else if (choice == 8) {
                application.disconnect();
                application.connected = false;
            } else if (choice ==9) {
                application.sendBroadcast(application.port, "hello");
            } else if (choice ==10) {
                application.exit();
            } else{
                System.out.println("Invalid Choice!");
        }

    }

    public void displayServers() {
        int count = 1;
        if (!application.getAvailableServers().isEmpty()) {
            for (NasServer server : application.getAvailableServers()) {
                System.out.print(count + ".   " + server.getName());
                count++;
            }
        }
    }

    public void displayfiles() {
        int count = 1;
        if (!application.getAvailableServers().isEmpty()) {
            for (NasServer server : application.getAvailableServers()) {
                System.out.println("Server is: " + server.getName());
                if (!server.getFiles().isEmpty()) {
                    for (String file : server.getFiles()) {
                        System.out.println(count + ".   " + file);
                        count++;
                    }
                }
            }

        }
    }

    public void startconnection(String file) {
        boolean serverIsAvailable = false;
        for(NasServer server :application.getAvailableServers()){
            if(server.getName().equals(file)){
                application.connect(server.getAddress(), server.getPort());
                serverIsAvailable = true;
                break;
            }
        }
        if(!serverIsAvailable){
            System.out.println("Did not found this server!");
        }


    }

    public void printlogo() {
        System.out.println("-----:||220$00$$211'        '112$$00$0221|:");
        System.out.println("--'00802$22112122$$802'   1080$22211122$2$800:");
        System.out.println("--|&0111111111111111108::881111111111111111$&1");
        System.out.println("---1&$111111112$$211112&&$11112$$2111111112&2");
        System.out.println("----2&$1111111111200228&&&$2$0$111111111128$");
        System.out.println("-----18821111111111$&&&&&&&&811111111112081");
        System.out.println("------'108$21111120&&&&&&&&&&021111122801:");
        System.out.println("--------'$&&8$$$8&&8$11||11$8&&80008&&$:");
        System.out.println("-------28$1;:'';0&1:' '''' ':181:::;12882'");
        System.out.println("-----:88;'  ';2&&8;' ''''''  :8&$|'   :2&8:");
        System.out.println("-----$&;  :1$&&&&&&021||||112088&&01:' '$&0");
        System.out.println("-----&8;|28&821;::;12&&&&&81;::::;108$1:1&&:");
        System.out.println("---;0&8&&&&1: '''''' ;0&&0: '''''' '|8&&88&0|");
        System.out.println("-'081;:$&&1 ''''''''' 1&&1 ''''''''' ;8&|';$&0:");
        System.out.println("-88; ' 1&$''''''''''' 1&&2 '''''''''' 28:'''1&8:");
        System.out.println("1&1 '' 2&0'''''''''' ;8&&&1'''''''''''0&:''''8&2");
        System.out.println("1&1 '':8&&2: '''' ':1&&&&&&$|''    ':2&&1 '':8&2");
        System.out.println("'88;  2&&&&821||11$0$1|;;;1208$1112$&&&&&; '2&&:");
        System.out.println("-:0822&0008&&&&&&&2:' ''''' '|8&&&&&821|1020&0:");
        System.out.println("---8&&1''':|$&&&&2 '''''''''' ;&&&0|: '' |&&8'");
        System.out.println("---;&8:'''''';$&&1 ''''''''''':8&1''''''':8&|");
        System.out.println("----$&1 '''''''2&8; '''''''' '2&| '''''' |&0");
        System.out.println("----'0&1''''''':8&&2;:'''':;10&$ '''''''|88:");
        System.out.println("------2&$1:'''';8&&&&&00008&&&&$'  '':|$&2'");
        System.out.println("-------'1$00$$$8&&0211||||112$8&02220001:");
        System.out.println("-----------|28&&&2'   '''''   2&&&8$|");
        System.out.println("-------------:128$1;''''':;10&21:");
        System.out.println("-----------------'1$00$$$$08$1'");
        System.out.println("---------------------':;;:'");
    }


    //String and Int input readers.
    private int readIntWithPrompt(String prompt) {
        int value = 0;
        boolean intRead = false;
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                } else {
                    System.out.println("not a valid input!");
                }
            }

        } while (!intRead);
        return value;
    }

    private String readStringWithPrompt(String prompt) {
        String text = "";
        boolean intRead = false;
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNext()) {
                    intRead = true;
                    text = scannerLine.next();
                } else {
                    System.out.println("not a valid input!");
                }
            }
        } while (!intRead);
        return text;
    }


}
