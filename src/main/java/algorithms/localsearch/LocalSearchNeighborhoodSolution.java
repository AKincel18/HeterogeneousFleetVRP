package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.Result;
import commons.SolutionFromNeighborhood;
import lombok.Getter;
import lombok.NonNull;
import model.City;
import model.Vehicle;
import utils.Decoder;
import utils.Writer;

import java.util.List;


public class LocalSearchNeighborhoodSolution extends SolutionFromNeighborhood {

    private final LocalSearchMethod method;
    @Getter private boolean isFoundBetterResult;

    public LocalSearchNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles, City depotCity,
                                           LocalSearchMethod method, @NonNull Result currentResult) {
        super(cities, vehicles, depotCity, currentResult);
        this.method = method;
    }

    public void findSolutionFromNeighborhood() {
        isFoundSolutionInNeighborhood = false;
        isFoundBetterResult = false;
        currentDecodedResult =  new Decoder(cities).decodeResult(currentResult.getRoutes());
        bestNeighborhoodResult = new Result(currentResult);

        //Writer.buildTitleOnConsole("Base result");
        //Writer.writeDecodedResultInOneRow(currentDecodedResult);
        //Writer.buildTitleOnConsole("DECODED RESULT");
        //Writer.writeDecodedResultInOneRow(currentDecodedResult);

        for (int vehicle = 0; vehicle < vehicles.size(); vehicle++) {
            findInTheSameVehicle(vehicle);
            if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                //System.out.println("STOP, FOUND BETTER SOLUTION");
                //Writer.writeResult(bestNeighborhoodResult);
                currentResult = bestNeighborhoodResult;
                return;
            }
            for (int city = 0; city < cities.size(); city++) {
                int visitOrder = currentDecodedResult[vehicle][city];
                if (visitOrder != 0) {
                    findInOtherVehicles(currentDecodedResult, visitOrder, vehicle, city);
                    if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                        //System.out.println("STOP, FOUND BETTER SOLUTION");
                        //Writer.writeResult(bestNeighborhoodResult);
                        currentResult = bestNeighborhoodResult;
                        return;
                    }
                }
            }
        }

        if (isFoundSolutionInNeighborhood) {
            isFoundBetterResult = true;
            //System.out.println("Found better solution in steepest: ");
            //Writer.writeResult(bestNeighborhoodResult);
            currentResult = bestNeighborhoodResult;
        }
    }

    private void findInTheSameVehicle(int vehicle) {
        for (int city1 = 0; city1 < cities.size(); city1++) {
            if (currentDecodedResult[vehicle][city1] != 0) {
                for (int city2 = city1 + 1; city2 < cities.size(); city2++) {
                    if (currentDecodedResult[vehicle][city2] != 0) {
                        int visitOrder1 = currentDecodedResult[vehicle][city1];
                        int visitOrder2 = currentDecodedResult[vehicle][city2];
                        exchangeSameVehicle(vehicle, city1, city2, visitOrder1, visitOrder2);
                        isFoundBetterResult = checkNewResultLocalSearch(vehicle, vehicle, city1, city2);
                        if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                            //System.out.println("STOP, FOUND BETTER SOLUTION");
                            return;
                        }
                    }
                }
            }

        }
    }

    private void findInOtherVehicles(Integer[][] decodedResult, int visitOrder,
                                     int vehicle1, int city1) {
        for (int vehicle2 = 0; vehicle2 < vehicles.size(); vehicle2++) {
            //not find in the same vehicle
            if (vehicle1 != vehicle2) {
                for (int city2 = 0; city2 < cities.size(); city2++) {
                    int foundVisitOrder = decodedResult[vehicle2][city2];
                    //find the same visit order and in vehicle with higher id (with prev id was searched before)
                    if (foundVisitOrder == visitOrder && vehicle2 > vehicle1) {
                        exchange(vehicle1, city1, vehicle2, city2, visitOrder);
                        isFoundBetterResult = checkNewResultLocalSearch(vehicle1, vehicle2, city1, city2);
                        if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                            //System.out.println("STOP, FOUND BETTER SOLUTION");
                            return;
                        }
                        // replaced higher number from one vehicle route to another vehicle route
                        // where higher number is one less then replaced vehicle route
                        // must be replaced on the same position
                    } else if (city1 == city2 &&
                            isMaxVisitOrder(decodedResult[vehicle2], visitOrder) &&
                            isMaxVisitOrderAnalyzed(decodedResult[vehicle1], visitOrder)) {
                        exchangeZero(vehicle1, city1, vehicle2, city2, visitOrder);
                        isFoundBetterResult = checkNewResultLocalSearch(vehicle1, vehicle2, city1, city2);
                        if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                            //System.out.println("STOP, FOUND BETTER SOLUTION");
                            return;
                        }
                    }
                }
            }
        }
    }
}
