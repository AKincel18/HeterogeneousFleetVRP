package algorithms.genetic;

import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static utils.Utils.countRouteAmount;

@RequiredArgsConstructor
public class GeneticAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final int populationSize;


    private Individual individual;
    private final List<Individual> population = new ArrayList<>();
    private Selection selection;


    public void start() {
        initPopulation();
        selection();
        crossover();
        Writer.writeTestDecodingAndEncoding(population, cities, vehicles.size(), depot);
    }


    private void initPopulation() {

        City startAndEndCity = Utils.getDepotByCity(depot);

        for (int i = 0; i < populationSize; i++) {
            generateIndividuals(startAndEndCity, i);
            individual.addEndCity();
            population.add(individual);
            countSumOfIndividual();
        }
        Writer.writePopulation(population);
    }

    private void selection() {
        Writer.buildTitleOnConsole("Selection");
        selection = new Selection(population, cities.size());
        selection.createRanking();
    }

    private void crossover() {
        Writer.buildTitleOnConsole("Crossover");
        new GeneticOperations(selection.getPairIndividuals(), cities).crossover();
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
                        Objects.requireNonNull(individual.getIndividual().computeIfPresent(vehicle, (key, value) -> individual.getIndividual().get(key)))
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
        return individual.getIndividual().entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() == 1);
    }

    private boolean isPossible(City city, Vehicle vehicle) {

        double vehicleAmount = vehicle.getAmount();
        List<City> routeVehicle = individual.getIndividual().get(vehicle);

        double sumAmount = countRouteAmount(routeVehicle);
        double cityAmount = city.getAmount();

        return vehicleAmount >= sumAmount + cityAmount && !city.isVisited();
    }

    private void countSumOfIndividual() {

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        individual.getIndividual().forEach((vehicle, cities) -> {
            double routeDistance =  Utils.countRouteDistance(cities);
            sum.updateAndGet(v -> v + routeDistance);
        });
        individual.setSum(sum.get());
    }

}
