package at.mvgeboltskirchen.kroissma.mvgapp.server.datagenerator.util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

    /**
     * @return a random number between min and max
     */
    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
