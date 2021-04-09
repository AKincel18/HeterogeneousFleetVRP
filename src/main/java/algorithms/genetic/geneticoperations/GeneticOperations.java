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

import static utils.Utils.checkIsAcceptableWeightAll;

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
    private Crossover crossover;
    private Mutation mutation;
    private Decoder decoder;
    private Decoder decoder2;
    private boolean isAcceptable1, isAcceptable2;

    public void geneticOperations() {
        init();
        pairIndividuals.forEach(p -> {
            if (crossover.isCrossover()) {
                crossover(p);
            } else {
                addIndividualToNewPopulation(p.getIndividual1());
                addIndividualToNewPopulation(p.getIndividual2());
            }
        });
        elitism();
    }

    private void init() {
        populationNew = new ArrayList<>();
        crossover = new Crossover(cities, vehicles, depot, params);
        decoder = new Decoder(cities, depot);
        decoder2 = new Decoder(cities, depot);
        mutation = new Mutation(params.getMutationProbability(), vehicles, depot);
    }

    private void crossover(PairIndividuals p) {
        Individual individual1, individual2;
        int crossoverIteration = 0;
        Individual best1 = new Individual(depot);
        Individual best2 = new Individual(depot);
        p.setPairIndividualsDecode(new PairIndividualsDecode(
                decoder.decodeResult2(p.getIndividual1().getResult().getRoutes()),
                decoder2.decodeResult2(p.getIndividual2().getResult().getRoutes())
        ));
        do {
            crossover.startCrossover(p.getPairIndividualsDecode(), decoder.getCutPoints(), decoder2.getCutPoints());
            individual1 = crossover.getIndividualNew1();
            individual2 = crossover.getIndividualNew2();


            if (mutation.isMutation()) {
                mutation.makeMutation(individual1.getResult().getRoutes());
            }
            if (mutation.isMutation()) {
                mutation.makeMutation(individual2.getResult().getRoutes());
            }
            analyzeReceivedResults(individual1, individual2, best1, best2);
            crossoverIteration++;
        } while (crossoverIteration != params.getCrossoverRepeatingNumber() && (!isAcceptable1 || !isAcceptable2));

        addIndividuals(p.getIndividual1(), p.getIndividual2(), best1, best2);

    }

    private void addIndividuals(Individual oldIndividual1, Individual oldIndividual2, Individual best1, Individual best2) {


        if (best1.getResult() == null) { //not found any new individuals
            addIndividualToNewPopulation(oldIndividual1);
            addIndividualToNewPopulation(oldIndividual2);
        }
        else {
            if (best2.getResult() == null) { //found one new individual
                addIndividualToNewPopulation(oldIndividual1.getResult().getSum() > oldIndividual2.getResult().getSum() ?
                        oldIndividual2 : oldIndividual1);
                addIndividualToNewPopulation(best1);
            }
            else { //found two new individual
                addIndividualToNewPopulation(best1);
                addIndividualToNewPopulation(best2);
            }
        }
    }

    private void analyzeReceivedResults(Individual individual1, Individual individual2, Individual best1, Individual best2) {
        Result result1 = new Result(individual1.getResult().getRoutes(), individual1.getResult().getSum());
        Result result2 = new Result(individual2.getResult().getRoutes(), individual2.getResult().getSum());
        isAcceptable1 = checkIsAcceptableWeightAll(result1.getRoutes());
        isAcceptable2 = checkIsAcceptableWeightAll(result2.getRoutes());

        if (isAcceptable1) {
            replaceBetterResult(result1, best1, best2);
        }
        if (isAcceptable2) {
            replaceBetterResult(result2, best1, best2);
        }
    }

    private void replaceBetterResult(Result result, Individual best1, Individual best2) {
        if (best1.getResult() == null) {
            best1.setResult(result);
            return;
        }
        if (best2.getResult() == null) {
            best2.setResult(result);
            return;
        }

        Individual bestOfTheBest = best1.getResult().getSum() > best2.getResult().getSum() ? best2 : best1;
        if (bestOfTheBest.getResult().getSum() < result.getSum()) {
            bestOfTheBest.setResult(result);
        }
    }

    private void addIndividualToNewPopulation(Individual individual) {
        individual.setId(currentId);
        currentId++;
        populationNew.add(new Individual(individual));
    }

    private void elitism() {
        populationNew.sort(Comparator.comparing(individual -> individual.getResult().getSum()));
        Individual theBestNew = populationNew.stream().findFirst().orElseThrow();
        if (theBestOld.getResult().getSum() < theBestNew.getResult().getSum()) {
            Individual indToRemove = populationNew.get(params.getPopulationSize() - 1);
            theBestOld.setId(indToRemove.getId());
            populationNew.remove(indToRemove); //remove the worst individual
            populationNew.add(new Individual(theBestOld)); //add the best from the previous population
        }
    }

}
