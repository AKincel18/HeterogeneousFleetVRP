package utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Utils.sortRoutesByVehicleId;

@RequiredArgsConstructor
public class Coder {

    private final City depot;
    @Getter @Setter private Integer[] cutPoints;

    public Integer[] codeResultToArray(Map<Vehicle, List<City>> routes) {
        List<Integer> codedResultList = new ArrayList<>();
        cutPoints = new Integer[routes.size()];
        int cutPoint = 0;
        routes = sortRoutesByVehicleId(routes);
        for (int i = 0; i < routes.keySet().size(); i++) {

            Vehicle vehicle = (Vehicle) routes.keySet().toArray()[i];
            List<City> route = routes.get(vehicle);
            route.removeIf(r -> r.getId() == 0);
            for (City city : route) {
                codedResultList.add(city.getId());
                cutPoint++;
            }
            route.add(0, depot);
            route.add(depot);
            cutPoints[i] = cutPoint;
        }
        return codedResultList.toArray(new Integer[0]);
    }

    public Map<Integer, List<Integer>> codeResultToMap(Map<Vehicle, List<City>> routes) {
        Map<Integer, List<Integer>> codedResultMap = new HashMap<>();
        routes = sortRoutesByVehicleId(routes);
        for (int i = 0; i < routes.keySet().size(); i++) {

            Vehicle vehicle = (Vehicle) routes.keySet().toArray()[i];
            List<City> route = routes.get(vehicle);
            route.removeIf(r -> r.getId() == 0);
            List<Integer> routeCoded = new ArrayList<>();
            for (City city : route) {
                routeCoded.add(city.getId());
            }
            route.add(0, depot);
            route.add(depot);
            codedResultMap.put(vehicle.getId(), routeCoded);
        }
        return codedResultMap;
    }
}
