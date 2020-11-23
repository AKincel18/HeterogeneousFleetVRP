package algorithms.genetic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class Selection {

    private final List<Individual> population;
    private final int numberOfCity;
    private final int numberOfVehicle;
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
        Utils.buildTitleOnConsole("Count circle segment");
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        population.forEach(i -> {
            sum.updateAndGet(v -> v + i.getReproductionProbability());
            i.setCircleSegment(sum.get());
            System.out.println(sum.get());
        });
    }

    private void selection() {
        Utils.buildTitleOnConsole("SELECTION NOW");
        selectedIndividuals = new ArrayList<>();
        Random random = new Random();
        for (int i =0; i < population.size(); i++) {
            Individual ind = findIndividual(random.nextDouble());
            System.out.println("choosed = " + (ind != null ? ind.getCircleSegment() : 0)); //for debugging
            selectedIndividuals.add(ind);
        }
    }

    private void generatePairsIndividual(){
        Utils.buildTitleOnConsole("generate pairs");
        List<Integer> numbers = Utils.generateListOfNumbers(population.size());
        Random random = new Random();
        pairIndividuals = new ArrayList<>();
        int leftRange;
        for (int i = 0; i < population.size(); i+=2) {
            leftRange = random.nextInt(numberOfCity - 1); //without the last one
            PairIndividuals selectedIndividual = new PairIndividuals(
                    selectedIndividuals.get(numbers.get(i)),
                    selectedIndividuals.get(numbers.get(i + 1)),
                    leftRange,
                    random.nextInt(numberOfCity) + leftRange,
                    random.nextInt(numberOfVehicle)
            );
            pairIndividuals.add(selectedIndividual);
            System.out.print("ind1 = " + numbers.get(i));
            System.out.print("; ind2 = " + numbers.get(i + 1));
            System.out.println(", <" + selectedIndividual.getLeftRange() + ":" + selectedIndividual.getRightRange() +
            ">, vehicle number = " + selectedIndividual.getWhichVehicle());
        }

    }


    private Individual findIndividual(double randomValue) {
        System.out.println("random value = " + randomValue);
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getCircleSegment() >= randomValue){
                if (i == 0 || i == population.size() -1)
                    return population.get(i);
                else {
                    return population.get(i - 1);
                }
            }
        }
        return null;
    }


}
