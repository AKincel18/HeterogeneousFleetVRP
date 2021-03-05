package algorithms.genetic;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import exceptions.IndividualNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Utils;
import utils.Writer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static constants.StringConst.INDIVIDUAL_NOT_FOUND;

@RequiredArgsConstructor
public class Selection {

    private final List<Individual> population;
    private final int numberOfCity;
    private List<Individual> selectedIndividuals;
    @Getter private List<PairIndividuals> pairIndividuals;

    public void createRanking() {
        //sorted by distance
        population.sort(Comparator.comparing(Individual::getSum).reversed());
        double sumDistance = population.stream().mapToDouble(Individual::getSum).sum();
        //double avg = population.stream().mapToDouble(Individual::getSum).average().orElse(0.0);

        population.forEach(p -> p.setReproductionProbability(p.getSum() / sumDistance));
        countCircleSegment();
        selection();
        generatePairsIndividual();
    }

    private void countCircleSegment() {
        Writer.buildTitleOnConsole("Count circle segment");
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        population.forEach(i -> {
            sum.updateAndGet(v -> v + i.getReproductionProbability());
            i.setCircleSegment(Utils.roundNumber(sum.get()));
            System.out.println(Utils.roundNumber(sum.get()));
        });
    }

    private void selection() {
        Writer.buildTitleOnConsole("SELECTION NOW");
        selectedIndividuals = new ArrayList<>();
        Random random = new Random();
        try {
            for (int i = 0; i < population.size(); i++) {
                Individual ind = findIndividual(Utils.roundNumber(random.nextDouble()));
                System.out.println("choosed = " + ind.getCircleSegment() + "; individual id = " + ind.getId()); //for debugging
                selectedIndividuals.add(ind);
            }
        } catch (IndividualNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void generatePairsIndividual() {
        Writer.buildTitleOnConsole("Generate pairs");
        List<Integer> numbers = Utils.generateListOfNumbers(population.size());

        pairIndividuals = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < population.size(); i+=2) {
//            int []ranges = calculateRanges();
//            PairIndividuals selectedIndividualPair = new PairIndividuals(
//                    selectedIndividuals.get(numbers.get(i)),
//                    selectedIndividuals.get(numbers.get(i + 1)),
//                    ranges[0], //left ranges
//                    ranges[1] //right ranges
//            );

            PairIndividuals selectedIndividualPair = new PairIndividuals(
                    selectedIndividuals.get(numbers.get(i)),
                    selectedIndividuals.get(numbers.get(i + 1)),
                    new Random().nextInt(numberOfCity - 1) + 1, id);
            id++;
            pairIndividuals.add(selectedIndividualPair);
            System.out.print("ind1 id = " + selectedIndividualPair.getIndividual1().getId());
            System.out.print("; ind2 id = " + selectedIndividualPair.getIndividual2().getId());
            System.out.println(", <" + selectedIndividualPair.getLeftRange() + ":" + selectedIndividualPair.getRightRange() + ">");
        }

    }

    private int[] calculateRanges() {
        Random random = new Random();
        int leftRange = random.nextInt(numberOfCity - 1); //without the last one
        int rightRange = random.nextInt(numberOfCity);
        if (leftRange == rightRange) {
            rightRange += 1;
        }
        if (leftRange > rightRange) {
            int temp = leftRange;
            leftRange = rightRange;
            rightRange = temp;
        }
        return new int[]{leftRange, rightRange};
    }

    private Individual findIndividual(double randomValue) throws IndividualNotFoundException {
        System.out.print("random value = " + randomValue + "; ");
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
