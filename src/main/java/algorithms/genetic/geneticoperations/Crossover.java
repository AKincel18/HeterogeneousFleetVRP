package algorithms.genetic.geneticoperations;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.PairIndividuals;
import algorithms.genetic.model.PairIndividualsDecode;
import algorithms.genetic.model.ParametersGenetic;
import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;
import utils.Encoder;

import java.util.*;
import java.util.stream.Collectors;

import static algorithms.genetic.geneticoperations.Utils.isInRange;
import static utils.Utils.*;

@RequiredArgsConstructor
public class Crossover {

    private final List<City> cities;
    private final List<Vehicle> vehicles;
    private final City depot;
    private final ParametersGenetic params;

    private boolean isAcceptable1, isAcceptable2;
    @Setter private PairIndividuals currentPair;
    @Getter private Individual best1Individual;
    @Getter private Individual best2Individual;

    public void startCrossover(PairIndividualsDecode pairIndividualsDecode) {
/*        Writer.buildTitleOnConsole("Before crossover: " + "id = " + id);
        Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual1());
        Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual2());*/
        int crossoverIteration = 0;
        Individual best1 = new Individual(depot);
        Individual best2 = new Individual(depot);
        Mutation mutation = new Mutation(vehicles.size(), params.getMutationProbability());
        do {

            replaceVehicle(pairIndividualsDecode);
/*
            Writer.buildTitleOnConsole("After crossover");
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual1());
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual2());*/

            bugFixer(pairIndividualsDecode);

/*            Writer.buildTitleOnConsole("After bug fix");
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual1());
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual2());*/

            if (mutation.isMutation()) {
                mutation.makeMutation(pairIndividualsDecode.getIndividual1());
            }

            if (mutation.isMutation()) {
                mutation.makeMutation(pairIndividualsDecode.getIndividual2());
            }

/*            Writer.buildTitleOnConsole("After mutations");
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual1());
            Writer.writeDecodedResultInOneRow(pairIndividualsDecode.getIndividual2());*/

            analyzeReceivedResults(pairIndividualsDecode, best1, best2);

            crossoverIteration++;
        } while (crossoverIteration != params.getCrossoverRepeatingNumber() && (!isAcceptable1 || !isAcceptable2));

        addIndividualsToNewPopulation(best1, best2);
    }

    /**
     * Method which analyzed received results after crossover
     *
     * @param pairIndividualsDecode pair individuals
     * @param best1                 current best first result
     * @param best2                 current best second result
     */
    private void analyzeReceivedResults(PairIndividualsDecode pairIndividualsDecode, Individual best1, Individual best2) {
        Result result1 = new Encoder(vehicles, cities, depot, pairIndividualsDecode.getIndividual1()).encodeResult();
        Result result2 = new Encoder(vehicles, cities, depot, pairIndividualsDecode.getIndividual2()).encodeResult();
        isAcceptable1 = checkIsAcceptableWeightAll(result1.getRoutes());
        isAcceptable2 = checkIsAcceptableWeightAll(result2.getRoutes());

        if (isAcceptable1) {
            replaceBetterResult(result1, best1, best2);
        }
        if (isAcceptable2) {
            replaceBetterResult(result2, best1, best2);
        }
    }

    /**
     * Replace two draws vehicles between first and second individuals
     *
     * @param pairIndividualsDecode pair individuals
     */
    private void replaceVehicle(PairIndividualsDecode pairIndividualsDecode) {

        List<Integer> changes = getPosChanging();
        Integer[] tmp1 = pairIndividualsDecode.getIndividual1()[changes.get(0)];
        Integer[] tmp2 = pairIndividualsDecode.getIndividual2()[changes.get(1)];

        pairIndividualsDecode.getIndividual1()[changes.get(0)] = tmp2;
        pairIndividualsDecode.getIndividual2()[changes.get(1)] = tmp1;
    }

    /**
     * If result are correct replace the result
     *
     * @param result given result after crossover
     * @param best1  current best first result
     * @param best2  current best second result
     */
    private void replaceBetterResult(Result result, Individual best1, Individual best2) {
        if (best1.getRoutes() == null) {
            best1.setRoutes(result.getRoutes());
            best1.setSum(result.getSum());
            return;
        }
        if (best2.getRoutes() == null) {
            best2.setRoutes(result.getRoutes());
            best2.setSum(result.getSum());
            return;
        }

        Individual bestOfTheBest = best1.getSum() >= best2.getSum() ? best1 : best2;
        if (bestOfTheBest.getSum() < result.getSum()) {
            bestOfTheBest.setRoutes(result.getRoutes());
            bestOfTheBest.setSum(result.getSum());
        }
    }

