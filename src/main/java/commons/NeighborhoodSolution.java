package commons;

import model.City;
import model.Vehicle;
import utils.Coder;
import utils.Decoder;

import java.util.*;

public abstract class NeighborhoodSolution implements SolutionFromNeighborhood {

    protected final List<City> cities;
    protected final List<Vehicle> vehicles;
    protected final City depotCity;
    protected Random random;
    protected Coder coder;
    protected Decoder decoder;

    public NeighborhoodSolution(List<City> cities, List<Vehicle> vehicles, City depotCity) {
        this.cities = cities;
        this.vehicles = vehicles;
        this.depotCity = depotCity;
        this.random = new Random();
        this.coder = new Coder(depotCity);
        this.decoder = new Decoder(vehicles, cities, depotCity);
    }

    protected Integer[] initArray(Integer[] decodedResult) {
        Integer[] arrayNew = new Integer[decodedResult.length];
        System.arraycopy(decodedResult, 0, arrayNew, 0, decodedResult.length);
        return arrayNew;
    }

    protected Map<Integer, List<Integer>> initMap(Map<Integer, List<Integer>> decodedResult) {
        Map<Integer, List<Integer>> mapNew = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : decodedResult.entrySet()) {
            mapNew.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return mapNew;
    }

//    protected void printMap(Map<Integer, List<Integer>> neighborhoodDecodedResult) {
//        neighborhoodDecodedResult.forEach((k, v) -> System.out.println("vec = " + k + ", route = " + v.toString()));
//    }
//
//    protected void printSwapInfo(Integer[] decodedResult, List<Integer> neighborhoodDecodedResult, int i, int j,
//                                 Integer[] cutPoints, Integer[] updatedCutPoints) {
//        Writer.buildTitleOnConsole("Swapping info");
//        System.out.println(i + "<->" + j);
//        System.out.print("Base = ");
//        Writer.writeDecodedResultInOneRow(Arrays.asList(decodedResult), cutPoints);
//        System.out.print("New  = ");
//        Writer.writeDecodedResultInOneRow(neighborhoodDecodedResult, updatedCutPoints);
//    }

}
