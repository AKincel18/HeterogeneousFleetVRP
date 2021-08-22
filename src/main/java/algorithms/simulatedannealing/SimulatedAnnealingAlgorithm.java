package algorithms.simulatedannealing;

import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.algorithms.Algorithm;
import commons.algorithms.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;

import java.util.List;

@RequiredArgsConstructor
public class SimulatedAnnealingAlgorithm implements Algorithm {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersSimulatedAnnealing params;

    @Getter private Result result;

    public void start() {
        City depotCity = Utils.getCityByDepot(depot);
        InitialConditions initialConditions = new InitialConditions(cities, vehicles, depotCity, params);
        initialConditions.generateInitialConditions();
        double currentTemperature = initialConditions.getTemperatureZero();
        Result currentResult = initialConditions.getGeneratedResult();

        int stopCondition = (int) Math.sqrt(vehicles.size() * cities.size());

        SimAnnealingNeighborhoodSolution solutionFromNeighborhood = new SimAnnealingNeighborhoodSolution(cities, vehicles,
                depotCity, currentResult);
        SolutionAnalysis analysis = new SolutionAnalysis(stopCondition);

        do {
            boolean isImprovement = false;
            for (int i = 0; i < params.getIterationNumber(); i++) {
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
            } else {
                analysis.checkFinish();
            }
            currentTemperature *= params.getCoolingFactor();
        } while (!analysis.isStop());

        result = currentResult;

    }


}
