package algorithms.simulatedannealing;

import commons.Result;
import commons.SolutionFromNeighborhood;
import lombok.Getter;
import lombok.NonNull;
import model.City;
import model.Vehicle;
import utils.Decoder;

import java.util.List;
import java.util.Random;

import static utils.Utils.generateListOfNumbers;
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
            boolean findInTheSame = new Random().nextBoolean();
            if (findInTheSame) {
                findInTheSameVehicle(vehicle);
            }
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

        int city1 = findFirstVisitedPlace(vehicle1);
        int city2 = findSecondVisitedPlace(vehicle1, city1);
        if (city1 != -1 && city2 != -1) {
            int visitOrder1 = currentDecodedResult[vehicle1][city1];
            int visitOrder2 = currentDecodedResult[vehicle1][city2];
            exchangeSameVehicle(vehicle1, city1, city2, visitOrder1, visitOrder2);
            isFoundNewResult = checkNewResultSimulatedAnnealing();
        }
    }

    private int findFirstVisitedPlace(int vehicle) {
        List<Integer> randomCities = generateListOfNumbers(cities.size());
        for (Integer city : randomCities) {
            if (currentDecodedResult[vehicle][city] != 0) {
                return city;
            }
        }
        return -1;
    }

    private int findSecondVisitedPlace(int vehicle, int city1) {
        List<Integer> randomCities = generateListOfNumbers(cities.size());
        for (Integer city : randomCities) {
            if (currentDecodedResult[vehicle][city] != 0 && city != city1) {
                return city;
            }
        }
        return -1;
    }

    private void findInOtherVehiclesRandom(Integer[][] decodedResult, int visitOrder,
                                           int vehicle1, int city1) {
        boolean[] vehicleAnalyzed = new boolean[vehicles.size()];
        vehicleAnalyzed[vehicle1] = true; //not finding in the same vehicle
        int vecIterator = 1;
        do {
            int vehicle2 = getAnalyzed(vehicleAnalyzed);
//            if (vehicle1 == vehicle2)
//                continue;
            vehicleAnalyzed[vehicle2] = true;
            //not find in the same vehicle
            for (int city2 = 0; city2 < cities.size(); city2++) {
                int foundVisitOrder = decodedResult[vehicle2][city2];
                //find the same visit order and in vehicle with higher id (with prev id was searched before)
                if (foundVisitOrder == visitOrder) {
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
            }

        vecIterator++;
        } while (vecIterator != vehicles.size());
    }
}
