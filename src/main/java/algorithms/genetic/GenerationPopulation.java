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

import static utils.Utils.countSumOfResult;

@RequiredArgsConstructor
public class GenerationPopulation {
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final int populationSize;

    private final List<Individual> population = new ArrayList<>();

    public List<Individual> initPopulation() {

        City startAndEndCity = Utils.getDepotByCity(depot);
        Individual individual;
        for (int i = 0; i < populationSize; i++) {
            individual = new Individual(Utils.generateRandomResult(vehicles, cities, startAndEndCity), startAndEndCity, i);
            //individual.addEndCity();
            population.add(individual);
            individual.setSum(countSumOfResult(individual.getRoutes()));
        }
        return population;
    }

}
