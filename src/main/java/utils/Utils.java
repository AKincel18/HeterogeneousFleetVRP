package utils;

import model.City;
import model.Coords;
import model.Depot;
import model.Vehicle;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    /**
     * https://www.geeksforgeeks.org/program-distance-two-points-earth/
     *
     * @param c1 coords1
     * @param c2 coords2
     * @return distance between two coords earth (in kilometers)
     */
    public static Double countDistance(Coords c1, Coords c2) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        double lon1 = Math.toRadians(c1.getLongitude());
        double lon2 = Math.toRadians(c2.getLongitude());
        double lat1 = Math.toRadians(c1.getLatitude());
        double lat2 = Math.toRadians(c2.getLatitude());

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    public static List<Integer> generateListOfNumbers(int size) {
        List<Integer> numbers = IntStream.rangeClosed(0, size - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
        return numbers;
    }

    /**
     * Round double number up to two decimal places
     *
     * @param number number to rounded
     * @return rounded number
     */
    public static double roundNumber(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    /**
     * count quality of the route
     *
     * @param vehicle vehicle
     * @param cities  route
     * @return quality
     */
    public static double countQuality(Vehicle vehicle, List<City> cities) {
        double amount = countRouteAmount(cities);
        double quality = amount / vehicle.getAmount();
        return Utils.roundNumber(quality);
    }

    public static double countRouteAmount(List<City> routeVehicle) {
        return routeVehicle.stream().mapToDouble(c -> c.getAmount()).sum();
    }

    public static double countRouteDistance(List<City> route) {
        double sumDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            sumDistance += countDistance(route.get(i).getCoords(), route.get(i + 1).getCoords());
        }
        return Utils.roundNumber(sumDistance);
    }

    public static City getDepotByCity(Depot depot) {
        return City.builder()
                .coords(depot.getCoords())
                .amount(0.0)
                .name(depot.getName())
                .build();
    }
}
