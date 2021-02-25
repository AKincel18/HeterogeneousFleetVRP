package utils;

import algorithms.genetic.Individual;
import model.City;
import model.Depot;
import model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Writer {

    public static void writeInputData(List<City> cities, List<Vehicle> vehicles, Depot depot) {
        buildTitleOnConsole("Cities");
        cities.forEach(c -> System.out.println(c.toString()));
        buildTitleOnConsole("Vehicles");
        vehicles.forEach(v -> System.out.println(v.toString()));
        buildTitleOnConsole("Depot");
        System.out.println(depot.toString());
    }

    public static void writePopulation(List<Individual> population) {
        AtomicInteger index = new AtomicInteger();
        population.forEach(
                p -> {
                    buildTitleOnConsole("Generated " + index.getAndIncrement() + " individual");
                    p.getIndividual().forEach((vehicle, cities) -> {


                        System.out.print("Vehicle: " + vehicle.getName() + " = ");
                        if (cities.isEmpty())
                            System.out.print("No cities ");
                        else
                            cities.forEach(city -> System.out.print(city.getName() + " "));
                        System.out.print("quality = " + Utils.countQuality(vehicle, cities));
                        System.out.println(", distance = " + Utils.countRouteDistance(cities));

                    });
                    System.out.println("Sum of distance = " + Utils.roundNumber(p.getSum()));
                }

        );
    }

    public static void writeTestDecodingAndEncoding(List<Individual> population, List<City> cities, int vehicleNumber, Depot depot) {
        for (Individual individual : population) {
            Decoder decoder = new Decoder(cities);
            Integer [][] array = decoder.decodeIndividual(individual.getIndividual());

            buildTitleOnConsole("Test decoding " + individual.getId());
            for (int i =0; i < vehicleNumber; i++) {
                System.out.println("Vehicle = " + individual.getIndividual().keySet().toArray()[i].toString());
                for (int j = 0; j < cities.size(); j++) {
                    System.out.print(array[i][j] + ";");
                }
                System.out.println();
            }

            buildTitleOnConsole("Test encoding " + individual.getId());
            Encoder encoder = new Encoder(cities, array);
            Map<Vehicle, List<City>> map = encoder.encodeIndividual(individual.getIndividual(), Utils.getDepotByCity(depot));
            for (Map.Entry<Vehicle, List<City>> entry : map.entrySet()) {
                System.out.println("Vehicle = " + entry.getKey());
                entry.getValue().forEach(city -> System.out.print(city.getName() + " "));
                System.out.println();System.out.println();
            }
        }
    }

    public static void buildTitleOnConsole(String title) {
        int numChars = title.length();
        int numSpaces = 20;
        buildSpaces(numSpaces + numChars + numSpaces); System.out.println();
        buildSpaces(numSpaces); System.out.print(title); buildSpaces(numSpaces); System.out.println();
        buildSpaces(numSpaces + numChars + numSpaces); System.out.println();
    }

    private static void buildSpaces(int num) {
        for (int i = 0; i < num; i++) {
            System.out.print("-");
        }
    }
}
