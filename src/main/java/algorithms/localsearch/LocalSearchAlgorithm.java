package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.algorithms.Algorithm;
import commons.algorithms.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;

import java.util.List;

import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class LocalSearchAlgorithm implements Algorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final LocalSearchMethod method;

    @Getter private Result result;

    public void start() {

        City depotCity = Utils.getCityByDepot(depot);
        Result currentResult = generateRandomResult(vehicles, cities, depotCity);

        LocalSearchNeighborhoodSolution solutionFromNeighborhood =
                new LocalSearchNeighborhoodSolution(cities, vehicles, depotCity, method, currentResult);
        do {
            solutionFromNeighborhood.findSolutionFromNeighborhood();
        } while (solutionFromNeighborhood.isFoundBetterResult());

        result = solutionFromNeighborhood.getCurrentResult();
    }
}


