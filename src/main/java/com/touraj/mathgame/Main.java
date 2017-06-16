package com.touraj.mathgame;

import com.touraj.mathgame.clientserver.Client;
import com.touraj.mathgame.clientserver.Server;
import com.touraj.mathgame.common.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Created by toraj on 06/12/2017.
 */
public class Main {

    public static void main(String[] args) {

        Configuration.showWelcomeMessage();
        Configuration.buildMenu();

        List<String> validOptions = Arrays.asList("1", "2", "3");
        boolean isAIMode;

        while (true) {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            Scanner scanner = new Scanner(System.in);

            System.out.print("Select an option:");
            String option = scanner.nextLine();

            if (!validOptions.contains(option)) {
                System.out.println(Configuration.getInvalidOptionMessage());
                continue;
            }

            if (option.equals("1")) {

                isAIMode = askForGameMode(scanner);

                try {
                    Server server = new Server(Configuration.getPort(), countDownLatch, isAIMode);
                    server.start();

                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Back to Main Menu.");
                    Configuration.buildMenu();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } else if (option.equals("2")) {
                isAIMode = askForGameMode(scanner);

                try {
                    Scanner scan = new Scanner(System.in);

                    System.out.print("Please input hostname or IP of the Server (or type localhost):");
                    String hostNameOrIP = scan.nextLine();

                    Client client = new Client(Configuration.getPort(), hostNameOrIP, countDownLatch, isAIMode);
                    client.start();

                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Back to Main Menu.");
                    Configuration.buildMenu();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } else if (option.equals("3")) {
                System.out.println("You terminated Application");
                break;
            }
        }
    }

    private static boolean askForGameMode(Scanner scanner) {
        boolean isAIMode;
        while (true) {
            List<String> validModeOptions = Arrays.asList("1", "2");

            Configuration.showGameMode();

            System.out.print("Select Game Mode:");
            String modeOption = scanner.nextLine();

            if (!validModeOptions.contains(modeOption)) {
                System.out.println(Configuration.getInvalidOptionMessage());
                continue;
            } else {
                int mode = Integer.parseInt(modeOption);
                isAIMode = mode != 1;
                break;
            }
        }

        if (isAIMode) {
            System.out.println("AI mode is selected.");
        }
        return isAIMode;
    }
}