package algorithms.genetic.model;

import commons.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class Individual extends Result {
     @Setter private double reproductionProbability;
     @Setter private double circleSegment;
     private City depot;
     @Setter private int id;

    public Individual(Map<Vehicle, List<City>> routes, City depot, int id) {
        super(routes);
        this.depot = depot;
        this.id = id;
    }

    public Individual(City depot) {
        this.depot = depot;
    }
}
