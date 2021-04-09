package algorithms.genetic.geneticoperations;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividualsDecode;
import algorithms.genetic.model.ParametersGenetic;
import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Encoder;
import utils.Pair;

import java.util.*;

import static algorithms.genetic.geneticoperations.Utils.isInRange;
import static utils.Utils.check;

@RequiredArgsConstructor
public class Crossover {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depot;
    private final ParametersGenetic params;
    @Getter private Individual individualNew1;
    @Getter private Individual individualNew2;

    private final PartialMappedCrossover partialMappedCrossover = new PartialMappedCrossover();

    public void startCrossover(PairIndividualsDecode pairIndividualsDecode, List<Integer> cutPoints1, List<Integer> cutPoints2) {
        Pair<Integer, Integer> points = drawCrossoverPoints();
        int leftCutPoints = points.getObj1();
        int rightCutPoints = points.getObj2();
        Integer[] i1 = pairIndividualsDecode.getIndividual1().toArray(new Integer[0]);
        Integer[] i2 = pairIndividualsDecode.getIndividual2().toArray(new Integer[0]);

//        System.out.println("Before");
//        Writer.writeDecodedResultInOneRow(Arrays.asList(i1), cutPoints1);
//        Writer.writeDecodedResultInOneRow(Arrays.asList(i2), cutPoints2);

        partialMappedCrossover.start(leftCutPoints, rightCutPoints, i1, i2);

//        System.out.println("After, points = " + leftCutPoints + ", " + rightCutPoints);
//        Writer.writeDecodedResultInOneRow(Arrays.asList(i1), cutPoints1);
//        Writer.writeDecodedResultInOneRow(Arrays.asList(i2), cutPoints2);

        //todo remove after testing
        check(cities, Arrays.asList(i1), cutPoints1);
        check(cities, Arrays.asList(i2), cutPoints2);

        Result result1 = new Encoder(vehicles, cities, depot, null, cutPoints1, i1).encodeResult2();
        Result result2 = new Encoder(vehicles, cities, depot, null, cutPoints2, i2).encodeResult2();

//        System.out.println("Encoded");
//        Writer.writeResult(result1);
//        Writer.writeResult(result2);

        individualNew1 = new Individual(result1, depot);
        individualNew2 = new Individual(result2, depot);

    }

    private Pair<Integer, Integer> drawCrossoverPoints() {
        int point1, point2;
        Random random = new Random();
        do {
            point1 = random.nextInt(cities.size()) + 1;
            point2 = random.nextInt(cities.size()) + 1;
        } while (point1 == point2);
        return new Pair<>(Math.min(point1, point2), Math.max(point1, point2));
    }

    public boolean isCrossover() {
        return isInRange(params.getCrossoverProbability());
    }
}
