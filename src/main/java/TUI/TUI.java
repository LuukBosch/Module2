package TUI;

import com.nedap.university.ConnectionInfo;
import com.nedap.university.Main;

import java.util.Scanner;

public class TUI extends Thread {
    private ConnectionInfo connectionInfo;
    private Scanner in;
    private Main main;
    private static final int EXIT = 3;

    public TUI(Main main, ConnectionInfo connectionInfo){
        this.main = main;
        this.connectionInfo = connectionInfo;
    }



    public void run() {
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
        System.out.println("▐  Exit..............................3 ▍");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println("");
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

    public void executeChoice(int choice){
        if(choice == 1){
            System.out.println("Available servers are:");
            System.out.println(main.getIoHandler().getInputHandler().getConnectionInfo().getServerName());
        } else if(choice == 2){
            System.out.println("Available files at server are:");
            System.out.println(main.getIoHandler().getInputHandler().getConnectionInfo().getFiles());
        }
    }




}
