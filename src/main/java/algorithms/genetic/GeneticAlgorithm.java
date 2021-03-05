package algorithms.genetic;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.Parameters;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;
import utils.Writer;

import java.util.List;

import static utils.Utils.getDepotByCity;

@RequiredArgsConstructor
public class GeneticAlgorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final Parameters params;


    private List<Individual> population;
    private Selection selection;


    public void start() {
        generatePopulation();
        for (int i = 0; i < params.getIterationNumber(); i++) {
            Writer.buildTitleOnConsole("ITERATION = " + i);
            selection();
            geneticOperations();
            //Writer.writeTestDecodingAndEncoding(population, cities, vehicles, depot);
        }
    }

    private void generatePopulation() {
        population = new GenerationPopulation(cities, vehicles, depot, params.getPopulationSize()).initPopulation();
    }


    private void selection() {
        Writer.buildTitleOnConsole("Selection");
        selection = new Selection(population, cities.size());
        selection.createRanking();
    }

    private void geneticOperations() {
        Writer.buildTitleOnConsole("Crossover");
        GeneticOperations geneticOperations = new GeneticOperations(selection.getPairIndividuals(), cities, vehicles,
                getDepotByCity(depot), params.getCrossoverProbability(), params.getMutationProbability());
        geneticOperations.crossoverAndMutation();
        population = geneticOperations.getNewPopulation();
        Writer.buildTitleOnConsole("New population");
        Writer.writePopulation(population);
    }

}
