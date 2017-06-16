package com.touraj.mathgame.clientserver;

import com.touraj.mathgame.game.Game;
import com.touraj.mathgame.game.GameState;
import com.touraj.mathgame.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Created by toraj on 06/12/2017.
 */
public class Client extends Thread {

    private CountDownLatch countDownLatch;
    int port;
    String host;
    boolean isAIMode;

    public Client(int port, String host, CountDownLatch countDownLatch, boolean isAIMode) throws IOException {
        this.port = port;
        this.host = host;
        this.countDownLatch = countDownLatch;
        this.isAIMode = isAIMode;
    }

    public void run() {

        Socket client = null;

        while (true) {
            try {

                Scanner scanner = new Scanner(System.in);

                System.out.print("Enter your name:");
                String clientPlayerName = scanner.nextLine();

                System.out.println("Your Selected Name:" + clientPlayerName);

                Player clientPlayer = new Player(clientPlayerName, 2, false);

                //[Touraj] Connect to Server
                client = new Socket(host, port);

                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                //[Touraj] : Sending Player Name to Server
                out.println("Player:" + clientPlayerName);

                String dataReceived = in.readLine();
                String serverPlayerName = null;

                if (dataReceived.contains(":")) {
                    String[] schemaAndPayload = dataReceived.split(":");

                    if (schemaAndPayload[0].equals("Player")) {
                        serverPlayerName = schemaAndPayload[1];
                        System.out.printf("Player: [%s] is Server Player.\n", serverPlayerName);
                    }
                }

                Player serverPlayer = new Player(serverPlayerName, 1, true);
                Game game = new Game();

                List<Player> players = new ArrayList<>(2);

                players.add(serverPlayer);
                players.add(clientPlayer);

                game.setPlayers(players);

                //[Touraj] New Game
                GameState newGameState = GameState.newGame;
                game.setGameState(newGameState);

                game.setAIMode(isAIMode);

                game.play(in, out, client, false);

                //[Touraj] Thread Synchronization :: Counting Down Latch
                countDownLatch.countDown();
                break;

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (UnknownHostException u) {
                System.out.println("Unknown Host:" + host);
                break;
            } catch (ConnectException c) {
                System.out.println("Can not Connect to:" + host);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        //[Touraj] Closing Server Socket
        try {
            if (client != null) {
                client.close();
            } else {
                countDownLatch.countDown();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}