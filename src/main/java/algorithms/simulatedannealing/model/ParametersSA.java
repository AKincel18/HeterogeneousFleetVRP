package algorithms.simulatedannealing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ParametersSA {
    //private final double t0;
    private final double probability; //accept solution with given probability
    private final int neighborhoodSolveNumber; //to count t0
    private final double coolingFactor; //alpha, range: <0.8,0.99>
    private final int iterationNumber; //iteration number for given temperature
}
