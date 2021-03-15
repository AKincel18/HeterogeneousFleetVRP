package algorithms.simulatedannealing;

import algorithms.localsearch.SolveFromNeighborhood;
import algorithms.simulatedannealing.model.ParametersSA;
import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static utils.Utils.countSumOfResult;
import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class SimulatedAnnealingAlgorithm {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersSA parametersSA;

    private Result currentResult;

    public void start() {
        //double currentTemperature = parametersSA.getT0();

        //generate random result
        City depotCity = Utils.getDepotByCity(depot);
        Map<Vehicle, List<City>> routes = generateRandomResult(vehicles, cities, depotCity);
        currentResult = new Result(routes);
        currentResult.setSum(countSumOfResult(routes));

        Writer.buildTitleOnConsole("Generated random solve");
        Writer.writeResult(currentResult);
        int stopCondition = (int) Math.sqrt(vehicles.size() * cities.size());
        SolveFromNeighborhood solveFromNeighborhood = new SolveFromNeighborhood(cities, vehicles,
                depotCity, null, currentResult);

        double currentTemperature = countTemperatureZero(solveFromNeighborhood);
        Writer.buildTitleOnConsole("t0 = " + currentTemperature);
        int noImprovementCounter = 0;
        boolean stop = false;

        int iteration = 0;
        do {

            for (int i = 0; i < parametersSA.getIterationNumber(); i++) {
                iteration++;
                solveFromNeighborhood.findRandomSolveFromNeighborhood();
                boolean isFound = false;
                if (solveFromNeighborhood.isFoundNewResult()) {
                    currentResult = getCurrentResult(solveFromNeighborhood.getCurrentResult(), currentTemperature);
                    if (currentResult.equals(solveFromNeighborhood.getCurrentResult())) {
                        isFound = true;
                        noImprovementCounter = 0; //new result was accept
                    }
                    solveFromNeighborhood.setCurrentResult(currentResult);
                }
                if (!isFound) {
                    noImprovementCounter++;
                    if (noImprovementCounter == stopCondition){
                        stop = true;
                        break;
                    }
                }
            }

            currentTemperature *= parametersSA.getCoolingFactor();
        } while (!stop);
        Writer.buildTitleOnConsole("FINAL RESULT");
        Writer.writeResult(currentResult);
        System.out.println("Iteration = " + iteration);

    }

    private Result getCurrentResult(Result foundResult, double currentTemperature) {
        if (currentResult.getSum() > foundResult.getSum() ||
                isAcceptableWorstResult(currentResult.getSum(), foundResult.getSum(), currentTemperature)) {
            //System.out.println("Find better solution or accept worst");
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

    private double countTemperatureZero(SolveFromNeighborhood solveFromNeighborhood) {
        double sum = 0.0;
        int counter = 0;
        for (int i = 0; i < parametersSA.getNeighborhoodSolveNumber(); i++) {
            solveFromNeighborhood.findRandomSolveFromNeighborhood();
            if (solveFromNeighborhood.isFoundNewResult() && solveFromNeighborhood.getCurrentResult().getSum() > currentResult.getSum()) {
                counter++;
                sum += (solveFromNeighborhood.getCurrentResult().getSum() - currentResult.getSum());
            }
            solveFromNeighborhood.setCurrentResult(currentResult);
        }
        double avg = counter == 0 ? 0.0 : sum / counter;
        return -avg / Math.log(parametersSA.getProbability());
    }
}
