package algorithms.tabusearch;

import algorithms.tabusearch.model.ParametersTabuSearch;
import algorithms.tabusearch.model.ResultTabu;
import algorithms.tabusearch.model.TabuCoords;
import commons.SolutionFromNeighborhood;
import lombok.Getter;
import lombok.NonNull;
import model.City;
import model.Vehicle;
import utils.Decoder;
import utils.Writer;

import java.util.List;

public class TabuSearchNeighborhoodSolution extends SolutionFromNeighborhood {

    private final ParametersTabuSearch params;
    @Getter private boolean isFoundResult;
    @Getter private final int[][] tabuArray;
    @Getter private ResultTabu currentResultTabu;
    private int vehicle1;
    private int city1;
    private final int sizeTabuArray;
    private boolean isFirstResult;

    public TabuSearchNeighborhoodSolution(List<City> cities, List<Vehicle> vehicles,
                                          City depotCity, @NonNull ResultTabu currentResultTabu,
                                          ParametersTabuSearch params) {
        super(cities, vehicles, depotCity, currentResultTabu.getResult());
        this.params = params;
        this.currentResultTabu = currentResultTabu;
        this.sizeTabuArray = vehicles.size() * cities.size();
        tabuArray = new int[sizeTabuArray][sizeTabuArray];
    }

    public void findSolutionFromNeighborhood() {
        isFirstResult = true;
        isFoundResult = false;
        currentDecodedResult = new Decoder(cities).decodeResult(currentResultTabu.getResult().getRoutes());

        //Writer.buildTitleOnConsole("Base result");
        //Writer.writeDecodedResultInOneRow(currentDecodedResult);

        for (vehicle1 = 0; vehicle1 < vehicles.size(); vehicle1++) {
            findInTheSameVehicle(vehicle1);
            for (city1 = 0; city1 < cities.size(); city1++) {
                int visitOrder = currentDecodedResult[vehicle1][city1];
                if (visitOrder != 0) {
                    findInOtherVehicles(currentDecodedResult, visitOrder, vehicle1, city1);
                }
            }
        }

        //update tabuArray
        if (isFoundResult) {
            //Writer.buildTitleOnConsole("Found result:");
            //Writer.writeResult(currentResultTabu.getResult());
            //System.out.println(currentResultTabu.getTabuCoords());
            //Writer.buildTitleOnConsole("Before update");
            //Writer.writeTabuArray(tabuArray, vehicles.size(), cities.size());
            updateTabu();
            updateMovementFrequency();
            //Writer.buildTitleOnConsole("After update");
            //Writer.writeTabuArray(tabuArray, vehicles.size(), cities.size());
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
                        checkFoundResult(vehicle, vehicle, city1, city2);
                    }
                }
            }

        }
    }

    private void updateTabu() {
        //decrease tabu part of array
        for (int i = 0; i < sizeTabuArray; i++) {
            for (int j = i + 1; j < sizeTabuArray; j++) {
                if (tabuArray[i][j] != 0)
                    tabuArray[i][j] -= 1;
            }
        }

        //set tabu number to exchanged position
        int row = currentResultTabu.getTabuCoords().getRowTabu();
        int col = currentResultTabu.getTabuCoords().getColumnTabu();
        tabuArray[row][col] = params.getTabuIterationNumber();

    }

    private void updateMovementFrequency() {
        int row = currentResultTabu.getTabuCoords().getColumnTabu();
        int col = currentResultTabu.getTabuCoords().getRowTabu();
        tabuArray[row][col] += 1;
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
                        checkFoundResult(this.vehicle1, vehicle2, this.city1, city2);

                        // replaced higher number from one vehicle route to another vehicle route
                        // where higher number is one less then replaced vehicle route
                        // must be replaced on the same position
                    } else if (city2 == city1 &&
                            isMaxVisitOrder(decodedResult[vehicle2], visitOrder) &&
                            isMaxVisitOrderAnalyzed(decodedResult[vehicle1], visitOrder)) {
                        exchangeZero(vehicle1, city1, vehicle2, city2, visitOrder);
                        checkFoundResult(this.vehicle1, vehicle2, this.city1, city2);
                    }
                }
            }
        }
    }

    private void checkFoundResult(int vehicle1, int vehicle2, int city1, int city2) {
        ResultTabu result = new ResultTabu(getEncodedResult());
        //System.out.println("Pairs: ");
//        System.out.print("(" + vehicle1 + "," + city1 + ") <-> ");
//        System.out.println("(" + vehicle2 + "," + city2 + ") ");
        if (result.getResult() != null) {

            TabuCoords tabuCoords = new TabuCoords(vehicle1, city1, vehicle2, city2, cities.size());
            double z = countZ(result.getResult().getSum(), tabuCoords);
            if (isFirstResult) {
                currentResultTabu = result;
                currentResultTabu.setArgs(z, tabuCoords);
                isFoundResult = true;
                isFirstResult = false;
            } else {
                //best result
                if (result.getResult().getSum() < currentResultTabu.getResult().getSum()) {
                    currentResultTabu = result;
                    currentResultTabu.setArgs(z, tabuCoords);
                    isFoundResult = true;
                } else if (!isTabu(tabuCoords) && currentResultTabu.getZ() > z) {
                    currentResultTabu = result;
                    currentResultTabu.setArgs(z, tabuCoords);
                    isFoundResult = true;
                }
            }
        }
    }


    private boolean isTabu(TabuCoords tabuCoords) {
        int row = tabuCoords.getRowTabu();
        int col = tabuCoords.getColumnTabu();
        return tabuArray[row][col] != 0;
    }

    private double countZ(double foundSum, TabuCoords tabuCoords) {

        //reverse column and row with tabu array
        int row = tabuCoords.getColumnTabu();
        int col = tabuCoords.getRowTabu();
        double dx = foundSum - currentResultTabu.getResult().getSum();
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
