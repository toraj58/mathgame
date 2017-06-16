package com.touraj.mathgame.game;

import com.touraj.mathgame.common.Commands;
import com.touraj.mathgame.common.Configuration;
import com.touraj.mathgame.common.MathHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by toraj on 06/12/2017.
 */
public class Game {

    GameState gameState;
    List<Player> players = new ArrayList<>(2);
    boolean isAIMode;

    public boolean isAIMode() {
        return isAIMode;
    }

    public void setAIMode(boolean AIMode) {
        isAIMode = AIMode;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void play(BufferedReader in, PrintWriter out, Socket client, boolean isServer) {

        Scanner userInputReader = null;
        boolean isQuited = false;

        if (isServer) {

            if (isAIMode()) {
                //AI mode

                int initialNumbertoPlay = MathHelper.generateRandomNumberInRange(
                        Configuration.getMinNumberForAIMode(),
                        Configuration.getMaxNumberForAIMode());
                System.out.println("AI Selected(as Initial Number):" + initialNumbertoPlay);
                out.println("Number:" + initialNumbertoPlay + ":2"); //2 is poison pill because initial number does not need modifier

            } else // [Touraj] : Player Mode
            {
                while (true) {
                    userInputReader = new Scanner(System.in);
                    System.out.print("Please input initial number to begin the game:");
                    String val = userInputReader.nextLine();

                    if (!MathHelper.isInteger(val)) {
                        System.out.println("Please input whole number greater than 1.");
                        continue;
                    }

                    int initialNumber = Integer.parseInt(val);

                    if (initialNumber < 2) {
                        System.out.println("Please input whole number greater than 1.");
                        continue;
                    }

                    out.println("Number:" + initialNumber + ":2"); //2 is poison pill because initial number does not need modifier
                    break;
                }
            }
        } else //Client Part :: initiate userInputReader
        {
            userInputReader = new Scanner(System.in);
        }

        try {
            while (true) {
                gameState = GameState.inProgress;

                System.out.println("Please wait for opponent to send you number...");
                String dataReceived = in.readLine();

                if (dataReceived.contains(":")) {
                    String[] schemaAndPayload = dataReceived.split(":");
                    String command;
                    String numberStr;
                    String numberModifierStr; // -1,0 or 1

                    if (schemaAndPayload[0].equals("Command")) {
                        //[Touraj] In case of Receiving any Command
                        command = schemaAndPayload[1];
                        if (command.equals(Commands.getQuitCommand())) {
                            System.out.println("Opponent Quited the Game");
                            isQuited = true;
                            break;
                        }
                    }

                    if (schemaAndPayload[0].equals("Number")) {
                        numberStr = schemaAndPayload[1];
                        numberModifierStr = schemaAndPayload[2];

                        int number = Integer.parseInt(numberStr);
                        int numberModifier = Integer.parseInt(numberModifierStr);

                        if (number > 1) {
                            int selectedNumber;
                            if (isAIMode()) {
                                //AI mode
                                if (numberModifier < 2) { // 2 is poision pill
                                    if (isServer) {
                                        System.out.printf("Opponent:%s selected:%d \n", getPlayers().get(1).getName(), numberModifier);
                                    } else {
                                        System.out.printf("Opponent:%s selected:%d \n", getPlayers().get(0).getName(), numberModifier);
                                    }
                                }

                                if (isServer) {
                                    System.out.printf("Opponent:%s Sent you Number:%d \n", getPlayers().get(1).getName(), number);
                                } else {
                                    System.out.printf("Opponent:%s Sent you Number:%d \n", getPlayers().get(0).getName(), number);
                                }

                                System.out.printf("What do you add to %d : to be divisible by three > {-1,0,1} :\n", number);
                                selectedNumber = MathHelper.whatNumberAddToBeDivisibleByThree(number);
                                System.out.println("AI Selected:" + selectedNumber);

                            } else // [Touraj] : Player Mode
                            {
                                while (true) {
                                    //[Touraj] Loop Until user input currect Number
                                    if (numberModifier < 2) { // 2 is poison pill
                                        if (isServer) {
                                            System.out.printf("Opponent:%s selected:%d \n", getPlayers().get(1).getName(), numberModifier);
                                        } else {
                                            System.out.printf("Opponent:%s selected:%d \n", getPlayers().get(0).getName(), numberModifier);
                                        }
                                    }

                                    if (isServer) {
                                        System.out.printf("Opponent:%s Sent you Number:%d \n", getPlayers().get(1).getName(), number);
                                    } else {
                                        System.out.printf("Opponent:%s Sent you Number:%d \n", getPlayers().get(0).getName(), number);
                                    }

                                    System.out.printf("What do you add to %d : to be divisible by three > {-1,0,1} :", number);
                                    String numStr = userInputReader.nextLine();

                                    selectedNumber = Integer.parseInt(numStr);

                                    if (selectedNumber == -1 || selectedNumber == 0 || selectedNumber == 1) {
                                        if ((number + selectedNumber) % 3 == 0) {
                                            break;
                                        } else {
                                            System.out.println("You Selected Wrong Number (Result not devisible by 3); Please Try Again!");
                                        }
                                    } else {
                                        System.out.println("You can only input -1,0 or 1; Please Try Again!");
                                    }
                                }
                            }

                            int numberToSend = (number + selectedNumber) / 3;

                            if (isAIMode()) {
                                //[Touraj] :: in AI mode I simulate Delay like Humans.
                                try {
                                    Thread.sleep(Configuration.getSimulateHumanDelayForAIinMilliSecond());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            out.println("Number:" + numberToSend + ":" + selectedNumber);
                            System.out.printf("You sent number:%d to the opponent.\n", numberToSend);

                            if (numberToSend == 1) {
                                if (isServer) {
                                    //[Touraj] Player 1 Win : server Player
                                    setGameState(GameState.player1Win);
                                    System.out.println(Configuration.getWinMessage());
                                    break;
                                } else {
                                    //[Touraj] Player 2 Win : Client Player
                                    setGameState(GameState.player2Win);
                                    System.out.println(Configuration.getWinMessage());
                                    break;
                                }
                            }
                        } else if (number == 1)//opponent send you 1 and you loose
                        {
                            if (isServer) {
                                //[Touraj] Player 2 Win : Client Player
                                setGameState(GameState.player2Win);
                                System.out.println("Opponent Sent you Number:" + numberStr);
                                System.out.println(Configuration.getGameOverMessage());
                                break;
                            } else {
                                //[Touraj] Player 1 Win : Server Player
                                setGameState(GameState.player1Win);
                                System.out.println("Opponent Sent you Number:" + numberStr);
                                System.out.println(Configuration.getGameOverMessage());
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid data Received:" + dataReceived);
                }
            }

            if (!isQuited) {
                printStatus();
                System.out.println("********* Game Finished *********");
            }

            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            out.println("Command:" + Commands.getQuitCommand());
        }
    }

    private void printStatus() {
        if (getGameState() == GameState.player1Win) {
            System.out.printf("Player: %s win \n", getPlayers().get(0).getName());
            System.out.printf("Player: %s loose \n", getPlayers().get(1).getName());
        } else if (getGameState() == GameState.player2Win) {
            System.out.printf("Player: %s win \n", getPlayers().get(1).getName());
            System.out.printf("Player: %s loose \n", getPlayers().get(0).getName());
        }
    }
}
