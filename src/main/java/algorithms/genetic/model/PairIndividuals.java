package algorithms.genetic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class PairIndividuals {
    private final Individual individual1;
    private final Individual individual2;
    private final int leftRange = 0;
    private final int rightRange = 0;
    private int changesNumber;
    private int id;
    @Setter private PairIndividualsDecode pairIndividualsDecode;

    public PairIndividuals(Individual individual1, Individual individual2, int changesNumber, int id) {
        this.individual1 = individual1;
        this.individual2 = individual2;
        this.changesNumber = changesNumber;
        this.id = id;
    }
}
