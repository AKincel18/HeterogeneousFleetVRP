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

    private final List<City> cities;
    private final Integer[][] decodedResult;

    public Result encodeResult(List<Vehicle> vehicles, City depot) {
        Map<Vehicle, List<City>> routes = new HashMap<>();
        for (int i = 0; i < decodedResult.length; i++) {
            final int finalI = i;
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == finalI).findFirst().orElse(null);
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
