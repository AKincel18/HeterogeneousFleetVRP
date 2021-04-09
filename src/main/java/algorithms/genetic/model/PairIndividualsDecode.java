package algorithms.genetic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PairIndividualsDecode {
    private List<Integer> individual1;
    private List<Integer> individual2;
}
