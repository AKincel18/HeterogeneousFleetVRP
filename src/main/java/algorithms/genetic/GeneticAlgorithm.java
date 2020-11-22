package algorithms.genetic;

import constants.StringConst;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.Utils.countDistance;

@RequiredArgsConstructor
public class GeneticAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;

    private Map<Vehicle, List<City>> vehiclesRoute = new HashMap<>();

    public void initRoutes() {

        City startAndEndCity = getDepotByCity();
        //init map and add start city
        vehicles.forEach( v -> vehiclesRoute.put(v, new ArrayList<City>(Collections.singletonList(startAndEndCity))));

        initPopulation();

        //add end city
        for (Vehicle vehicle : vehiclesRoute.keySet())
            Objects.requireNonNull(vehiclesRoute.computeIfPresent(vehicle, (key, value) -> vehiclesRoute.get(key))).add(startAndEndCity);

        writeRoutes();


    }

    private City getDepotByCity() {
        return City.builder()
                .coords(depot.getCoords())
                .amount(0.0)
                .name(depot.getName())
                .build();
    }

    private void initPopulation() {

        boolean[] drawVehicle = new boolean[vehicles.size()];
        Random random = new Random();

        AtomicInteger generatedNumberVehicle = new AtomicInteger();
        vehicles.forEach(v -> {

            do {
                generatedNumberVehicle.set(random.nextInt(vehicles.size()));
            } while (drawVehicle[generatedNumberVehicle.get()]);
            drawVehicle[generatedNumberVehicle.get()] = true;

            boolean[] drawCity = new boolean[cities.size()];
            cities.forEach( c -> {
                int generatedNumberCity;
                do {
                    generatedNumberCity = random.nextInt(cities.size());
                } while (drawCity[generatedNumberCity]);
                drawCity[generatedNumberCity] = true;

                Vehicle vehicle = vehicles.get(generatedNumberVehicle.get());
                City city = cities.get(generatedNumberCity);
                if (isPossible(city, vehicle)) {
                    Objects.requireNonNull(vehiclesRoute.computeIfPresent(vehicle, (key, value) -> vehiclesRoute.get(key))).add(city);
                    city.setVisited(true);
                }
            });
        });

    }

    private boolean notVisited() {
        return cities.stream().allMatch(City::isVisited);
    }

    private boolean isPossible(City city, Vehicle vehicle) {

        double vehicleAmount = vehicle.getAmount();
        List<City> routeVehicle = vehiclesRoute.get(vehicle);

        double sumAmount = countRouteAmount(routeVehicle);
        double cityAmount = city.getAmount();

        return vehicleAmount >= sumAmount + cityAmount && !city.isVisited();
    }

    private double countQuality(Vehicle vehicle) {
        double amount = countRouteAmount(vehiclesRoute.get(vehicle));
        return amount / vehicle.getAmount();
    }

    private double countRouteAmount(List<City> routeVehicle) {
        return routeVehicle.stream().mapToDouble(c -> c.getAmount()).sum();
    }


    private double countRouteDistance(Vehicle vehicle) {
        List<City> route = vehiclesRoute.get(vehicle);
        double sumDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            sumDistance+= countDistance(route.get(i).getCoords(), route.get(i + 1).getCoords());
        }
        return sumDistance;
    }

    private void writeRoutes() {

        Utils.buildTitleOnConsole("first population");
        vehiclesRoute.forEach((vehicle, cities) -> {
            System.out.print("Vehicle: " + vehicle.getName() + " = ");
            if (cities.isEmpty())
                System.out.print("No cities ");
            else
                cities.forEach(city -> System.out.print(city.getName() + " "));

            System.out.print("quality = " + countQuality(vehicle));
            System.out.println(", distance = " + countRouteDistance(vehicle));
        });

        System.out.println();
        //todo: some not visited cities -> bug
        System.out.println("not visited cities: ");
        cities.stream().filter(c -> !c.isVisited()).forEach(city -> System.out.print(city.getName() + " "));
    }

}
