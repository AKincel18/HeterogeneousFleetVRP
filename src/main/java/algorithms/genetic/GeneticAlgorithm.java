package algorithms.genetic;

import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Decoder;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static utils.Utils.countDistance;

@RequiredArgsConstructor
public class GeneticAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final int populationSize;


    private Individual individual;
    private List<Individual> population = new ArrayList<>();
    private Boolean[][] populationDecode;


    public void initPopulation() {

        City startAndEndCity = getDepotByCity();

        for (int i = 0; i < populationSize; i++) {
            //init map and add start city
            individual = new Individual();
            individual.initMap(vehicles, startAndEndCity);
            //vehicles.forEach( v -> individual.put(v, new ArrayList<City>(Collections.singletonList(startAndEndCity))));
            generateIndividuals();

            //add end city

            individual.addEndCity();
            population.add(individual);
            countSumOfIndividual();

            //todo: some not visited cities -> bug
            if (cities.stream().anyMatch(c -> !c.isVisited())) {
                Utils.buildTitleOnConsole("ERROR, some cities have been not visited");
                cities.stream().filter(c -> !c.isVisited()).forEach(city -> System.out.print(city.getName() + " "));
            }

            cities.forEach(c -> c.setVisited(false));
        }


        writePopulation();
        selection();
        for (int i =0; i < populationSize; i++)
            writeTestDecoding(i);


    }

    private void selection() {
        Utils.buildTitleOnConsole("SELECTION");
        Selection selection = new Selection(population, cities.size(), vehicles.size());
        selection.createRanking();

        crossover(selection.getPairIndividuals());
    }
    private void crossover(List<PairIndividuals> pairIndividuals) {
        Utils.buildTitleOnConsole("Crossover");
        GeneticOperations geneticOperations = new GeneticOperations(pairIndividuals, cities);
        geneticOperations.crossover();

    }

    private City getDepotByCity() {
        return City.builder()
                .coords(depot.getCoords())
                .amount(0.0)
                .name(depot.getName())
                .build();
    }

    private void generateIndividuals() {
        List<Integer> vehiclesNumberGenerated = Utils.generateListOfNumbers(vehicles.size());
        for (int i = 0; i < vehicles.size(); i++) {
            List<Integer> citiesNumberGenerated = Utils.generateListOfNumbers(cities.size());
            for (int j = 0; j < cities.size(); j++) {
                Vehicle vehicle = vehicles.get(vehiclesNumberGenerated.get(i));
                City city = cities.get(citiesNumberGenerated.get(j));
                if (isPossible(city, vehicle)) {
                    Objects.requireNonNull(individual.getIndividual().computeIfPresent(vehicle, (key, value) -> individual.getIndividual().get(key))).add(city);
                    city.setVisited(true);
                }
            }
        }

    }

    private boolean notVisited() {
        return cities.stream().allMatch(City::isVisited);
    }

    private boolean isPossible(City city, Vehicle vehicle) {

        double vehicleAmount = vehicle.getAmount();
        List<City> routeVehicle = individual.getIndividual().get(vehicle);

        double sumAmount = countRouteAmount(routeVehicle);
        double cityAmount = city.getAmount();

        return vehicleAmount >= sumAmount + cityAmount && !city.isVisited();
    }

    private double countQuality(Vehicle vehicle) {
        double amount = countRouteAmount(individual.getIndividual().get(vehicle));
        return amount / vehicle.getAmount();
    }

    private double countRouteAmount(List<City> routeVehicle) {
        return routeVehicle.stream().mapToDouble(c -> c.getAmount()).sum();
    }


    private double countRouteDistance(Vehicle vehicle) {
        List<City> route = individual.getIndividual().get(vehicle);
        double sumDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            sumDistance+= countDistance(route.get(i).getCoords(), route.get(i + 1).getCoords());
        }
        return sumDistance;
    }

    private void countSumOfIndividual() {

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        individual.getIndividual().forEach((vehicle, cities) -> {
            double routeDistance =  countRouteDistance(vehicle);
            sum.updateAndGet(v -> v + routeDistance);
        });
        individual.setSum(sum.get());
    }

    private void writePopulation() {
        AtomicInteger index = new AtomicInteger();
        population.forEach(
                p -> {
                    Utils.buildTitleOnConsole("Generated " + index.getAndIncrement() + " individual");
                    p.getIndividual().forEach((vehicle, cities) -> {


                        System.out.print("Vehicle: " + vehicle.getName() + " = ");
                        if (cities.isEmpty())
                            System.out.print("No cities ");
                        else
                            cities.forEach(city -> System.out.print(city.getName() + " "));
                        System.out.print("quality = " + countQuality(vehicle));
                        System.out.println(", distance = " + countRouteDistance(vehicle));

                    });
                    System.out.println("Sum of distance = " + p.getSum());
                }

                );

    }

    private void writeTestDecoding(int numberPopulation) {
        Individual individual = population.get(numberPopulation);
        Decoder decoder = new Decoder(cities);
        Integer [][] array = decoder.decodeIndividual(individual.getIndividual());

        Utils.buildTitleOnConsole("Test decoding " + numberPopulation);
        for (int i =0; i < 3; i++) {
            System.out.println("Vehicle = " + individual.getIndividual().keySet().toArray()[i].toString());
            for (int j = 0; j < cities.size(); j++) {
                System.out.print(array[i][j] + ";");
            }
            System.out.println();

        }
    }

}
