package algorithms.genetic.selection;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import algorithms.genetic.model.ParametersGenetic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static utils.Utils.generateListOfNumbers;

@RequiredArgsConstructor
abstract public class Selection {

    protected final List<Individual> population;
    protected final ParametersGenetic params;

    protected List<Individual> selectedIndividuals;
    @Getter private List<PairIndividuals> pairIndividuals;
    @Getter protected Individual theBest;

    public abstract void makeSelection();


    protected void countCircleSegment() {
        //Writer.buildTitleOnConsole("Count circle segment");
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        population.forEach(i -> {
            sum.updateAndGet(v -> v + i.getReproductionProbability());
            i.setCircleSegment(Utils.roundNumber(sum.get()));
            //System.out.println("id = " + i.getId() + ", circle element = " + Utils.roundNumber(sum.get()));
        });
    }

    protected void selectIndividuals() {
        //Writer.buildTitleOnConsole("SELECTION NOW");
        selectedIndividuals = new ArrayList<>();
        Random random = new Random();
        HashMap<Individual, Integer> stats = new HashMap<>();
        for (int i = 0; i < population.size(); i++) {
            double drawNumber = Utils.roundNumber(random.nextDouble());
            Individual ind = findSelectedIndividual(Utils.roundNumber(drawNumber));
            if (stats.get(ind) != null) {
                Integer prev = stats.get(ind);
                stats.put(ind, prev + 1);
            } else {
                stats.put(ind, 1);
            }
            //System.out.println("Draw number = " + drawNumber + ", chosen = " + ind.getCircleSegment() +
                 //   ", individual id = " + ind.getId()); //for debugging
            selectedIndividuals.add(ind);
        }

        //Writer.buildTitleOnConsole("Stats");
//        stats = stats.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
//                Map.Entry::getKey, Map.Entry::getValue,
//                (oldValue, newValue) -> oldValue, LinkedHashMap::new)
//        );
        //stats.forEach((ind, number) -> System.out.println("ind " + ind.getId() + " = " + number));

    }

    protected void generateIndividualsPairs() {
        //Writer.buildTitleOnConsole("Generate pairs");
        List<Integer> numbers = generateListOfNumbers(population.size());

        pairIndividuals = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < population.size(); i += 2) {

            PairIndividuals selectedIndividualPair = new PairIndividuals(
                    selectedIndividuals.get(numbers.get(i)),
                    selectedIndividuals.get(numbers.get(i + 1)),
                    id);
            id++;
            pairIndividuals.add(selectedIndividualPair);
            //System.out.println(selectedIndividualPair.getIndividual1().getId() + " - " + selectedIndividualPair.getIndividual2().getId());
        }

    }

    private Individual findSelectedIndividual(double randomValue) {
        //System.out.print("random value = " + randomValue + "; ");
        double leftRange = 0.0, rightRange;
        for (Individual individual : population) {
            rightRange = individual.getCircleSegment();

            if (leftRange <= randomValue && rightRange > randomValue) {
                return individual;
            }
            leftRange = rightRange;
        }
        return population.get(population.size() - 1); //worst individual
    }


}
