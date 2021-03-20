package algorithms.simulatedannealing;

import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.Algorithm;
import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.List;

import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class SimulatedAnnealingAlgorithm implements Algorithm {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersSimulatedAnnealing params;

    public void start() {
        //generate random result
        City depotCity = Utils.getDepotByCity(depot);
        Result currentResult = generateRandomResult(vehicles, cities, depotCity);

        Writer.buildTitleOnConsole("Generated random solution");
        Writer.writeResult(currentResult);
        //int stopCondition = 5;
        //int stopCondition = (vehicles.size() * cities.size());
        int stopCondition = (int) Math.sqrt(vehicles.size() * cities.size());

        double currentTemperature = new TemperatureZero(cities, vehicles, depotCity, currentResult, params).countTemperatureZero();
        //double currentTemperature = 1000.0;

        Writer.buildTitleOnConsole("t0 = " + currentTemperature);

        SimAnnealingNeighborhoodSolution solutionFromNeighborhood = new SimAnnealingNeighborhoodSolution(cities, vehicles,
                depotCity, currentResult);
        SolutionAnalysis analysis = new SolutionAnalysis(stopCondition);
        int iteration = 0;
        do {
            boolean isImprovement = false;
            for (int i = 0; i < params.getIterationNumber(); i++) {
                iteration++;
                solutionFromNeighborhood.findSolutionFromNeighborhood();
                if (solutionFromNeighborhood.isFoundNewResult()) {
                    currentResult = analysis.findCurrentResult(currentResult,
                            solutionFromNeighborhood.getCurrentResult(), currentTemperature);
                    if (analysis.isNewResult()) {
                        isImprovement = true;
                    }
                    solutionFromNeighborhood.setCurrentResult(currentResult);
                }
            }
            if (isImprovement) {
                analysis.setNoImprovementCounter(0);
            }
            else {
                analysis.checkFinish();
            }

            currentTemperature *= params.getCoolingFactor();
            System.out.println("distance = " + currentResult.getSum() + ", t = " + currentTemperature);
        } while (!analysis.isStop()); //while (!analysis.isStop() && currentTemperature > 5);

        Writer.buildTitleOnConsole("FINAL RESULT");
        Writer.writeResult(currentResult);
        System.out.println("Iteration = " + iteration);
        System.out.println("Temperature = " + currentTemperature);

    }


}
