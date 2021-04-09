package utils;

import commons.Result;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

import static utils.Utils.countSumOfResult;

@RequiredArgsConstructor
public class Encoder {

    private final List<Vehicle> vehicles;
    private final List<City> cities;
    private final City depot;
    private final Integer[][] decodedResult;
    private List<Integer> cutPoints;
    private Integer[] decodedResult2;

    public Encoder(List<Vehicle> vehicles, List<City> cities, City depot, Integer[][] decodedResult,
                   List<Integer> cutPoints, Integer[] decodedResult2) {
        this.vehicles = vehicles;
        this.cities = cities;
        this.depot = depot;
        this.decodedResult = decodedResult;
        this.cutPoints = cutPoints;
        this.decodedResult2 = decodedResult2;
    }

    public Result encodeResult() {
        Map<Vehicle, List<City>> routes = new HashMap<>();
        for (int i = 0; i < decodedResult.length; i++) {
            final int finalI = i;
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == finalI).findFirst().orElseThrow();
            List<City> route = new ArrayList<>(Collections.singleton(depot)); //set depot on first in a route
            List<PlaceCity> placeCities = new ArrayList<>();
            for (int j = 0; j < cities.size(); j++) {
                if (decodedResult[i][j] != 0) {
                    placeCities.add(new PlaceCity(cities.get(j), decodedResult[i][j]));
                }
            }
            placeCities = placeCities.stream()
                    .sorted(Comparator.comparingInt(PlaceCity::getPlace))
                    .collect(Collectors.toList());

            route.addAll(placeCities.stream().map(PlaceCity::getCity).collect(Collectors.toList()));
            route.add(depot); //set depot on last in a route
            routes.put(vehicle, route);
        }
        return new Result(routes, countSumOfResult(routes));
    }

    public Result encodeResult2() {
        Map<Vehicle, List<City>> routes = new HashMap<>();
        int index = 0, leftRange = 0, rightRange;
        for (int i = 0; i < vehicles.size(); i++) {
            final int finalI = i;
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == finalI).findFirst().orElseThrow();
            List<City> route = new ArrayList<>(Collections.singleton(depot)); //set depot on first in a route
            rightRange = cutPoints.get(i);
            for (int j = leftRange; j < rightRange; j++) {
                int cityId = decodedResult2[index];
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

    static class PlaceCity {

        private final City city;
        private final int place;

        public PlaceCity(City city, int place) {
            this.city = city;
            this.place = place;
        }

        public City getCity() {
            return city;
        }

        public int getPlace() {
            return place;
        }
    }
}
