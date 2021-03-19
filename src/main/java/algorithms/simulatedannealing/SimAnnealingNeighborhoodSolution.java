package algorithms.simulatedannealing;

import commons.Result;
import commons.SolutionFromNeighborhood;
import lombok.Getter;
import lombok.NonNull;
import model.City;
import model.Vehicle;
import utils.Decoder;

import java.util.List;

import static utils.Utils.getAnalyzed;

public class SimAnnealingNeighborhoodSolution extends SolutionFromNeighborhood {

    @Getter private boolean isFoundNewResult;

    public SimAnnealingNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles,
                                            City depotCity, @NonNull Result currentResult) {
        super(cities, vehicles, depotCity, currentResult);
    }

    public void findSolutionFromNeighborhood() {
        isFoundNewResult = false;
        currentDecodedResult =  new Decoder(cities).decodeResult(currentResult.getRoutes());
        bestNeighborhoodResult = new Result(currentResult);

        int vecIterator = 0;
        boolean[] vehicleAnalyzed = new boolean[vehicles.size()];
        do {
            int vehicle = getAnalyzed(vehicleAnalyzed);
            findInTheSameVehicle(vehicle);
            vehicleAnalyzed[vehicle] = true;
            boolean[] cityAnalyzed = new boolean[cities.size()];
            int cityIterator = 0;
            do {
                int city = getAnalyzed(cityAnalyzed);
                cityAnalyzed[city] = true;

                int visitOrder = currentDecodedResult[vehicle][city];
                if (visitOrder != 0) {
                    findInOtherVehiclesRandom(currentDecodedResult, visitOrder, vehicle, city);
                    if (isFoundNewResult) {
                        currentResult = bestNeighborhoodResult;
                        //System.out.println("STOOOOOOP");
                        return;
                    }
                }

                cityIterator++;
            } while (cityIterator != cities.size());
            vecIterator++;
        } while (vecIterator != vehicles.size());
    }

    private void findInTheSameVehicle(int vehicle1) {
        for (int city1 = 0; city1 < cities.size(); city1++) {
            if (currentDecodedResult[vehicle1][city1] != 0) {
                for (int city2 = city1 + 1; city2 < cities.size(); city2++) {
                    if (currentDecodedResult[vehicle1][city2] != 0) {
                        int visitOrder1 = currentDecodedResult[vehicle1][city1];
                        int visitOrder2 = currentDecodedResult[vehicle1][city2];
                        exchangeSameVehicle(vehicle1, city1, city2, visitOrder1, visitOrder2);
                        isFoundNewResult = checkNewResultSimulatedAnnealing();
                        if (isFoundNewResult) {
                            return;
                        }
                    }
                }
            }

        }
    }

    private void findInOtherVehiclesRandom(Integer[][] decodedResult, int visitOrder,
                                           int vehicle1, int city1) {
        boolean[] vehicleAnalyzed = new boolean[vehicles.size()];
        int vecIterator = 0;
        do {
            int vehicle2 = getAnalyzed(vehicleAnalyzed);
            vehicleAnalyzed[vehicle2] = true;
            boolean[] cityAnalyzed = new boolean[cities.size()];
            int cityIterator = 0;
            do {
                int city2 = getAnalyzed(cityAnalyzed);
                cityAnalyzed[city2] = true;

                int foundVisitOrder = decodedResult[vehicle2][city2];
                //find the same visit order and in vehicle with higher id (with prev id was searched before)
                if (foundVisitOrder == visitOrder && vehicle2 > vehicle1) {
                    exchange(vehicle1, city1, vehicle2, city2, visitOrder);
                    isFoundNewResult = checkNewResultSimulatedAnnealing();
                    if (isFoundNewResult) {
                        //System.out.println("Find new solution");
                        return;
                    }
                    // replaced higher number from one vehicle route to another vehicle route
                    // where higher number is one less then replaced vehicle route
                    // must be replaced on the same position
                } else if (city2 == city1 &&
                        isMaxVisitOrder(decodedResult[vehicle2], visitOrder) &&
                        isMaxVisitOrderAnalyzed(decodedResult[vehicle1], visitOrder)) {
                    exchangeZero(vehicle1, city1, vehicle2, city2, visitOrder);
                    isFoundNewResult = checkNewResultSimulatedAnnealing();
                    if (isFoundNewResult) {
                        //System.out.println("Find new solution");
                        return;
                    }

                }

                cityIterator++;
            } while (cityIterator != cities.size());
            vecIterator++;
        } while (vecIterator != vehicles.size());

    }
}
