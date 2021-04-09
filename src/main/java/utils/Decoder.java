package utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.Utils.sortRoutesByVehicleId;

@RequiredArgsConstructor
public class Decoder {

    private final List<City> cities;
    private City depot;
    private Integer[][] decodedResult;
    @Getter private List<Integer> cutPoints;

    public Decoder(List<City> cities, City depot) {
        this.cities = cities;
        this.depot = depot;
    }

    public Integer[][] decodeResult(Map<Vehicle, List<City>> routes) {
        initDecoders(routes.keySet().size(), cities.size());
        routes = sortRoutesByVehicleId(routes);
        for (int i = 0; i < routes.keySet().size(); i++) {
            Vehicle vehicle = (Vehicle) routes.keySet().toArray()[i];
            for (int j = 1; j < routes.get(vehicle).size() - 1; j++) { //iterate without depot: <1, N-1>
                City foundCity = routes.get(vehicle).get(j);
                decodedResult[i][cities.indexOf(foundCity)] = j;
            }
        }
        return decodedResult;
    }

    private void initDecoders(int numberOfVehicle, int numberOfCity) {
        decodedResult = new Integer[numberOfVehicle][numberOfCity];
        for (int i = 0; i < numberOfVehicle; i++) {
            for (int j = 0; j < numberOfCity; j++) {
                decodedResult[i][j] = 0;
            }
        }
    }

    public List<Integer> decodeResult2(Map<Vehicle, List<City>> routes) {
        List<Integer> decodedResult2 = new ArrayList<>();
        cutPoints = new ArrayList<>();
        int cutPoint = 0;
        routes = sortRoutesByVehicleId(routes);
        for (int i = 0; i < routes.keySet().size(); i++) {

            Vehicle vehicle = (Vehicle) routes.keySet().toArray()[i];
            List<City> route = routes.get(vehicle);
            route.removeIf(r -> r.getId() == 0);
            for (City city : route) {
                decodedResult2.add(city.getId());
                cutPoint++;
            }
            route.add(0, depot);
            route.add(depot);
            cutPoints.add(cutPoint);
        }
        return decodedResult2;

    }
}
