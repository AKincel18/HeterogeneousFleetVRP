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
     @Setter private double r;
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

    public Individual (Individual individual) {
        this.sum = individual.getSum();
        this.routes = individual.getRoutes();
        this.id = individual.getId();
        this.depot = individual.getDepot();
        this.reproductionProbability = 0.0;
        this.r = 0.0;
        this.circleSegment = 0.0;
    }
}
