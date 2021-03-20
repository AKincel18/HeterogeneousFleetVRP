package algorithms.genetic;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import commons.Result;
import exceptions.IndividualNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static constants.StringConst.INDIVIDUAL_NOT_FOUND;

@RequiredArgsConstructor
public class Selection {

    private final List<Individual> population;
    private List<Individual> selectedIndividuals;
    @Getter private List<PairIndividuals> pairIndividuals;
    @Getter private Individual theBest;

    public void selection() {
        setReproductionProbability();
        countCircleSegment();
        makeSelection();
        generatePairsIndividual();
    }

    private void setReproductionProbability() {
        population.sort(Comparator.comparing(Result::getSum));
        theBest = population.stream().findFirst().orElse(new Individual());
        double sumDistance = population.stream().mapToDouble(Individual::getSum).sum();
        population.forEach(p -> p.setReproductionProbability((1 - (p.getSum() / sumDistance)) / (population.size() - 1)));
    }

    private void countCircleSegment() {
        //Writer.buildTitleOnConsole("Count circle segment");
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        population.forEach(i -> {
            sum.updateAndGet(v -> v + i.getReproductionProbability());
            i.setCircleSegment(Utils.roundNumber(sum.get()));
            //System.out.println(Utils.roundNumber(sum.get()));
        });
    }

    private void makeSelection() {
        //Writer.buildTitleOnConsole("SELECTION NOW");
        selectedIndividuals = new ArrayList<>();
        Random random = new Random();
        try {
            for (int i = 0; i < population.size(); i++) {
                Individual ind = findIndividual(Utils.roundNumber(random.nextDouble()));
                //System.out.println("chosen = " + ind.getCircleSegment() + "; individual id = " + ind.getId()); //for debugging
                selectedIndividuals.add(ind);
            }
        } catch (IndividualNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void generatePairsIndividual() {
        //Writer.buildTitleOnConsole("Generate pairs");
        List<Integer> numbers = Utils.generateListOfNumbers(population.size());

        pairIndividuals = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < population.size(); i+=2) {

            PairIndividuals selectedIndividualPair = new PairIndividuals(
                    selectedIndividuals.get(numbers.get(i)),
                    selectedIndividuals.get(numbers.get(i + 1)),
                    id);
            id++;
            pairIndividuals.add(selectedIndividualPair);
//            System.out.print("ind1 id = " + selectedIndividualPair.getIndividual1().getId());
//            System.out.print("; ind2 id = " + selectedIndividualPair.getIndividual2().getId());
        }

    }

    private Individual findIndividual(double randomValue) throws IndividualNotFoundException {
        //System.out.print("random value = " + randomValue + "; ");
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getCircleSegment() >= randomValue){
                if (i == 0 || i == population.size() -1)
                    return population.get(i);
                else {
                    return population.get(i - 1);
                }
            }
        }
        throw new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND);
    }


}
