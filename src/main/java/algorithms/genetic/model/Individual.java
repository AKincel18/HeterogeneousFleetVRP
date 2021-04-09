package algorithms.genetic.model;

import commons.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.City;


@NoArgsConstructor
@Getter
@Setter
public class Individual {
    private int id;
    private Result result;
    private double reproductionProbability;
    private double circleSegment;
    private double r;
    private City depot;


    public Individual(Result result, City depot) {
        this.depot = depot;
        this.result = result;
    }

    public Individual(int id, City depot, Result result) {
        this.id = id;
        this.depot = depot;
        this.result = result;
    }

    public Individual(City depot) {
        this.depot = depot;
    }

    public Individual (Individual individual) {
        this.result = individual.getResult();
        this.id = individual.getId();
        this.depot = individual.getDepot();
        this.reproductionProbability = 0.0;
        this.r = 0.0;
        this.circleSegment = 0.0;
    }
}
