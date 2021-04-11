package algorithms.genetic;

import algorithms.genetic.model.Individual;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GenerationPopulation {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final int populationSize;

    private final List<Individual> population = new ArrayList<>();

    public List<Individual> initPopulation() {

        City depotCity = Utils.getDepotByCity(depot);
        Individual individual;
        for (int i = 0; i < populationSize; i++) {
            individual = new Individual(i, depotCity, Utils.generateRandomResult(vehicles, cities, depotCity));
            population.add(individual);
        }
        return population;
    }

}
