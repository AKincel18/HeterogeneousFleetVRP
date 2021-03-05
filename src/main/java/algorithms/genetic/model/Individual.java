package algorithms.genetic.model;

import lombok.*;
import model.City;
import model.Vehicle;

import java.util.*;
@NoArgsConstructor
@Getter
public class Individual {
     @Setter private Map<Vehicle, List<City>> routes = new HashMap<>();
     @Setter private Double sum = 0.0;
     @Setter private double reproductionProbability;
     @Setter private double circleSegment;
     private City depot;
     private int id;

    public Individual(int id) {
        this.id = id;
    }

    public Individual(Map<Vehicle, List<City>> routes, City depot, int id) {
        this.routes = routes;
        this.depot = depot;
        this.id = id;
    }

    public void initMap(List<Vehicle> vehicles, City depot) {
        this.depot = depot;
        vehicles.forEach( v -> routes.put(v, new ArrayList<>(Collections.singletonList(depot))));
    }

    public void addEndCity() {
        for (Vehicle vehicle : routes.keySet())
            Objects.requireNonNull(routes.computeIfPresent(vehicle, (key, value) -> routes.get(key))).add(depot);
    }
}
