package algorithms.tabusearch.model;

import commons.Result;
import lombok.*;

@Getter
public class ResultTabu {
    @Setter private Result result;
    @Setter private double z;
    private TabuCoords tabuCoords;

    public ResultTabu(Result result) {
        this.result = result;
    }

    public ResultTabu(Result result, double z, TabuCoords tabuCoords) {
        this.result = result;
        this.z = z;
        this.tabuCoords = tabuCoords;
    }

    public void setArgs(double z, TabuCoords tabuCoords) {
        this.z = z;
        this.tabuCoords = tabuCoords;
    }

    public void clear() {
        this.result = null;
        this.z = 0.0;
        this.tabuCoords = null;
    }

}
