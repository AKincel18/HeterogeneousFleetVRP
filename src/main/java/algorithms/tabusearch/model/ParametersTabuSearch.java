package algorithms.tabusearch.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ParametersTabuSearch {
    private final int iterationNumber;
    private final int tabuIterationNumber;
}
