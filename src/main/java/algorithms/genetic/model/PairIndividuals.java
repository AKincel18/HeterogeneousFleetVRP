package algorithms.genetic.model;

import lombok.Data;

@Data
public class PairIndividuals {
    private final int id;
    private final Individual individual1;
    private final Individual individual2;
    private PairIndividualsDecode pairIndividualsDecode;
}
