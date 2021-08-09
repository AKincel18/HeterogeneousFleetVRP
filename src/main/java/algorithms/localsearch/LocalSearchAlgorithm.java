package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.Algorithm;
import commons.Result;
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

        //Writer.buildTitleOnConsole("Generated random solution");
//        Result currentResult = generateStaticResult(vehicles, cities, depotCity);
//        Writer.buildTitleOnConsole("Generated static solution");

        //Writer.writeResult(currentResult);

        LocalSearchNeighborhoodSolution solutionFromNeighborhood =
                new LocalSearchNeighborhoodSolution(cities, vehicles, depotCity, method, currentResult);
        int iterationNumber = 0;
        do {
            solutionFromNeighborhood.findSolutionFromNeighborhood();
            iterationNumber++;
        } while (solutionFromNeighborhood.isFoundBetterResult());

        //Writer.buildTitleOnConsole("FINAL RESULT");
        //Writer.writeResult(solutionFromNeighborhood.getCurrentResult());
        //System.out.println("Iteration nr = " + iterationNumber);
        result = solutionFromNeighborhood.getCurrentResult();
    }
}


