package algorithms.tabusearch;

import algorithms.tabusearch.model.ParametersTabuSearch;
import algorithms.tabusearch.model.ResultTabu;
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
public class TabuSearchAlgorithm implements Algorithm {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersTabuSearch parameters;

    @Getter private Result result;

    public void start() {
        City depotCity = Utils.getCityByDepot(depot);
        ResultTabu currentResultTabu = new ResultTabu(generateRandomResult(vehicles, cities, depotCity));

        TabuSearchNeighborhoodSolution neighborhoodSolution = new TabuSearchNeighborhoodSolution(cities, vehicles,
                depotCity, currentResultTabu, parameters);

        for (int i = 0; i < parameters.getIterationNumber(); i++) {
            neighborhoodSolution.findSolutionFromNeighborhood();
            if (!neighborhoodSolution.isFoundResult()) {
                break;
            }
        }

        result = neighborhoodSolution.getCurrentResult();
    }
}
