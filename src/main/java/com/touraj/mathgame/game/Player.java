package com.touraj.mathgame.game;

/**
 * Created by toraj on 06/12/2017.
 */
public class Player {

    String name;
    int number;
    boolean makeFirstMove;

    public Player(String name, int number, boolean makeFirstMove) {
        this.name = name;
        this.number = number;
        this.makeFirstMove = makeFirstMove;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", makeFirstMove=" + makeFirstMove +
                '}';
    }
}
