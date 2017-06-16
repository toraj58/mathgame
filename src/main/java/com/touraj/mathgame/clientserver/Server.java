package com.touraj.mathgame.clientserver;

import com.touraj.mathgame.game.Game;
import com.touraj.mathgame.game.GameState;
import com.touraj.mathgame.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Created by toraj on 06/12/2017.
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    private CountDownLatch countDownLatch;
    boolean isAIMode;

    public Server(int port, CountDownLatch countDownLatch, boolean isAIMode) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.countDownLatch = countDownLatch;
        this.isAIMode = isAIMode;
    }

    public void run() {
        while (true) {
            try {

                Scanner scanner = new Scanner(System.in);

                System.out.print("Enter your name:");
                String serverPlayerName = scanner.nextLine();

                System.out.println("Your Selected Name:" + serverPlayerName);

                Player serverPlayer = new Player(serverPlayerName, 1, true);

                System.out.println("Waiting for another player to connect on port : " +
                        serverSocket.getLocalPort() + "...");
                Socket client = serverSocket.accept();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String dataReceived = in.readLine();
                String oppositePlayerName = null;

                if (dataReceived.contains(":")) {
                    String[] schemaAndPayload = dataReceived.split(":");

                    if (schemaAndPayload[0].equals("Player")) {
                        oppositePlayerName = schemaAndPayload[1];
                        System.out.printf("Player: [%s] Connected.\n", oppositePlayerName);
                    }
                }

                //[Touraj] Sending Server player Name to Client
                out.println("Player:" + serverPlayerName);

                Player oppositePlayer = new Player(oppositePlayerName, 2, false);
                Game game = new Game();

                List<Player> players = new ArrayList<>(2);

                players.add(serverPlayer);
                players.add(oppositePlayer);

                game.setPlayers(players);

                //[Touraj] New Game
                GameState newGameState = GameState.newGame;
                game.setGameState(newGameState);

                game.setAIMode(isAIMode);

                game.play(in, out, client, true);

                //[Touraj] Thread Synchronization :: Counting Down Latch
                countDownLatch.countDown();
                break;

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        //[Touraj] Closing Server Socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}