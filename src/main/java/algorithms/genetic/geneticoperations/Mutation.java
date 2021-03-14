package algorithms.genetic.geneticoperations;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static algorithms.genetic.geneticoperations.Utils.isInRange;

@RequiredArgsConstructor
public class Mutation {
    private final int vehicleNumber;
    private final double mutationProbability;

    public void makeMutation(Integer[][] ind) {

        int vehicle = new Random().nextInt(vehicleNumber);
        boolean isMutationPossible = checkMutationPossibility(ind[vehicle]);
        if (isMutationPossible) {
            List<Integer> mutationCities = drawMutationCities(ind[vehicle]);
            int oldValue1 = ind[vehicle][mutationCities.get(0)];
            int oldValue2 = ind[vehicle][mutationCities.get(1)];

            ind[vehicle][mutationCities.get(1)] = oldValue1;
            ind[vehicle][mutationCities.get(0)] = oldValue2;
        }
    }

    /**
     * If is only one city in a route -> mutation in impossible
     *
     * @param route route
     * @return is mutation possible
     */
    private boolean checkMutationPossibility(Integer[] route) {
        for (Integer city : route) {
            if (city > 1)
                return true;
        }
        return false;
    }

    /**
     * Draws cities to the mutation
     *
     * @param route route
     * @return two numbers of city
     */
    private List<Integer> drawMutationCities(Integer[] route) {
        int c1, c2;
        do {
            c1 = new Random().nextInt(route.length);
            c2 = new Random().nextInt(route.length);
        } while (route[c1] == 0 || route[c2] == 0 || c1 == c2);
        return Arrays.asList(c1, c2);
    }

    public boolean isMutation() {
        return isInRange(mutationProbability);
    }
}
