package algorithms.simulatedannealing;

import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.List;

@RequiredArgsConstructor
public class TemperatureZero {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depotCity;
    private final Result currentResult;
    private final ParametersSimulatedAnnealing params;

    //todo situation t0 = 0
    public double countTemperatureZero() {
        SimAnnealingNeighborhoodSolution solutionFromNeighborhood =
                new SimAnnealingNeighborhoodSolution(cities, vehicles, depotCity, currentResult);
        double sum = 0.0;
        int counter = 0;
        for (int i = 0; i < params.getNeighborhoodSolveMaxNumber(); i++) {
            solutionFromNeighborhood.findSolutionFromNeighborhood();
            if (solutionFromNeighborhood.isFoundNewResult() && solutionFromNeighborhood.getCurrentResult().getSum() > currentResult.getSum()) {
                counter++;
                sum += (solutionFromNeighborhood.getCurrentResult().getSum() - currentResult.getSum());
            }
            solutionFromNeighborhood.setCurrentResult(currentResult);
        }
        double avg = counter == 0 ? 0.0 : sum / counter;
        return -avg / Math.log(params.getProbability());
    }
}
