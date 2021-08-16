package algorithms.tabusearch;

import algorithms.tabusearch.model.NeighborhoodStrategy;
import algorithms.tabusearch.model.ParametersTabuSearch;
import algorithms.tabusearch.model.ResultTabu;
import algorithms.tabusearch.model.TabuCoords;
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

import static utils.Utils.checkIsAcceptableWeightAll;

public class TabuSearchNeighborhoodSolution extends NeighborhoodSolution {

    private final ParametersTabuSearch params;
    @Getter private boolean isFoundResult;
    @Getter private final int[][] tabuArrayReplacingStrategy;
    @Getter private final int[][] tabuArrayPuttingStrategy;
    @Getter private final int[][] freqArrayPuttingStrategy;
    @Getter private ResultTabu currentResultTabu;
    private ResultTabu bestTabu;
    private TabuCoords lastTabuCoords;
    @Getter private Result currentResult;
    private NeighborhoodStrategy neighborhoodStrategy;

    public TabuSearchNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles,
                                          City depotCity, @NonNull ResultTabu currentResultTabu,
                                          ParametersTabuSearch params) {
        super(cities, vehicles, depotCity);
        this.params = params;
        this.currentResultTabu = currentResultTabu;
        this.currentResult = currentResultTabu.getResult();
        tabuArrayReplacingStrategy = new int[cities.size()][cities.size()];
        lastTabuCoords = new TabuCoords();
        this.tabuArrayPuttingStrategy = new int[vehicles.size()][cities.size()];
        this.freqArrayPuttingStrategy = new int[vehicles.size()][cities.size()];
    }

    public void findSolutionFromNeighborhood() {
        bestTabu = new ResultTabu(null);
        isFoundResult = false;

        currentResultTabu.clear();
        if (random.nextBoolean()) {
            neighborhoodStrategy = NeighborhoodStrategy.REPLACE_CITIES;
            replaceTwoCities();
        }
        else {
            neighborhoodStrategy = NeighborhoodStrategy.PUT_CITY_TO_ANOTHER_VEHICLE;
            putCityToAnotherVehicle();
        }

        if (bestTabu.getResult() != null && bestTabu.getZ() < currentResultTabu.getZ() &&
                bestTabu.getResult().getSum() < currentResult.getSum() ) {
            currentResultTabu = bestTabu;
            isFoundResult = true;
            //System.out.println("Choose tabu result!");
        }

        if (isFoundResult) {
            switch (neighborhoodStrategy) {
                case REPLACE_CITIES:
                    updateTabuReplaceCities();
                    updateMovementFrequencyReplaceCity();
                    break;
                case PUT_CITY_TO_ANOTHER_VEHICLE:
                    updateTabuPutCityToVehicle();
                    updateMovementFrequencyPutCityToVehicle();
                    break;
            }

            currentResult = currentResultTabu.getResult();
            lastTabuCoords = currentResultTabu.getTabuCoords();
        }
    }

    private void replaceTwoCities() {
        Integer[] decodedResult = coder.codeResultToArray(currentResult.getRoutes());
        for (int i = 0; i < decodedResult.length - 1; i++) {
            for (int j = i + 1; j < decodedResult.length; j++) {
                Integer[] neighborhoodDecodedResult = new Integer[cities.size()];
                System.arraycopy(decodedResult, 0, neighborhoodDecodedResult, 0, cities.size());
                Collections.swap(Arrays.asList(neighborhoodDecodedResult), i, j);
                Result resultNew = decoder.decodeResultFromArray(neighborhoodDecodedResult, coder.getCutPoints());
                if (checkIsAcceptableWeightAll(resultNew.getRoutes())) {
                    checkFoundResult(neighborhoodDecodedResult[i], neighborhoodDecodedResult[j], resultNew);
                }
            }
        }
    }

    private void putCityToAnotherVehicle() {
        Map<Integer, List<Integer>> decodedResult = coder.codeResultToMap(currentResult.getRoutes());
        for (Map.Entry<Integer, List<Integer>> entry : decodedResult.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> entry2 : decodedResult.entrySet()) {
                if (entry == entry2 || entry.getValue().size() == 1) continue;
                for (Integer cityToMove : entry.getValue()) {
                    for (int i = 0; i <= entry2.getValue().size(); i++) {
                        Map<Integer, List<Integer>> neighborhoodDecodedResult = initMap(decodedResult);

                        neighborhoodDecodedResult.get(entry.getKey()).remove(cityToMove);
                        if (i == entry2.getValue().size()) {
                            neighborhoodDecodedResult.get(entry2.getKey()).add(cityToMove);
                        } else {
                            neighborhoodDecodedResult.get(entry2.getKey()).add(i, cityToMove);
                        }
                        Result resultNew = decoder.decodeResultFromMap(neighborhoodDecodedResult);
                        if (checkIsAcceptableWeightAll(resultNew.getRoutes())) {
                            //System.out.println("Accept");
                            checkFoundResult(entry2.getKey(), cityToMove, resultNew);
                        }
                    }
                }
            }
        }
    }

    private void updateTabuReplaceCities() {
        //decrease tabu part of array
        for (int i = 0; i < tabuArrayReplacingStrategy.length; i++) {
            for (int j = i + 1; j < tabuArrayReplacingStrategy[i].length; j++) {
                if (tabuArrayReplacingStrategy[i][j] != 0)
                    tabuArrayReplacingStrategy[i][j] -= 1;
            }
        }

        //set tabu number to exchanged position
        int row = currentResultTabu.getTabuCoords().getRow();
        int col = currentResultTabu.getTabuCoords().getCol();
        tabuArrayReplacingStrategy[row][col] = params.getTabuIterationNumber();

    }

    private void updateTabuPutCityToVehicle() {
        //decrease tabu part of array
        for (int i = 0; i < tabuArrayPuttingStrategy.length; i++) {
            for (int j = 0; j < tabuArrayPuttingStrategy[i].length; j++) {
                if (tabuArrayPuttingStrategy[i][j] != 0)
                    tabuArrayPuttingStrategy[i][j] -= 1;
            }
        }

        //set tabu number to exchanged position
        int row = currentResultTabu.getTabuCoords().getRow();
        int col = currentResultTabu.getTabuCoords().getCol();
        tabuArrayPuttingStrategy[row][col] = params.getTabuIterationNumber();

    }

    private void updateMovementFrequencyReplaceCity() {
        int row = currentResultTabu.getTabuCoords().getCol();
        int col = currentResultTabu.getTabuCoords().getRow();
        tabuArrayReplacingStrategy[row][col] += 1;
    }

    private void updateMovementFrequencyPutCityToVehicle() {
        int row = currentResultTabu.getTabuCoords().getRow();
        int col = currentResultTabu.getTabuCoords().getCol();
        freqArrayPuttingStrategy[row][col] += 1;
    }

    private void checkFoundResult(int row, int col, Result resultNew) {
        TabuCoords tabuCoords = new TabuCoords(row, col, neighborhoodStrategy);
        if (!tabuCoords.isSameCoords(lastTabuCoords)) {

            double z = countZ(resultNew.getSum(), tabuCoords);
            if (!isFoundResult) {
                if (isTabu(tabuCoords)) {
                    bestTabu = new ResultTabu(resultNew, z, tabuCoords);
                }
                else {
                    currentResultTabu = new ResultTabu(resultNew, z, tabuCoords);
                    isFoundResult = true;
                }

            }
            else {
                if (currentResultTabu.getZ() > z) {
                    if (isTabu(tabuCoords)) {
                        if (bestTabu.getResult() == null || bestTabu.getZ() > z) {
                            bestTabu = new ResultTabu(resultNew, z, tabuCoords);
                        }

                    } else {
                        currentResultTabu = new ResultTabu(resultNew, z, tabuCoords);
                        isFoundResult = true;
                    }
                }
            }
        }
    }


    private boolean isTabu(TabuCoords tabuCoords) {
        int[][] tabuArray = neighborhoodStrategy == NeighborhoodStrategy.REPLACE_CITIES ?
                this.tabuArrayReplacingStrategy : this.tabuArrayPuttingStrategy;
        int row = tabuCoords.getRow();
        int col = tabuCoords.getCol();
        return tabuArray[row][col] != 0;
    }

    private double countZ(double foundSum, TabuCoords tabuCoords) {

        int[][] tabuArray;
        int row, col;
        if (neighborhoodStrategy == NeighborhoodStrategy.REPLACE_CITIES) {
            tabuArray = this.tabuArrayReplacingStrategy;
            //reverse column and row with tabu array
            row = tabuCoords.getCol();
            col = tabuCoords.getRow();
        }
        else {
            tabuArray = this.freqArrayPuttingStrategy;
            row = tabuCoords.getRow();
            col = tabuCoords.getCol();
        }

        double dx = foundSum - currentResult.getSum();
        if (tabuArray[row][col] == 0) {
            return dx;
        } else if (dx < 0) {
            return dx / tabuArray[row][col];
        } else if (dx > 0) {
            return dx * tabuArray[row][col];
        }
        return 0.0;
    }


}
