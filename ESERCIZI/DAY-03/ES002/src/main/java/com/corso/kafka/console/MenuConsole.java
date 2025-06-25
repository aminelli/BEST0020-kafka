package com.corso.kafka.console;

import java.util.Scanner;

import com.corso.kafka.producers.ProducerFireAndForgetSync;

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
        System.out.println("\n" + "=".repeat(40));
        System.out.println("MENU PRINCIPALE");
        System.out.println("=".repeat(40));
        System.out.println("1. Crea Producers sincrono Acks 0");
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
                new ProducerFireAndForgetSync().sendMessages();
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


}
