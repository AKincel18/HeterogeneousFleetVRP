package algorithms.genetic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;

import java.util.*;
@NoArgsConstructor
public class Individual {
    @Getter private Map<Vehicle, List<City>> individual = new HashMap<>();
    @Getter @Setter private Double sum = 0.0;
    @Getter @Setter private double reproductionProbability;
    @Getter @Setter private double circleSegment;

    private City depot;

    public void initMap(List<Vehicle> vehicles, City depot) {
        this.depot = depot;
        vehicles.forEach( v -> individual.put(v, new ArrayList<>(Collections.singletonList(depot))));
    }

    public void addEndCity() {
        for (Vehicle vehicle : individual.keySet())
            Objects.requireNonNull(individual.computeIfPresent(vehicle, (key, value) -> individual.get(key))).add(depot);
    }
}
