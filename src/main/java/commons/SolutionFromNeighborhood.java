package commons;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;
import utils.Encoder;

import java.util.Arrays;
import java.util.List;

import static utils.Utils.checkIsAcceptableWeightAll;

@RequiredArgsConstructor
public abstract class SolutionFromNeighborhood implements NeighborhoodSolution {

    protected final List<City> cities;
    protected final List<Vehicle> vehicles;
    protected final City depotCity;
    @NonNull @Getter @Setter protected Result currentResult;

    protected Integer[][] currentDecodedResult;
    protected Result bestNeighborhoodResult;
    protected boolean isFoundSolutionInNeighborhood;

    private Integer[][] tmpDecodedResult;

    protected boolean checkNewResultLocalSearch(int vehicle1, int vehicle2, int city1, int city2) {
        Result foundResult = new Encoder(vehicles, cities, depotCity, tmpDecodedResult).encodeResult();
        //Writer.buildTitleOnConsole("NEW RESULT");
        //Writer.writeResult(foundResult);
        //System.out.println("CURRENT BEST RESULT = " + bestNeighborhoodResult.getSum());
        //if new result is better than old and routes are correct
        if (foundResult.getSum() < bestNeighborhoodResult.getSum() && checkIsAcceptableWeightAll(foundResult.getRoutes())) {
//            Writer.buildTitleOnConsole("Found result");
//            Writer.writeDecodedResultInOneRow(currentDecodedResult);
//            Writer.writeDecodedResultInOneRow(tmpDecodedResult);
//            System.out.print("(" + vehicle1 + "," + city1 +") <-> ");
//            System.out.println("(" + vehicle2 + "," + city2 +") ");
            bestNeighborhoodResult = foundResult;
            isFoundSolutionInNeighborhood = true;
            return true;
        }
        else {
//            Writer.buildTitleOnConsole("Reject result");
//            Writer.writeDecodedResultInOneRow(currentDecodedResult);
//            Writer.writeDecodedResultInOneRow(tmpDecodedResult);
//            System.out.print("(" + vehicle1 + "," + city1 +") <-> ");
//            System.out.println("(" + vehicle2 + "," + city2 +") ");
        }
        return false;
    }

    protected boolean checkNewResultSimulatedAnnealing() {
        Result foundResult = new Encoder(vehicles, cities, depotCity, tmpDecodedResult).encodeResult();
        //Writer.buildTitleOnConsole("NEW RESULT");
        //Writer.writeResult(foundResult);
        if (checkIsAcceptableWeightAll(foundResult.getRoutes())) {
            //Writer.buildTitleOnConsole("NEW RESULT FOUND!!!");
            bestNeighborhoodResult = foundResult;
            return true;
        }
//        else {
//            //System.out.println("NO ACCEPTABLE SOLUTION");
//        }
        return false;
    }

    /**
     * Get encoded result
     * @return encoded result, if result is not acceptable return null
     */
    protected Result getEncodedResult() {

        Result result = new Encoder(vehicles, cities, depotCity, tmpDecodedResult).encodeResult();
        if (checkIsAcceptableWeightAll(result.getRoutes())) {
//            Writer.buildTitleOnConsole("Found result");
//            Writer.writeDecodedResultInOneRow(currentDecodedResult);
//            Writer.writeDecodedResultInOneRow(tmpDecodedResult);
            //System.out.println("Found");
            return result;
        }
        //System.out.println("Not acceptable");
//        Writer.buildTitleOnConsole("Reject result");
//        Writer.writeDecodedResultInOneRow(currentDecodedResult);
//        Writer.writeDecodedResultInOneRow(tmpDecodedResult);
        return null;
    }

    protected void exchange(int vehicle, int city, int vehicle2, int city2, int visitOrder) {

        //newDecodedResult = Arrays.stream(decodedResult).toArray(Integer[][]::new);
        //newDecodedResult = Arrays.copyOf(decodedResult, decodedResult.length);
        initTmpDecodedResult();
        tmpDecodedResult[vehicle][city] = 0;
        tmpDecodedResult[vehicle][city2] = visitOrder;

        tmpDecodedResult[vehicle2][city2] = 0;
        tmpDecodedResult[vehicle2][city] = visitOrder;

//        Writer.buildTitleOnConsole("AFTER EXCHANGE: ");
//        Writer.writeDecodedResultInOneRow(tmpDecodedResult);


    }

    protected void exchangeZero(int vehicle1, int city1, int vehicle2, int city2, int visitOrder) {
        initTmpDecodedResult();
        tmpDecodedResult[vehicle2][city2] = visitOrder;

        tmpDecodedResult[vehicle1][city1] = 0;

        //Writer.buildTitleOnConsole("AFTER EXCHANGE ZERO: ");
        //Writer.writeDecodedResultInOneRow(tmpDecodedResult);
    }

    protected void exchangeSameVehicle(int vehicle, int city1, int city2, int visitOrder1, int visitOrder2) {
        initTmpDecodedResult();
        tmpDecodedResult[vehicle][city1] = visitOrder2;
        tmpDecodedResult[vehicle][city2] = visitOrder1;
    }

    protected boolean isMaxVisitOrder(Integer[] vehicleRoute, Integer visitOrder) {
        int max = Arrays.stream(vehicleRoute).max(Integer::compareTo).orElse(0);
        return visitOrder == max + 1;
    }

    protected boolean isMaxVisitOrderAnalyzed(Integer[] vehicleRoute, Integer visitOrder) {
        int max = Arrays.stream(vehicleRoute).max(Integer::compareTo).orElse(0);
        return max == visitOrder;
    }

    protected void initTmpDecodedResult() {
        tmpDecodedResult = new Integer[vehicles.size()][cities.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            System.arraycopy(currentDecodedResult[i], 0, tmpDecodedResult[i], 0, cities.size());
        }
    }
}
