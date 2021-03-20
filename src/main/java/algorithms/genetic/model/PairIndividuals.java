package algorithms.genetic.model;

import lombok.Data;
import lombok.Setter;

@Data
public class PairIndividuals {
    private final Individual individual1;
    private final Individual individual2;
    private final int id;
    @Setter private PairIndividualsDecode pairIndividualsDecode;
}
