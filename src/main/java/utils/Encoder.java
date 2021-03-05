package utils;

import algorithms.genetic.model.Individual;
import model.City;
import model.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

import static utils.Utils.countSumOfIndividual;

public class Encoder {

    private final List<City> cities;
    private final Integer[][] decodedIndividual;

    public Encoder(List<City> cities, Integer[][] decodedIndividual) {
        this.cities = cities;
        this.decodedIndividual = decodedIndividual;
    }

    public Individual encodeIndividual(List<Vehicle> vehicles, City depot, int id) {
        Map<Vehicle, List<City>> individualRoutes = new HashMap<>();
        for (int i = 0; i < decodedIndividual.length; i++) {
            final int finalI = i;
            Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == finalI).findFirst().orElse(null);
            List<City> route = new ArrayList<>(Collections.singleton(depot)); //set depot on first in a route
            List<PlaceCity> placeCities = new ArrayList<>();
            for (int j = 0; j < cities.size(); j++) {
                if (decodedIndividual[i][j] != 0) {
                    placeCities.add(new PlaceCity(cities.get(j), decodedIndividual[i][j]));
                }
            }
            placeCities = placeCities.stream()
                    .sorted(Comparator.comparingInt(PlaceCity::getPlace))
                    .collect(Collectors.toList());

            route.addAll(placeCities.stream().map(PlaceCity::getCity).collect(Collectors.toList()));
            route.add(depot); //set depot on last in a route
            individualRoutes.put(vehicle, route);
        }
        Individual individual = new Individual(individualRoutes, depot, id);
        individual.setSum(countSumOfIndividual(individual));
        return individual;
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
