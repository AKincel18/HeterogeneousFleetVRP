package algorithms.simulatedannealing;

import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.algorithms.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.List;

import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class InitialConditions {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depotCity;
    private final ParametersSimulatedAnnealing params;
    @Getter private double temperatureZero;
    @Getter private Result generatedResult;

    public void generateInitialConditions() {
        do {
            generatedResult = generateRandomResult(vehicles, cities, depotCity);
            temperatureZero = countTemperatureZero();
        } while (temperatureZero == 0.0);
    }

    private double countTemperatureZero() {
        SimAnnealingNeighborhoodSolution solutionFromNeighborhood =
                new SimAnnealingNeighborhoodSolution(cities, vehicles, depotCity, generatedResult);
        double sum = 0.0;
        int counter = 0;
        for (int i = 0; i < params.getNeighborhoodSolveMaxNumber(); i++) {
            solutionFromNeighborhood.findSolutionFromNeighborhood();
            if (solutionFromNeighborhood.isFoundNewResult()) {
                if (generatedResult.isBetter(solutionFromNeighborhood.getCurrentResult())) {
                    counter++;
                    sum += (solutionFromNeighborhood.getCurrentResult().getSum() - generatedResult.getSum());
                }
            } else {
                break;
            }
            solutionFromNeighborhood.setCurrentResult(generatedResult);
        }
        double avg = counter == 0 ? 0.0 : sum / counter;
        return -avg / Math.log(params.getProbability());
    }
}
