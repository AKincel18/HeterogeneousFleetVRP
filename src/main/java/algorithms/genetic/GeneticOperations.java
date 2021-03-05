package algorithms.genetic;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import algorithms.genetic.model.PairIndividualsDecode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Decoder;
import utils.Encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class GeneticOperations {
    private final List<PairIndividuals> pairIndividuals;
    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depot;
    private final double crossoverProbability;
    private final double mutationProbability;
    @Getter private List<Individual> newPopulation;
    private int currentId = 0;

    //todo partial mapped crossover from list of pair individuals
    // https://www.youtube.com/watch?v=c2ft8AG8JKE -> example
    public void crossoverAndMutation() {
        newPopulation = new ArrayList<>();
        Decoder decoder = new Decoder(cities);
        pairIndividuals.forEach(p -> {
                    if (isCrossover()) {
                        p.setPairIndividualsDecode(new PairIndividualsDecode(
                                decoder.decodeIndividual(p.getIndividual1().getRoutes()),
                                decoder.decodeIndividual(p.getIndividual2().getRoutes())
                        ));
                        randomCrossover(p.getPairIndividualsDecode(), p.getChangesNumber(), p.getId());
                        //partialMappedCrossover(p.getLeftRange(), p.getRightRange(), p.getPairIndividualsDecode(), i);
                    }
                    else {
                        newPopulation.add(p.getIndividual1());
                        newPopulation.add(p.getIndividual2());
                    }
                }
        );
        System.out.println();
    }

    private void randomCrossover(PairIndividualsDecode pairIndividualsDecode, int changesNumber, int id) {
        Integer[][] iDecode1 = new Integer[vehicles.size()][cities.size()];
        Integer[][] iDecode2 = new Integer[vehicles.size()][cities.size()];
        for (int currentVecNumber = 0; currentVecNumber < vehicles.size(); currentVecNumber++) {
            List<Integer> i1 = Arrays.asList(pairIndividualsDecode.getIndividual1()[currentVecNumber]);
            List<Integer> i2 = Arrays.asList(pairIndividualsDecode.getIndividual2()[currentVecNumber]);
            int crossoverNumber = getCrossoverNumber(i1, i2, changesNumber);
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Starting crossover: " + "id = " + id + ", vehicle = " + currentVecNumber + " <" + crossoverNumber + ">");
            System.out.println("Before");
            System.out.println(i1.toString());
            System.out.println(i2.toString());
            for (int i = 1; i <= crossoverNumber; i++) {
                int pos1 = i1.indexOf(i);
                int pos2 = i2.indexOf(i);
                if (pos1 != pos2) {
                    boolean added1 = false;
                    boolean added2 = false;
                    if (i1.get(pos2) == 0) {
                        i1.set(pos2, i);
                        added1 = true;
                    }
                    if (i2.get(pos1) == 0) {
                        i2.set(pos1, i);
                        added2 = true;
                    }
                    if (added1) {
                        i1.set(pos1, 0);
                    }
                    if (added2) {
                        i2.set(pos2, 0);
                    }
                }
            }
            System.out.println("After");
            System.out.println(i1.toString());
            System.out.println(i2.toString());

            if (isMutation()) {
                mutation(i1);
                System.out.println("Mutation1");
                System.out.println(i1.toString());
            }

            if (isMutation()) {
                mutation(i2);
                System.out.println("Mutation2");
                System.out.println(i2.toString());
            }
            iDecode1[currentVecNumber] = i1.toArray(Integer[]::new);
            iDecode2[currentVecNumber] = i2.toArray(Integer[]::new);
        }
        encodeResults(iDecode1);
        encodeResults(iDecode2);
    }

    private void encodeResults(Integer[][] iDecode) {
        Encoder encoder = new Encoder(cities, iDecode);
        newPopulation.add(encoder.encodeIndividual(vehicles, depot, currentId));
        currentId++;
    }

    private int getCrossoverNumber(List<Integer> i1, List<Integer> i2, int changesNumber) {

        int biggest1 = i1.stream().max(Integer::compareTo).orElse(1);
        int biggest2 = i2.stream().max(Integer::compareTo).orElse(1);
        int smallest = Math.min(biggest1, biggest2);
        return Math.min(smallest, changesNumber);
    }

    private boolean isCrossover() {
        return isInRange(crossoverProbability);
    }
    private boolean isMutation() {
        return isInRange(mutationProbability);
    }

    private boolean isInRange(double probability) {
        double randomNumber = new Random().nextDouble();
        return probability > randomNumber;
    }

    public void partialMappedCrossover(int leftRange, int rightRange, PairIndividualsDecode pairIndividualsDecode,
                                       int currentVehicleNumber) {
        Integer[] i1 = pairIndividualsDecode.getIndividual1()[currentVehicleNumber];
        Integer[] i2 = pairIndividualsDecode.getIndividual2()[currentVehicleNumber];
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Starting crossover: " + "<" + leftRange + ":" + rightRange + ">");
        System.out.println("Before");
        System.out.println(Arrays.toString(i1));
        System.out.println(Arrays.toString(i2));
        List<Relation> relations = new ArrayList<>();
        for (int i = leftRange; i < rightRange; i++) {
            int buf1 = i1[i];
            int buf2 = i2[i];
            i1[i] = buf2;
            i2[i] = buf1;
            addRelation(relations, buf1, buf2);
        }

        for (int i = 0; i < leftRange; i++) {
            makePMXCrossover(relations, i1, i2, i);
        }
        for (int i = rightRange; i < i1.length; i++) {
            makePMXCrossover(relations, i1, i2, i);
        }
        System.out.println("After");
        System.out.println(Arrays.toString(i1));
        System.out.println(Arrays.toString(i2));
    }

    private void makePMXCrossover(List<Relation> relations, Integer[] i1, Integer[] i2, Integer pos) {
        Relation relation = findRelation(relations, i1[pos], true);
        if (relation != null)
            i1[pos] = i1[pos] == relation.getA() ? relation.getB() : relation.getA();
        relation = findRelation(relations, i2[pos], false);
        if (relation != null)
            i2[pos] = i2[pos] == relation.getA() ? relation.getB() : relation.getA();
    }

    private Relation findRelation(List<Relation> relations, Integer num, boolean whichInd) {
        for (Relation relation : relations) {
            //boolean isVisited = whichInd ? relation.isVisited1() : relation.isVisited2();
            if ((relation.getA() == num || relation.getB() == num)) {
                if (whichInd) {
                    relation.setVisited1();
                }
                else {
                    relation.setVisited2();
                }
                return relation;
            }
        }
        return null;
    }

    private boolean checkIsRepeated(Integer[] i, int pos) {
        for (Integer current : i) {
            if (current.equals(i[pos]))
                return true;
        }
        return false;
    }

    private void addRelation(List<Relation> relations, int buf1, int buf2) {
        for (Relation r : relations) {
            if (buf1 == buf2)
                return;
            boolean isSet = r.setRelation(buf1, buf2);
            if (isSet) {
                return;
            }
        }
        relations.add(new Relation(buf1, buf2));

    }


    private void mutation(List<Integer> i) {
        List<Integer> positionsToChange = drawNoRepeatNumbers();
        int oldValue1 = i.get(positionsToChange.get(0));
        int oldValue2 = i.get(positionsToChange.get(1));
        i.set(positionsToChange.get(0), oldValue2);
        i.set(positionsToChange.get(1), oldValue1);
    }

    private List<Integer> drawNoRepeatNumbers() {
        int firstPosToChange, secondPosToChange;
        do {
            firstPosToChange = new Random().nextInt(cities.size() - 1);
            secondPosToChange = new Random().nextInt(cities.size() - 1);
        } while (firstPosToChange == secondPosToChange);
        return Arrays.asList(firstPosToChange, secondPosToChange);
    }
}
