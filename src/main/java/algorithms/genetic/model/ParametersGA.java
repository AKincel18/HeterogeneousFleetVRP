package algorithms.genetic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ParametersGA {
    private final int populationSize;
    private final int iterationNumber;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final double crossoverRepeatingNumber;
}
