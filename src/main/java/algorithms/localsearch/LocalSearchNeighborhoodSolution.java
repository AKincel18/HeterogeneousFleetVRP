package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.NeighborhoodSolution;
import commons.Result;
import lombok.Getter;
import lombok.NonNull;
import model.City;
import model.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static utils.Utils.check;
import static utils.Utils.checkIsAcceptableWeightAll;


public class LocalSearchNeighborhoodSolution extends NeighborhoodSolution {

    private final LocalSearchMethod method;
    @Getter private Result currentResult;
    @Getter private boolean isFoundBetterResult;

    public LocalSearchNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles, City depotCity,
                                           LocalSearchMethod method, @NonNull Result currentResult) {
        super(cities, vehicles, depotCity);
        this.method = method;
        this.currentResult = currentResult;
    }

    public void findSolutionFromNeighborhood() {
        isFoundBetterResult = false;
        if (random.nextBoolean())
            replaceTwoCities();
        else
            putCityToAnotherVehicle();
        check(cities.size(), currentResult);
    }

    private void replaceTwoCities() {
        Integer[] decodedResult = coder.codeResultToArray(currentResult.getRoutes());
        for (int i = 0; i < decodedResult.length - 1; i++) {
            for (int j = i + 1; j < decodedResult.length; j++) {
                Integer[] neighborhoodDecodedResult = initArray(decodedResult);
                Collections.swap(Arrays.asList(neighborhoodDecodedResult), i, j);
                Result resultNew = decoder.decodeResultFromArray(neighborhoodDecodedResult, coder.getCutPoints());
                if (checkIsAcceptableWeightAll(resultNew.getRoutes()) && resultNew.isBetter(currentResult)) {
                    currentResult = resultNew;
                    isFoundBetterResult = true;
                    if (method == LocalSearchMethod.GREEDY) {
                        return;
                    }
                }
            }
        }
    }

    private void putCityToAnotherVehicle() {
        Map<Integer, List<Integer>> decodedResult = coder.codeResultToMap(currentResult.getRoutes());
        for (Map.Entry<Integer, List<Integer>> removeEntry : decodedResult.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> addEntry : decodedResult.entrySet()) {
                if (removeEntry == addEntry || removeEntry.getValue().size() == 1) continue;
                for (Integer cityToRemove : removeEntry.getValue()) {
                    for (int i = 0; i <= addEntry.getValue().size(); i++) {
                        Map<Integer, List<Integer>> neighborhoodDecodedResult = initMap(decodedResult);
                        neighborhoodDecodedResult.get(removeEntry.getKey()).remove(cityToRemove);
                        if (i == addEntry.getValue().size()) {
                            neighborhoodDecodedResult.get(addEntry.getKey()).add(cityToRemove);
                        } else {
                            neighborhoodDecodedResult.get(addEntry.getKey()).add(i, cityToRemove);
                        }
                        Result resultNew = decoder.decodeResultFromMap(neighborhoodDecodedResult);
                        if (checkIsAcceptableWeightAll(resultNew.getRoutes()) && resultNew.isBetter(currentResult)) {
                            currentResult = resultNew;
                            isFoundBetterResult = true;
                            if (method == LocalSearchMethod.GREEDY) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
