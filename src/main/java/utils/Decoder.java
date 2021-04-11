package utils;

import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.*;

import static utils.Utils.countSumOfResult;

@RequiredArgsConstructor
public class Decoder {

    private final List<Vehicle> vehicles;
    private final List<City> cities;
    private final City depot;

    public Result decodeResultFromArray(Integer[] codedResult, Integer[] cutPoints) {
        Map<Vehicle, List<City>> routes = new HashMap<>();
        int index = 0, leftRange = 0, rightRange;
        for (int i = 0; i < vehicles.size(); i++) {
            final int finalI = i;
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == finalI).findFirst().orElseThrow();
            List<City> route = new ArrayList<>(Collections.singleton(depot)); //set depot on first in a route
            rightRange = cutPoints[i];
            for (int j = leftRange; j < rightRange; j++) {
                int cityId = codedResult[index];
                City city = cities.stream().filter(c -> c.getId() == cityId).findFirst().orElseThrow();
                route.add(city);
                index++;
            }
            leftRange = rightRange;
            route.add(depot); //set depot on last in a route
            routes.put(vehicle, route);
        }
        return new Result(routes, countSumOfResult(routes));
    }

    public Result decodeResultFromMap(Map<Integer, List<Integer>> codedResult) {
        Map<Vehicle, List<City>> routes = new HashMap<>();

        codedResult.forEach((vehicleDecoded, citiesDecoded) -> {
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == vehicleDecoded).findFirst().orElseThrow();
            List<City> route = new ArrayList<>(Collections.singleton(depot)); //set depot on first in a route
            citiesDecoded.forEach(cityDecoded -> {
                City city = cities.stream().filter(c -> c.getId() == cityDecoded).findFirst().orElseThrow();
                route.add(city);
            });
            route.add(depot); //set depot on last in a route
            routes.put(vehicle, route);
        });

        return new Result(routes, countSumOfResult(routes));
    }
}
