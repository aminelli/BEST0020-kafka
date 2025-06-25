package com.corso.kafka.console;

import java.io.IOException;
import java.util.Scanner;

import com.corso.kafka.producers.ProducerSyncAckAll;
import com.corso.kafka.producers.ProducerSyncAckOne;
import com.corso.kafka.producers.ProducerSyncFireAndForget;

public class MenuConsole {
    
    private Scanner scanner;

    public MenuConsole() {
        scanner = new Scanner(System.in);
    }


    public void showMenu() {

        boolean displayMenu = true;

        while (displayMenu) {
            drawMenu();
            int menuIndex = loadMenu();
            displayMenu = executeAction(menuIndex);
        }

        System.out.print("Ciao Torna Presto... ");
        scanner.close();
    }

    private void drawMenu() {
        clearConsole();
        System.out.println("\n" + "=".repeat(40));
        System.out.println("MENU PRINCIPALE");
        System.out.println("=".repeat(40));
        System.out.println("1. Producers sincrono Acks 0");
        System.out.println("2. Producers sincrono Acks 1");
        System.out.println("3. Producers sincrono Acks All");
        System.out.println("0. Esci");
        System.out.println("=".repeat(40));
        System.out.print("Scegli la voce di menu: ");
    }

    private int loadMenu() {
        try {
            int menuIndex = scanner.nextInt();
            scanner.nextLine();
            return menuIndex;
        } catch (Exception ex) {
            scanner.nextLine();
            return -1;
        }
    }


    private boolean executeAction(int menuIndex) {

        switch (menuIndex) {
            case 1:
                new ProducerSyncFireAndForget().sendMessages( "FIRE_AND_FORGET", 5000);
                break;
            case 2:
                new ProducerSyncAckOne().sendMessages( "ACK_1", 5000);
                break;
            case 3:
                new ProducerSyncAckAll().sendMessages( "ACK_ALL", 5000);
                break;
            case 0:
                return false;
            default:
                System.out.println("Scelta non valida !");
        }

        System.out.println("Premere invio per continuare");
        scanner.nextLine();
        return true;
    }    


        // Metodo per pulire la console - funziona su Windows, Linux e Mac
    private void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux/Mac/Unix
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            // Se il comando non funziona, usa il metodo alternativo
            clearConsoleAlternative();
        }
    }

        // Metodo alternativo per pulire la console (stampa righe vuote)
    private void clearConsoleAlternative() {
        // Stampa 50 righe vuote per simulare la pulizia
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}
