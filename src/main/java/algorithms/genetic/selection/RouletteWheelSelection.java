package algorithms.genetic.selection;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.ParametersGenetic;

import java.util.List;


public class RouletteWheelSelection extends Selection {

    public RouletteWheelSelection(List<Individual> population, ParametersGenetic params) {
        super(params);
        this.population = population;
    }

    public void makeSelection() {
        theBest = population.stream().findFirst().orElseThrow();
        setReproductionProbability();
        countCircleSegment();
        selectIndividuals();
        generateIndividualsPairs();
    }

    private void setReproductionProbability() {
        double sumDistance = population.stream().mapToDouble(i -> i.getResult().getSum()).sum();
        population.forEach(p -> p.setReproductionProbability((1 - (p.getResult().getSum() / sumDistance)) / (population.size() - 1)));
    }
}
