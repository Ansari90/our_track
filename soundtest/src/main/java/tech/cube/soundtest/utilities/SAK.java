package tech.cube.soundtest.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class SAK {

    public static int RANDOM(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
