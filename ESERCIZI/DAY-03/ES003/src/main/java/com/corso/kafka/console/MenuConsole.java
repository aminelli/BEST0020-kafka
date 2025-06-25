package com.corso.kafka.console;

import java.io.IOException;
import java.util.Scanner;

import com.corso.kafka.consumers.ConsumerKafka;

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
        System.out.println("1. Consumer FIRE_AND_FORGET");
        System.out.println("2. Consumer ACK_1");
        System.out.println("3. Consumer ACK_ALL");
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
                new ConsumerKafka().receiveMessages( "FIRE_AND_FORGET");
                break;
            case 2:
                new ConsumerKafka().receiveMessages( "ACK_1");
                break;
            case 3:
                new ConsumerKafka().receiveMessages( "ACK_ALL");
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
