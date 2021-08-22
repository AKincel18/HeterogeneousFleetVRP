package algorithms.simulatedannealing;

import commons.algorithms.NeighborhoodSolution;
import commons.algorithms.Result;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import model.City;
import model.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static utils.Utils.checkIsAcceptableWeightAll;
import static utils.Utils.generateListOfNumbers;

public class SimAnnealingNeighborhoodSolution extends NeighborhoodSolution {

    @Getter private boolean isFoundNewResult;
    @Getter @Setter private Result currentResult;

    public SimAnnealingNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles,
                                            City depotCity, @NonNull Result currentResult) {
        super(cities, vehicles, depotCity);
        this.currentResult = currentResult;
    }

    public void findSolutionFromNeighborhood() {
        isFoundNewResult = false;
        if (random.nextBoolean())
            replaceTwoCitiesRandom();
        else
            putCityToAnotherVehicleRandom();
    }

    private void replaceTwoCitiesRandom() {
        Integer[] decodedResult = coder.codeResultToArray(currentResult.getRoutes());
        Integer[] positionsToSwapped = generateListOfNumbers(cities.size()).toArray(new Integer[0]);
        for (int i = 0; i < positionsToSwapped.length - 1; i++) {
            for (int j = i + 1; j < positionsToSwapped.length; j++) {
                Integer[] neighborhoodDecodedResult = new Integer[cities.size()];
                System.arraycopy(decodedResult, 0, neighborhoodDecodedResult, 0, cities.size());
                int pos1 = positionsToSwapped[i];
                int pos2 = positionsToSwapped[j];
                Collections.swap(Arrays.asList(neighborhoodDecodedResult), pos1, pos2);
                Result resultNew = decoder.decodeResultFromArray(neighborhoodDecodedResult, coder.getCutPoints());
                if (checkIsAcceptableWeightAll(resultNew.getRoutes())) {
                    currentResult = resultNew;
                    isFoundNewResult = true;
                    return;
                }
            }
        }
    }

    private void putCityToAnotherVehicleRandom() {
        Map<Integer, List<Integer>> decodedResult = coder.codeResultToMap(currentResult.getRoutes());
        Integer[] vehiclesOrder = generateListOfNumbers(vehicles.size()).toArray(new Integer[0]);
        for (int i = 0; i < vehiclesOrder.length; i++) {
            for (int j = 0; j < vehiclesOrder.length; j++) {
                if (i == j || decodedResult.get(vehiclesOrder[i]).size() == 1) continue;
                List<Integer> positionsToRemove = generateListOfNumbers(decodedResult.get(vehiclesOrder[i]).size());
                List<Integer> positionsToAdd = generateListOfNumbers(decodedResult.get(vehiclesOrder[j]).size() + 1);
                for (Integer posToRemove : positionsToRemove) {
                    for (Integer posToAdd : positionsToAdd) {
                        Map<Integer, List<Integer>> neighborhoodDecodedResult = initMap(decodedResult);
                        List<Integer> removingRoute = neighborhoodDecodedResult.get(vehiclesOrder[i]);
                        List<Integer> addingRoute = neighborhoodDecodedResult.get(vehiclesOrder[j]);

                        Integer city = removingRoute.get(posToRemove);
                        removingRoute.remove(city);

                        if (posToAdd == positionsToAdd.size()) {
                            addingRoute.add(city);
                        } else {
                            addingRoute.add(posToAdd, city);
                        }
                        Result resultNew = decoder.decodeResultFromMap(neighborhoodDecodedResult);
                        if (checkIsAcceptableWeightAll(resultNew.getRoutes())) {
                            currentResult = resultNew;
                            isFoundNewResult = true;
                            return;
                        }
                    }
                }
            }
        }
    }
}
