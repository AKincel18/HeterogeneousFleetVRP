package algorithms.genetic.selection;

import algorithms.genetic.model.Individual;
import algorithms.genetic.model.ParametersGenetic;
import commons.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.generateListOfNumbers;

public class TournamentSelection extends Selection {

    public TournamentSelection(List<Individual> population, ParametersGenetic params) {
        super(population, params);
    }

    public void makeSelection() {
        theBest = population.stream().findFirst().orElseThrow();
        selectedIndividuals = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            List<Individual> players = drawIndividualsToTournament();
            Individual winner = findWinnerTournament(players);
            selectedIndividuals.add(winner);
        }
        generateIndividualsPairs();
    }

    private List<Individual> drawIndividualsToTournament() {
        List<Integer> drawNumbers = generateListOfNumbers(population.size());
        return drawNumbers.stream().map(population::get).limit(params.getTournamentSize()).collect(Collectors.toList());
    }

    private Individual findWinnerTournament(List<Individual> players) {
        players.sort(Comparator.comparing(Result::getSum));
        return players.stream().findFirst().orElseThrow();
    }
}
