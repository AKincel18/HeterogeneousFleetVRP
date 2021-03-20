package algorithms.genetic.geneticoperations;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import algorithms.genetic.model.PairIndividualsDecode;
import algorithms.genetic.model.ParametersGenetic;
import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Decoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class GeneticOperations {
    private final List<PairIndividuals> pairIndividuals;
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depot;
    private final ParametersGenetic params;
    private final Individual theBestOld;

    @Getter
    private List<Individual> populationNew;

    private int currentId = 0;

    public void geneticOperations() {
        populationNew = new ArrayList<>();
        Decoder decoder = new Decoder(cities);
        Crossover crossover = new Crossover(cities, vehicles, depot, params);
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
        exclusivity();
    }

    private void addIndividualToNewPopulation(Individual best) {
        best.setId(currentId);
        currentId++;
        populationNew.add(best);
    }

    private void exclusivity() {
        populationNew.sort(Comparator.comparing(Result::getSum));
        Individual theBestNew = populationNew.stream().findFirst().orElse(new Individual());
        if (theBestOld.getSum() < theBestNew.getSum()) {
            populationNew.remove(params.getPopulationSize() - 1); //remove the worst individual
            populationNew.add(theBestOld); //add the best in the previous population
        }
    }

}
