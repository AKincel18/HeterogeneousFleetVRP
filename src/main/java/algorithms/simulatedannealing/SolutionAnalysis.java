package algorithms.simulatedannealing;

import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Random;

@RequiredArgsConstructor
public class SolutionAnalysis {
    private final int stopCondition;
    @Getter private boolean isNewResult;
    @Setter private int noImprovementCounter = 0;
    @Getter private boolean stop = false;

    public Result findCurrentResult(Result currentResult, Result foundResult, double currentTemperature) {

        isNewResult = false;
        if (currentResult.getSum() >= foundResult.getSum() ||
                isAcceptableWorstResult(currentResult.getSum(), foundResult.getSum(), currentTemperature)) {
            isNewResult = true;
            return foundResult;
        }
        return currentResult;
    }

    private boolean isAcceptableWorstResult(Double currentSum, Double foundSum, double currentTemperature) {
        double acceptableProbability = Math.exp((currentSum - foundSum) / currentTemperature);
        double probability = new Random().nextDouble();
        return probability < acceptableProbability;
    }

    public void checkFinish() {
        this.noImprovementCounter++;
        if (noImprovementCounter == stopCondition){
            stop = true;
        }
    }
}
