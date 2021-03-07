package utils;

import model.City;
import model.Coords;
import model.Depot;
import model.Vehicle;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    public static double countSumOfResult(Map<Vehicle, List<City>> routes) {

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        routes.forEach((vehicle, cities) -> {
            double routeDistance =  Utils.countRouteDistance(cities);
            sum.updateAndGet(v -> v + routeDistance);
        });
        return roundNumber(sum.get());
    }

    public static Map<Vehicle, List<City>> generateRandomResult(List<Vehicle> vehicles, List<City> cities, City depot) {
        boolean isAllCitiesVisited, isEmptyRoute;
        Map<Vehicle, List<City>> routes = new HashMap<>();

        do {
            vehicles.forEach( v -> routes.put(v, new ArrayList<>(Collections.singletonList(depot))));
            //individual.initMap(vehicles, startAndEndCity);
            cities.forEach(c -> c.setVisited(false));
            List<Integer> vehiclesNumberGenerated = Utils.generateListOfNumbers(vehicles.size());
            for (int i = 0; i < vehicles.size(); i++) {
                List<Integer> citiesNumberGenerated = Utils.generateListOfNumbers(cities.size());
                for (int j = 0; j < cities.size(); j++) {
                    Vehicle vehicle = vehicles.get(vehiclesNumberGenerated.get(i));
                    City city = cities.get(citiesNumberGenerated.get(j));
                    if (checkIsAcceptableWeight(city, vehicle.getAmount(), routes.get(vehicle))) {
                        //Objects.requireNonNull(individual.getRoutes().computeIfPresent(vehicle, (key, value) -> individual.getRoutes().get(key))).add(city);
                        Objects.requireNonNull(routes.computeIfPresent(vehicle, (key, value) -> routes.get(key))).add(city);
                        city.setVisited(true);
                    }
                }
            }
            isAllCitiesVisited = cities.stream().allMatch(City::isVisited);
            isEmptyRoute = checkIsEmptyRoute(routes);
        } while (!isAllCitiesVisited || isEmptyRoute);

        //and depot as last point in the route
        for (Vehicle vehicle : routes.keySet())
            Objects.requireNonNull(routes.computeIfPresent(vehicle, (key, value) -> routes.get(key))).add(depot);

        return routes;
    }

    /**
     * Check if route is empty
     * @param routes routes
     * @return true if consists only one city (depot), otherwise return false
     */
    private static boolean checkIsEmptyRoute(Map<Vehicle, List<City>> routes) {
        return routes.entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() == 1);
    }

    /**
     * Check if can adding city to the given route: if vehicle can visit city with given amount
     * @param city city
     * @param vehicleAmount amount of vehicle
     * @param routeVehicle route
     * @return true if route is OK, false is route is wrong
     */
    private static boolean checkIsAcceptableWeight(City city, Double vehicleAmount, List<City> routeVehicle) {

        //List<City> routeVehicle = individual.getRoutes().get(vehicle);

        double sumAmount = countRouteAmount(routeVehicle);

        return vehicleAmount >= sumAmount + city.getAmount() && !city.isVisited();
    }

    /**
     * Check if route for all vehicles is correct
     * @param routes routes
     * @return true if route is OK, false is route is wrong
     */
    public static boolean checkIsAcceptableWeightAll(Map<Vehicle, List<City>> routes) {
        for (Map.Entry<Vehicle, List<City>> route : routes.entrySet()) {
            double routeAmount = countRouteAmount(route.getValue());
            if (routeAmount > route.getKey().getAmount())
                return false;
        }
        return true;
    }

    public static LinkedHashMap<Vehicle, List<City>> sortRoutesByVehicleId(Map<Vehicle, List<City>> routes) {
        return routes.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