    /**
     * Adds the best individuals to the new population after crossover
     *
     * @param best1 best first result
     * @param best2 best second result
     */
    private void addIndividualsToNewPopulation(Individual best1, Individual best2) {

        if (best1.getRoutes() != null && best2.getRoutes() != null) {
            best1Individual = best1;
            best2Individual = best2;
        } else if (best1.getRoutes() != null && best2.getRoutes() == null) {
            best1Individual = best1;
            best2Individual = getBetterIndividual();
        } else if (best1.getRoutes() == null && best2.getRoutes() == null) {
            best1Individual = currentPair.getIndividual1();
            best2Individual = currentPair.getIndividual2();
        }

    }

    private Individual getBetterIndividual() {
        return currentPair.getIndividual1().getSum() > currentPair.getIndividual2().getSum()
                ? currentPair.getIndividual1() : currentPair.getIndividual2();
    }

    /**
     * Fix bugs after crossover
     *
     * @param pairIndividualsDecode - crossover pair
     */
    private void bugFixer(PairIndividualsDecode pairIndividualsDecode) {

        Integer[][] fixedInd1 = repairBugFix(pairIndividualsDecode.getIndividual1());
        check(cities, vehicles, fixedInd1);

        Integer[][] fixedInd2 = repairBugFix(pairIndividualsDecode.getIndividual2());
        check(cities, vehicles, fixedInd2);

        pairIndividualsDecode.setIndividual1(fixedInd1);
        pairIndividualsDecode.setIndividual2(fixedInd2);
    }

    /**
     * Method which fixes all bugs resulting from crossover. Vehicles and cities are analyzed in random order.
     *
     * @param individual analyzed individual
     * @return fixed individual
     */
    private Integer[][] repairBugFix(Integer[][] individual) {
        Integer[][] ind = initTmpDecodedResult(individual);
        List<Boolean> visitedPlaces = new ArrayList<>(Arrays.asList(new Boolean[cities.size()]));
        Collections.fill(visitedPlaces, false);
        boolean[] vehicleAnalyzed = new boolean[vehicles.size()];
        List<Integer> vehiclesOrder = generateListOfNumbers(vehicles.size());

        for (Integer vehicle : vehiclesOrder) {
            vehicleAnalyzed[vehicle] = true;
            List<Integer> citiesOrder = generateListOfNumbers(cities.size());
            for (Integer city : citiesOrder) {
                if (individual[vehicle][city] != 0) {
                    if (visitedPlaces.get(city)) {
                        //System.out.println("repair: vec = " + vehicle + ", city = " + city);
                        repairCityVisitedManyTimes(ind, individual[vehicle][city], visitedPlaces, vehicle, city, vehicleAnalyzed);
                    } else {
                        visitedPlaces.set(city, true);
                    }
                }
            }
            repairOrder(ind[vehicle]);
            repairVehicleHasNoRoute(ind, vehicle);
        }
        List<Integer> notVisitedCities = getNotVisitedCities(visitedPlaces);

        if (notVisitedCities.size() > 0) {
            repairAllCitiesNotVisited(ind, notVisitedCities);
        }
        return ind;

    }

    /**
     * Repair situation if all cities are not visited
     *
     * @param ind              analyzed individual
     * @param notVisitedCities list of not visited cities
     */
    private void repairAllCitiesNotVisited(Integer[][] ind, List<Integer> notVisitedCities) {
        for (Integer city : notVisitedCities) {
            int vehicle = new Random().nextInt(vehicles.size());
            int highestCity = findHighestCityNumber(ind[vehicle]);
            ind[vehicle][city] = highestCity + 1;
        }
    }

    /**
     * Repair situation if for the current vehicle is no route for example: 00000
     *
     * @param ind            analyzed individual
     * @param currentVehicle analyzed vehicle
     */
    private void repairVehicleHasNoRoute(Integer[][] ind, int currentVehicle) {
        Integer[] currentRoute = ind[currentVehicle];
        List<Integer> route = Arrays.asList(currentRoute);
        long notVisitedNumber = route.stream().filter(i -> i == 0).count();
        if (notVisitedNumber == cities.size()) {
            //System.out.println("Not visited");
            int vehicle = getVehicleToExchanging(currentVehicle);
            int city = findHighestCity(ind[vehicle]);
            currentRoute[city] = 1;
        }
    }

    /**
     * Find the highest city in the route
     *
     * @param route route
     * @return highest city
     */
    private int findHighestCity(Integer[] route) {
        int max = 0;
        int posMax = 0;
        for (int i = 0; i < route.length; i++) {
            if (max < route[i]) {
                max = route[i];
                posMax = i;
            }
        }
        route[posMax] = 0;
        return posMax;
    }

    /**
     * Find the highest city position in the route
     *
     * @param route route
     * @return highest city position
     */
    private int findHighestCityNumber(Integer[] route) {
        int max = 0;
        for (Integer city : route) {
            if (max < city) {
                max = city;
            }
        }
        return max;
    }

