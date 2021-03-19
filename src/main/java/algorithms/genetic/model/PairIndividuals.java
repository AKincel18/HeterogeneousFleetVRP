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
    private final int id;
    @Setter private PairIndividualsDecode pairIndividualsDecode;
}
