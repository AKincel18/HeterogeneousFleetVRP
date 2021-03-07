package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.List;
import java.util.Map;

import static utils.Utils.countSumOfResult;
import static utils.Utils.generateRandomResult;

@RequiredArgsConstructor
public class LocalSearchAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final LocalSearchMethod method;

    public void start() {
        //generate random result
        City depotCity = Utils.getDepotByCity(depot);
        Map<Vehicle, List<City>> routes = generateRandomResult(vehicles, cities, depotCity);
        Result currentResult = new Result(routes);
        currentResult.setSum(countSumOfResult(routes));

        Writer.buildTitleOnConsole("Generated random solve");
        Writer.writeResult(currentResult);

        SolveFromNeighborhood solveFromNeighborhood = new SolveFromNeighborhood(cities, vehicles, depotCity, method, currentResult);
        int iterationNumber = 0;
        do {
            Writer.buildTitleOnConsole("Iteration nr = " + iterationNumber);
            solveFromNeighborhood.findSolveFromNeighborhood();
            iterationNumber++;
        } while (solveFromNeighborhood.isFoundBetterResult());

        Writer.buildTitleOnConsole("FINAL RESULT");
        Writer.writeResult(solveFromNeighborhood.getCurrentResult());
        System.out.println();
        System.out.println();
        System.out.println("Iteration nr = " + iterationNumber);
    }

}