    /**
     * Get random vehicle but not the same like a current analyzed
     *
     * @param currentVehicle analyzed vehicle
     * @return draw vehicle
     */
    private int getVehicleToExchanging(int currentVehicle) {
        int drawVehicle;
        do {
            drawVehicle = new Random().nextInt(vehicles.size());
        } while (drawVehicle == currentVehicle);
        return drawVehicle;
    }

    /**
     * Repair situation where order in route are incorrect: for example: 00200 (there is no first visited city)
     *
     * @param ind analyzed individual
     */
    private void repairOrder(Integer[] ind) {
        int prev = 0;
        List<Integer> route = Arrays.stream(ind).sorted().collect(Collectors.toList());

        for (Integer current : route) {
            if (current != 0 && prev + 1 != current) {
                //System.out.println("repair order");
                findAndDecreaseCity(ind, prev, current);
            }
            prev = current;
        }
    }

    /**
     * Find the highest position and decrease it
     *
     * @param ind     analyzed individual
     * @param prev    last correct position
     * @param current first incorrect position
     */
    private void findAndDecreaseCity(Integer[] ind, Integer prev, Integer current) {
        for (int i = 0; i < ind.length; i++) {
            if (ind[i].equals(current)) {
                ind[i] = prev + 1;
                return;
            }
        }
    }

    /**
     * Fixes situation when given city is visited more then once
     *
     * @param ind             individual
     * @param currentPos      current position in a route
     * @param visitedPlaces   array visited places
     * @param vehicle         current analyzed vehicle
     * @param oldCity         current analyzed vehicle
     * @param vehicleAnalyzed array analyzed vehicles
     */
    private void repairCityVisitedManyTimes(Integer[][] ind, Integer currentPos, List<Boolean> visitedPlaces,
                                            int vehicle, int oldCity, boolean[] vehicleAnalyzed) {
        int city = findNotVisitedCity(visitedPlaces);
        if (city != -1) {
            visitedPlaces.set(city, true);
            ind[vehicle][city] = currentPos;
        }

        if (notVisitedThatCityRandom(ind, oldCity, vehicle, vehicleAnalyzed)) {
            ind[vehicle][oldCity] = 0;
        }
    }

    /**
     * Check if given city is visited by the previous vehicles
     *
     * @param ind             analyzed individual
     * @param oldCity         analyzed city
     * @param currentVehicle  analyzed vehicle
     * @param vehicleAnalyzed array vehicles analyzed
     * @return true for fulfilled conditions
     */
    private boolean notVisitedThatCityRandom(Integer[][] ind, int oldCity, int currentVehicle, boolean[] vehicleAnalyzed) {
        for (int v = 0; v < vehicles.size(); v++) {
            if (currentVehicle != v && vehicleAnalyzed[v] && ind[v][oldCity] != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find not visited city. All cities which is not visited are draw. One draw city are returned.
     *
     * @param visitedPlaces array visited places
     * @return draw city, if all cities are visited return -1
     */
    private int findNotVisitedCity(List<Boolean> visitedPlaces) {
        List<Integer> notVisitedCities = getNotVisitedCities(visitedPlaces);
        if (notVisitedCities.size() == 0)
            return -1;
        int pos = new Random().nextInt(notVisitedCities.size());
        return notVisitedCities.get(pos);
    }

    /**
     * Return all cities are not visited.
     *
     * @param visitedPlaces array visited places
     * @return not visited cities
     */
    private List<Integer> getNotVisitedCities(List<Boolean> visitedPlaces) {
        List<Integer> cities = new ArrayList<>();
        for (int city = 0; city < visitedPlaces.size(); city++) {
            if (!visitedPlaces.get(city)) {
                cities.add(city);
            }
        }
        return cities;
    }

    private Integer[][] initTmpDecodedResult(Integer[][] currentDecodedResult) {
        Integer[][] tmpDecodedResult = new Integer[vehicles.size()][cities.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            System.arraycopy(currentDecodedResult[i], 0, tmpDecodedResult[i], 0, cities.size());
        }
        return tmpDecodedResult;
    }

    /**
     * Get two numbers of vehicle which will be replace
     *
     * @return two numbers of vehicle
     */
    private List<Integer> getPosChanging() {
        int firstPosToChange, secondPosToChange;
        do {
            firstPosToChange = new Random().nextInt(vehicles.size());
            secondPosToChange = new Random().nextInt(vehicles.size());
        } while (firstPosToChange == secondPosToChange);
        return Arrays.asList(firstPosToChange, secondPosToChange);
    }

    public boolean isCrossover() {
        return isInRange(params.getCrossoverProbability());
    }
}
