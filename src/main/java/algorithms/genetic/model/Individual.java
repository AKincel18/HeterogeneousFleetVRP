package algorithms.genetic.model;

import commons.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.City;


@NoArgsConstructor
@Getter
public class Individual extends Result {
     @Setter private double reproductionProbability;
     @Setter private double circleSegment;
     private City depot;
     @Setter private int id;

    public Individual(Result result, City depot, int id) {
        super(result);
        this.depot = depot;
        this.id = id;
    }

    public Individual(City depot) {
        this.depot = depot;
    }
}
