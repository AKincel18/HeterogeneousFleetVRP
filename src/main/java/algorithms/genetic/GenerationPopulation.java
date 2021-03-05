package algorithms.genetic;

import algorithms.genetic.model.Individual;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utils.Utils.countRouteAmount;
import static utils.Utils.countSumOfIndividual;

@RequiredArgsConstructor
public class GenerationPopulation {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final int populationSize;

    private Individual individual;
    private final List<Individual> population = new ArrayList<>();

    public List<Individual> initPopulation() {

        City startAndEndCity = Utils.getDepotByCity(depot);

        for (int i = 0; i < populationSize; i++) {
            generateIndividuals(startAndEndCity, i);
            individual.addEndCity();
            population.add(individual);
            individual.setSum(countSumOfIndividual(individual));
        }
        Writer.writePopulation(population);
        return population;
    }

    private void generateIndividuals(City startAndEndCity, int id) {
        individual = new Individual(id);
        boolean isAllCitiesVisited, isEmptyRoute;
        do {
            individual.initMap(vehicles, startAndEndCity);
            cities.forEach(c -> c.setVisited(false));
            List<Integer> vehiclesNumberGenerated = Utils.generateListOfNumbers(vehicles.size());
            for (int i = 0; i < vehicles.size(); i++) {
                List<Integer> citiesNumberGenerated = Utils.generateListOfNumbers(cities.size());
                for (int j = 0; j < cities.size(); j++) {
                    Vehicle vehicle = vehicles.get(vehiclesNumberGenerated.get(i));
                    City city = cities.get(citiesNumberGenerated.get(j));
                    if (isPossible(city, vehicle)) {
                        Objects.requireNonNull(individual.getRoutes().computeIfPresent(vehicle, (key, value) -> individual.getRoutes().get(key)))
                                .add(city);
                        city.setVisited(true);
                    }
                }
            }
            isAllCitiesVisited = cities.stream().allMatch(City::isVisited);
            isEmptyRoute = checkIsEmptyRoute();
        } while (!isAllCitiesVisited || isEmptyRoute);
    }

    /**
     * Check if route is empty
     * @return true if consists only one city (depot), otherwise return false
     */
    private boolean checkIsEmptyRoute() {
        return individual.getRoutes().entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() == 1);
    }

    private boolean isPossible(City city, Vehicle vehicle) {

        double vehicleAmount = vehicle.getAmount();
        List<City> routeVehicle = individual.getRoutes().get(vehicle);

        double sumAmount = countRouteAmount(routeVehicle);
        double cityAmount = city.getAmount();

        return vehicleAmount >= sumAmount + cityAmount && !city.isVisited();
    }

}
