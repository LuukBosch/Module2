package TUI;

import LTP.LTPConnection;
import com.nedap.university.ServerInfo;
import com.nedap.university.Main;

import java.util.Scanner;

public class TUI extends Thread {
    private ServerInfo serverInfo;
    private Scanner in;
    private Main main;
    private static final int EXIT = 4;

    public TUI(Main main, ServerInfo serverInfo){
        this.main = main;
        this.serverInfo = serverInfo;
    }



    public void run() {
        printlogo();
        int choice = -1;
        while (choice != EXIT) {
            displayMenu();
            choice = readIntWithPrompt("Enter choice:");
            executeChoice(choice);
        }
        //TODO main.exit?
    }

    private void displayMenu() {
        System.out.println();
        System.out.println(" Enter the number denoting the action");
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("▐  Available servers.................1 ▍");
        System.out.println("▐  Available files...................2 ▍");
        System.out.println("▐  Get File..........................3 ▍");
        System.out.println("▐  Exit..............................4 ▍");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

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

    public void executeChoice(int choice){
        if(choice == 1){
            System.out.println("Available servers are:");
            System.out.println(main.getIoHandler().getInputHandler().getServerInfo().getServerName());
        } else if(choice == 2){
            System.out.println("Available files at server are:");
            System.out.println(main.getIoHandler().getInputHandler().getServerInfo().getFiles());
        } else if(choice == 3){
            LTPConnection connection = main.getIoHandler().getInputHandler().getLTPHandler().StartNewLTPConnection
                    (main.getIoHandler().getInputHandler().getServerInfo().getAddress(), main.getIoHandler().getInputHandler().getServerInfo().getPort() );
            String input = readStringWithPrompt("What file do you want?");
            connection.getFile(input);
        }
    }



    public void printlogo(){

        System.out.println("-----:||220$00$$211'        '112$$00$0221|:");
        System.out.println("--'00802$22112122$$802'   1080$22211122$2$800:");
        System.out.println("--|&0111111111111111108::881111111111111111$&1");
        System.out.println("---1&$111111112$$211112&&$11112$$2111111112&2");
        System.out.println("----2&$1111111111200228&&&$2$0$111111111128$");
        System.out.println( "-----18821111111111$&&&&&&&&811111111112081");
        System.out.println("------'108$21111120&&&&&&&&&&021111122801:");
        System.out.println( "--------'$&&8$$$8&&8$11||11$8&&80008&&$:");
        System.out.println("-------28$1;:'';0&1:' '''' ':181:::;12882'");
        System.out.println("-----:88;'  ';2&&8;' ''''''  :8&$|'   :2&8:");
        System.out.println("-----$&;  :1$&&&&&&021||||112088&&01:' '$&0");
        System.out.println("-----&8;|28&821;::;12&&&&&81;::::;108$1:1&&:");
        System.out.println("---;0&8&&&&1: '''''' ;0&&0: '''''' '|8&&88&0|");
        System.out.println( "-'081;:$&&1 ''''''''' 1&&1 ''''''''' ;8&|';$&0:");
        System.out.println("-88; ' 1&$''''''''''' 1&&2 '''''''''' 28:'''1&8:");
        System.out.println( "1&1 '' 2&0'''''''''' ;8&&&1'''''''''''0&:''''8&2");
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
        System.out.println( "-------------:128$1;''''':;10&21:");
        System.out.println("-----------------'1$00$$$$08$1'");
        System.out.println("---------------------':;;:'");
    }




}
