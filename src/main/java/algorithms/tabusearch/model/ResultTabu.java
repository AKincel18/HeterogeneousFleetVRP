package algorithms.tabusearch.model;

import commons.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class ResultTabu {
    @Getter private final Result result;
    @Getter @Setter private double z;
    @Getter private TabuCoords tabuCoords;

    public void setArgs(double z, TabuCoords tabuCoords) {
        this.z = z;
        this.tabuCoords = tabuCoords;
    }

}
