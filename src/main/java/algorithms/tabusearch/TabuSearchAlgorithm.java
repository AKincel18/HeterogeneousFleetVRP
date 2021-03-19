package algorithms.tabusearch;

import algorithms.tabusearch.model.ParametersTabuSearch;
import algorithms.tabusearch.model.ResultTabu;
import commons.Algorithm;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.List;

import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class TabuSearchAlgorithm implements Algorithm {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersTabuSearch parameters;

    public void start() {
        //generate random result
        City depotCity = Utils.getDepotByCity(depot);
        ResultTabu currentResultTabu = new ResultTabu(generateRandomResult(vehicles, cities, depotCity));

        Writer.buildTitleOnConsole("Generated random solution");
        Writer.writeResult(currentResultTabu.getResult());

        TabuSearchNeighborhoodSolution neighborhoodSolution = new TabuSearchNeighborhoodSolution(cities, vehicles,
                depotCity, currentResultTabu, parameters);

        for (int i = 0; i < parameters.getIterationNumber(); i++) {
            neighborhoodSolution.findSolutionFromNeighborhood();
        }

        Writer.buildTitleOnConsole("Final result");
        Writer.writeResult(neighborhoodSolution.getCurrentResultTabu().getResult());
    }
}
