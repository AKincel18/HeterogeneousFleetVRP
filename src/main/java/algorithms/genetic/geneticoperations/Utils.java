package algorithms.genetic.geneticoperations;

import java.util.Random;

public class Utils {

    static boolean isInRange(double probability) {
        double randomNumber = new Random().nextDouble();
        return probability > randomNumber;
    }
}
