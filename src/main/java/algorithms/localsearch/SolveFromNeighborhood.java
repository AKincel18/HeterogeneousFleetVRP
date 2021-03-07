package algorithms.localsearch;

import algorithms.localsearch.model.LocalSearchMethod;
import commons.Result;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Decoder;
import utils.Encoder;
import utils.Writer;

import java.util.Arrays;
import java.util.List;

import static utils.Utils.checkIsAcceptableWeightAll;

@RequiredArgsConstructor
public class SolveFromNeighborhood {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depotCity;
    private final LocalSearchMethod method;
    @NonNull @Getter private Result currentResult;

    private Integer[][] currentDecodedResult;
    private Integer[][] tmpDecodedResult;
    @Getter private boolean isFoundBetterResult;
    private Result bestNeighborhoodResult;

    public void findSolveFromNeighborhood() {

        isFoundBetterResult = false;
        currentDecodedResult =  new Decoder(cities).decodeResult(currentResult.getRoutes());
        bestNeighborhoodResult = new Result(currentResult);

        Writer.buildTitleOnConsole("Base result");
        Writer.writeDecodedResultInOneRow(currentDecodedResult);
        //Writer.buildTitleOnConsole("DECODED RESULT");
        //Writer.writeDecodedResultInOneRow(currentDecodedResult);

        for (int vehiclePos = 0; vehiclePos < vehicles.size(); vehiclePos++) {
            for (int cityPos = 0; cityPos < cities.size(); cityPos++) {
                int visitOrder = currentDecodedResult[vehiclePos][cityPos];
                if (visitOrder != 0) {
                    findInOtherVehicles(currentDecodedResult, visitOrder, vehiclePos, cityPos);
                    if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                        System.out.println("STOP, FOUND BETTER SOLUTION");
                        currentResult = bestNeighborhoodResult;
                        return;
                    }
                }
            }
        }

        if (isFoundBetterResult)
            currentResult = bestNeighborhoodResult;
    }

    private void findInOtherVehicles(Integer[][] decodedResult, int visitOrder,
                                     int analyzedVehiclePos, int analyzedCityPos) {
        for (int vehiclePos = 0; vehiclePos < vehicles.size(); vehiclePos++) {
            //not find in the same vehicle
            if (analyzedVehiclePos != vehiclePos) {
                for (int cityPos = 0; cityPos < cities.size(); cityPos++) {
                    int foundVisitOrder = decodedResult[vehiclePos][cityPos];
                    //find the same visit order and in vehicle with higher id (with prev id was searched before)
                    if (foundVisitOrder == visitOrder && vehiclePos > analyzedVehiclePos) {
                        exchange(visitOrder, vehiclePos, cityPos, analyzedVehiclePos, analyzedCityPos);
                        checkNewResult();
                        if (method == LocalSearchMethod.GREEDY && isFoundBetterResult) {
                            System.out.println("STOP, FOUND BETTER SOLUTION");
                            return;
                        }
                    // replaced higher number from one vehicle route to another vehicle route
                    // where higher number is one less then replaced vehicle route
                    // must be replaced on the same position
                    } else if (cityPos == analyzedCityPos &&
                            isMaxVisitOrder(decodedResult[vehiclePos], visitOrder) &&
                            isMaxVisitOrderAnalyzed(decodedResult[analyzedVehiclePos], visitOrder)) {
                        exchangeZero(visitOrder, vehiclePos, cityPos, analyzedVehiclePos, analyzedCityPos);
                        checkNewResult();
                    }
                }
            }
        }
    }

    private void checkNewResult() {
        Result newResult = new Encoder(cities, tmpDecodedResult).encodeResult(vehicles, depotCity);
        Writer.buildTitleOnConsole("NEW RESULT");
        Writer.writeResult(newResult);
        System.out.println("CURRENT BEST RESULT = " + bestNeighborhoodResult.getSum());
        //if new result is better than old and routes are correct
        if (newResult.getSum() < bestNeighborhoodResult.getSum() && checkIsAcceptableWeightAll(newResult.getRoutes())) {
            Writer.buildTitleOnConsole("NEW RESULT FOUND!!!");
            bestNeighborhoodResult = newResult;
            isFoundBetterResult = true;
        }
        else {
            System.out.println("NEW RESULT REJECTED");
        }
    }

    private void exchange(int visitOrder, int vehiclePos, int cityPos,
                          int analyzedVehiclePos, int analyzedCityPos) {

        //newDecodedResult = Arrays.stream(decodedResult).toArray(Integer[][]::new);
        //newDecodedResult = Arrays.copyOf(decodedResult, decodedResult.length);
        initTmpDecodedResult();
        tmpDecodedResult[analyzedVehiclePos][analyzedCityPos] = 0;
        tmpDecodedResult[analyzedVehiclePos][cityPos] = visitOrder;

        tmpDecodedResult[vehiclePos][cityPos] = 0;
        tmpDecodedResult[vehiclePos][analyzedCityPos] = visitOrder;

        Writer.buildTitleOnConsole("AFTER EXCHANGE: ");
        Writer.writeDecodedResultInOneRow(tmpDecodedResult);


    }

    private void exchangeZero(int visitOrder, int vehiclePos, int cityPos,
                              int analyzedVehiclePos, int analyzedCityPos) {
        initTmpDecodedResult();
        tmpDecodedResult[vehiclePos][cityPos] = visitOrder;

        tmpDecodedResult[analyzedVehiclePos][analyzedCityPos] = 0;

        Writer.buildTitleOnConsole("AFTER EXCHANGE ZERO: ");
        Writer.writeDecodedResultInOneRow(tmpDecodedResult);
    }

    private boolean isMaxVisitOrder(Integer[] vehicleRoute, Integer visitOrder) {
        int max = Arrays.stream(vehicleRoute).max(Integer::compareTo).orElse(0);
        return visitOrder == max + 1;
    }

    private boolean isMaxVisitOrderAnalyzed(Integer[] vehicleRoute, Integer visitOrder) {
        int max = Arrays.stream(vehicleRoute).max(Integer::compareTo).orElse(0);
        return max == visitOrder;
    }

    private void initTmpDecodedResult() {
        tmpDecodedResult = new Integer[vehicles.size()][cities.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            System.arraycopy(currentDecodedResult[i], 0, tmpDecodedResult[i], 0, cities.size());
        }
    }
}
