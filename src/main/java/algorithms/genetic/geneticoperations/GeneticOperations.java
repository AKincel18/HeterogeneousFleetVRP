package algorithms.genetic.geneticoperations;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import algorithms.genetic.model.PairIndividualsDecode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Decoder;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GeneticOperations {
    private final List<PairIndividuals> pairIndividuals;
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depot;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final double crossoverRepeatingNumber;

    @Getter
    private List<Individual> newPopulation;

    private int currentId = 0;

    public void geneticOperations() {
        newPopulation = new ArrayList<>();
        Decoder decoder = new Decoder(cities);
        Crossover crossover = new Crossover(cities, vehicles, depot, crossoverProbability,
                mutationProbability, crossoverRepeatingNumber);
        pairIndividuals.forEach(p -> {
                    if (crossover.isCrossover()) {
                        crossover.setCurrentPair(p);
                        p.setPairIndividualsDecode(new PairIndividualsDecode(
                                decoder.decodeResult(p.getIndividual1().getRoutes()),
                                decoder.decodeResult(p.getIndividual2().getRoutes())
                        ));
                        crossover.startCrossover(p.getPairIndividualsDecode());
                        addIndividualToNewPopulation(crossover.getBest1Individual());
                        addIndividualToNewPopulation(crossover.getBest2Individual());
                    } else {
                        addIndividualToNewPopulation(p.getIndividual1());
                        addIndividualToNewPopulation(p.getIndividual2());
                    }
                }
        );
    }

    private void addIndividualToNewPopulation(Individual best) {
        best.setId(currentId);
        currentId++;
        newPopulation.add(best);
    }

}
