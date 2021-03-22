package algorithms.genetic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ParametersGenetic {
    private final int populationSize;
    private final int iterationNumber;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final double crossoverRepeatingNumber;
    private final SelectionMethods selectionMethod;
    private final int tournamentSize;
    private final double selectivePressure; // rank selection <1, 2>
}
