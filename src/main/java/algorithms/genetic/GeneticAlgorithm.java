package algorithms.genetic;

import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class GeneticAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;

    private Map<Vehicle, List<City>> vehiclesRoute = new HashMap<>();

    public void initRoutes() {
        vehicles.forEach( v -> vehiclesRoute.put(v, new ArrayList<>()));

        initPopulation();

        vehiclesRoute.forEach((vehicle, cities) -> {
            System.out.print("Vehicle: " + vehicle.getName() + " = ");
            if (cities.isEmpty())
                System.out.print("No cities ");
            else
                cities.forEach(city -> System.out.print(city.getName() + " "));

            System.out.println(countQuality(vehicle));
        });

        System.out.println();
        //todo: some not visited cities -> bug
        System.out.println("not visited cities: ");
        cities.stream().filter(c -> !c.isVisited()).forEach(city -> System.out.print(city.getName() + " "));


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


}
