package com.touraj.mathgame.common;

/**
 * Created by toraj on 06/12/2017.
 */
public class Configuration {

    private static final int port = 7077;

    //[Touraj] for Having good AI experience I suggest AI select Initial Number in range [10,500] ...
    // You can change following too
    private static final int minNumberForAIMode = 10;
    private static final int maxNumberForAIMode = 500;

    //[Touraj] : Following parameter mimics that AI is thinking like Human (it is in Millisecond)
    private static final int simulateHumanDelayForAIinMilliSecond = 2000;


    private static final String welcomeMessage = "" +
            "*********************************\n" +
            "**                             **\n" +
            "**    Welcome to Math Game     **\n" +
            "**        Developed By         **\n" +
            "**       Touraj Ebrahimi       **\n" +
            "**                             **\n" +
            "*********************************\n";

    private static final String menu = "" +
            "************ Options ************\n" +
            "**                             **\n" +
            "** 1- Create Server            **\n" +
            "** 2- Connect to Server        **\n" +
            "** 3- Exit                     **\n" +
            "**                             **\n" +
            "*********************************\n";

    private static final String gameMode = "" +
            "************* Modes *************\n" +
            "**                             **\n" +
            "** 1- Play yourself            **\n" +
            "** 2- AI mode (Automatic Play) **\n" +
            "**                             **\n" +
            "*********************************\n";

    private static final String winMessage = "" +
            "*********************************\n" +
            "**                             **\n" +
            "**       You win the game      **\n" +
            "**        Congratulation       **\n" +
            "**                             **\n" +
            "*********************************\n";

    private static final String gameOverMessage = "" +
            "*********************************\n" +
            "**                             **\n" +
            "**           Game Over         **\n" +
            "**       You Loose the game    **\n" +
            "**                             **\n" +
            "*********************************\n";

    public static String getGameMode() {
        return gameMode;
    }

    private static final String invalidOptionMessage = "You entered invalid option; Please try again.";

    public static String getInvalidOptionMessage() {
        return invalidOptionMessage;
    }

    public static String getWinMessage() {
        return winMessage;
    }

    public static String getGameOverMessage() {
        return gameOverMessage;
    }

    public static int getPort() {
        return port;
    }

    public static String getWelcomeMessage() {
        return welcomeMessage;
    }

    public static String getMenu() {
        return menu;
    }

    public static int getMinNumberForAIMode() {
        return minNumberForAIMode;
    }

    public static int getMaxNumberForAIMode() {
        return maxNumberForAIMode;
    }

    public static int getSimulateHumanDelayForAIinMilliSecond() {
        return simulateHumanDelayForAIinMilliSecond;
    }

    public static void showWelcomeMessage() {
        System.out.println(Configuration.getWelcomeMessage());
    }

    public static void buildMenu() {
        System.out.println(Configuration.getMenu());
    }

    public static void showGameMode() {
        System.out.println(Configuration.getGameMode());
    }


}
