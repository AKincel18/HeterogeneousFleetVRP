package algorithms.genetic.selection;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.ParametersGenetic;

import java.util.Comparator;
import java.util.List;

public class RankSelection extends Selection {

    public RankSelection(List<Individual> population, ParametersGenetic params) {
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
        int i = 0;
        double sum = 0.0;
        double sp = params.getSelectivePressure();
        for (int rank = population.size(); rank > 0; rank--) {
            double r = (2 - sp) + ((2 * (sp - 1) * (rank - 1)) / (population.size() - 1));
            sum += r;
            population.get(i).setR(r);
            i++;
        }
        for (Individual ind : population) {
            ind.setReproductionProbability(ind.getR() / sum);
        }
        population.sort(Comparator.comparing(individual -> individual.getResult().getSum()));
    }
}
