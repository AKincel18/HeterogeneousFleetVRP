package algorithms.genetic;

import lombok.AllArgsConstructor;
import model.City;
import utils.Decoder;

import java.util.List;

@AllArgsConstructor
public class GeneticOperations {
    private List<PairIndividuals> pairIndividuals;
    private List<City> cities;

    //todo partial mapped crossover from list of pair individuals
    // https://www.youtube.com/watch?v=c2ft8AG8JKE -> example
    public void crossover() {
        Decoder decoder = new Decoder(cities);
        pairIndividuals.forEach( p -> p.setPairIndividualsDecode(new PairIndividualsDecode(
                decoder.decodeIndividual(p.getIndividual1().getIndividual()),
                decoder.decodeIndividual(p.getIndividual2().getIndividual())
        ))
        );
        System.out.println();
    }

    public void mutation() {

    }
}
