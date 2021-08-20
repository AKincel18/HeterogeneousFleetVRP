package algorithms.genetic.geneticoperations;

import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static algorithms.genetic.geneticoperations.Utils.isInRange;

@RequiredArgsConstructor
public class Mutation {
    private final double mutationProbability;
    private final List<Vehicle> vehicles;
    private final City depot;

    public void makeMutation(Map<Vehicle, List<City>> routes) {
        Pair<Integer, Integer> vehiclesToMutation = drawVehicles();
        Vehicle v1 = vehicles.stream().filter(v -> v.getId() == vehiclesToMutation.getObj1()).findFirst().orElseThrow();
        List<City> routeVehicle1 = routes.get(v1);
        if (routeVehicle1.size() == 3) //only one city (plus two depots)
            return;

        Vehicle v2 = vehicles.stream().filter(v -> v.getId() == vehiclesToMutation.getObj2()).findFirst().orElseThrow();
        List<City> routeVehicle2 = routes.get(v2);
        if (routeVehicle2.size() == 3) //only one city (plus two depots)
            return;

        routeVehicle1.removeIf(c -> c.getId() == 0);
        routeVehicle2.removeIf(c -> c.getId() == 0);

        int randomCity1 = new Random().nextInt(routeVehicle1.size());
        City moveCity = routeVehicle1.get(randomCity1);
        int randomCity2 = new Random().nextInt(routeVehicle2.size());
        routeVehicle1.remove(moveCity);
        routeVehicle2.add(randomCity2, moveCity);

        routeVehicle1.add(0, depot);
        routeVehicle1.add(depot);

        routeVehicle2.add(0, depot);
        routeVehicle2.add(depot);
    }

    private Pair<Integer, Integer> drawVehicles() {
        int vec1, vec2;
        Random random = new Random();
        do {
            vec1 = random.nextInt(vehicles.size());
            vec2 = random.nextInt(vehicles.size());
        } while (vec1 == vec2);
        return new Pair<>(vec1, vec2);
    }

    public boolean isMutation() {
        return isInRange(mutationProbability);
    }
}
