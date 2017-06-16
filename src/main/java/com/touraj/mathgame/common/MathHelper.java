package com.touraj.mathgame.common;

/**
 * Created by toraj on 06/14/2017.
 */
public class MathHelper {

    public static int whatNumberAddToBeDivisibleByThree(int number) {
        switch (number % 3) {
            case 0:
                return 0;
            case 1:
                return -1;
            case 2:
                return 1;
        }
        return 0;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public static int generateRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min should be lesser than max");
        }
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
