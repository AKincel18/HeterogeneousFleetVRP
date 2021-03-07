package algorithms.genetic.model;

import commons.Result;
import lombok.*;
import model.City;
import model.Vehicle;

import java.util.*;

@NoArgsConstructor
@Getter
public class Individual extends Result {
     @Setter private double reproductionProbability;
     @Setter private double circleSegment;
     private City depot;
     @Setter private int id;

    public Individual(Map<Vehicle, List<City>> routes, City depot, int id) {
        super(routes);
        //this.result = new Result(routes);
        this.depot = depot;
        this.id = id;
    }

    public Individual(Result result, City depot, int id) {
        super(result);
        this.depot = depot;
        this.id = id;
    }
}
