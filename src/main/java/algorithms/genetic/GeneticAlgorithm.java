package algorithms.genetic;

import algorithms.genetic.geneticoperations.GeneticOperations;
import algorithms.genetic.model.Individual;
import algorithms.genetic.model.ParametersGenetic;
import algorithms.genetic.selection.RankSelection;
import algorithms.genetic.selection.RouletteWheelSelection;
import algorithms.genetic.selection.Selection;
import algorithms.genetic.selection.TournamentSelection;
import commons.algorithms.Algorithm;
import commons.algorithms.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Depot;
import model.Vehicle;

import java.util.Comparator;
import java.util.List;

import static utils.Utils.getCityByDepot;

@RequiredArgsConstructor
public class GeneticAlgorithm implements Algorithm {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final Depot depot;
    private final ParametersGenetic params;

    private List<Individual> population;
    private Selection selection;

    @Getter private Result result;

    public void start() {
        population = new GenerationPopulation(cities, vehicles, depot, params.getPopulationSize()).initPopulation();
        population.sort(Comparator.comparing(individual -> individual.getResult().getSum()));

        initSelectionMethod();
        for (int i = 0; i < params.getIterationNumber(); i++) {
            selection();
            geneticOperations();
        }
        population.sort(Comparator.comparing(individual -> individual.getResult().getSum()));
        Individual theBest = population.stream().findFirst().orElseThrow();

        result = theBest.getResult();
    }

    private void initSelectionMethod() {
        switch (params.getSelectionMethod()) {
            case ROULETTE_WHEEL:
                selection = new RouletteWheelSelection(population, params);
                break;
            case RANK:
                selection = new RankSelection(population, params);
                break;
            case TOURNAMENT:
                selection = new TournamentSelection(population, params);
                break;
        }
    }

    private void selection() {
        population.sort(Comparator.comparing(individual -> individual.getResult().getSum()));
        selection.setPopulation(population);
        selection.makeSelection();
    }

    private void geneticOperations() {
        GeneticOperations geneticOperations = new GeneticOperations(selection.getPairIndividuals(), cities, vehicles,
                getCityByDepot(depot), params, selection.getTheBest());
        geneticOperations.geneticOperations();
        population = geneticOperations.getPopulationNew();
    }

}
