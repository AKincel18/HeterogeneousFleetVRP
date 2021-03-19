package algorithms.simulatedannealing;

import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class SolutionAnalysis {
    private final int stopCondition;
    @Getter private boolean isNewResult;
    private int noImprovementCounter = 0;
    @Getter private boolean stop = false;

    public Result findCurrentResult(Result currentResult, Result foundResult, double currentTemperature) {
        isNewResult = false;
        if (currentResult.getSum() > foundResult.getSum() ||
                isAcceptableWorstResult(currentResult.getSum(), foundResult.getSum(), currentTemperature)) {
            //System.out.println("Find better solution or accept worst");
            isNewResult = true;
            noImprovementCounter = 0;
            return foundResult;
        }

        //System.out.println("Old solution was chosen!!!");
        return currentResult;
    }

    private boolean isAcceptableWorstResult(Double currentSum, Double foundSum, double currentTemperature) {
        double acceptableProbability = countAcceptable(currentSum, foundSum, currentTemperature);
        double probability = new Random().nextDouble();
        return probability < acceptableProbability;
    }

    private double countAcceptable(Double currentSum, Double foundSum, double currentTemperature) {
        return Math.exp((currentSum - foundSum) / currentTemperature);
    }

    public void incrementNoImprovementCounter() {
        this.noImprovementCounter++;
        if (noImprovementCounter == stopCondition){
            stop = true;
        }
    }

}
