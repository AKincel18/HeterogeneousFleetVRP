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
    @Getter private List<Individual> selectedIndividuals;

    public void createRanking() {
        //sorted by distance
        population.sort(Comparator.comparing(Individual::getSum).reversed());
        double sumDistance = population.stream().mapToDouble(Individual::getSum).sum();
        //double avg = population.stream().mapToDouble(Individual::getSum).average().orElse(0.0);

        population.forEach(p -> p.setReproductionProbability(p.getSum() / sumDistance));
        countCircleSegment();
        selection();

    }

    private void countCircleSegment() {
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        population.forEach(i -> {
            sum.updateAndGet(v -> v + i.getReproductionProbability());
            i.setCircleSegment(sum.get());
            //System.out.println(sum.get());
        });
    }
    private void selection() {
        //Utils.buildTitleOnConsole("SELECTION NOW");
        selectedIndividuals = new ArrayList<>();
        Random random = new Random();
        for (int i =0; i < population.size(); i++) {
            Individual ind = findIndividual(random.nextDouble());
            //System.out.println("choosed = " + (ind != null ? ind.getCircleSegment() : 0)); //for debugging
            selectedIndividuals.add(ind);
        }

    }

    private Individual findIndividual(double randomValue) {
        //System.out.println("random value = " + randomValue);
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
