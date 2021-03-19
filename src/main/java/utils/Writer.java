package utils;

import algorithms.genetic.model.Individual;
import commons.Result;
import model.City;
import model.Depot;
import model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.Utils.sortRoutesByVehicleId;

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
                    buildTitleOnConsole("Individual: " + index.getAndIncrement());
                    writeResult(new Result(p.getRoutes(), p.getSum()));
                }

        );
    }

    public static void writeResult(Result result) {
        result.setRoutes(sortRoutesByVehicleId(result.getRoutes())); //sort routes
        result.getRoutes().forEach((vehicle, cities) -> {


            System.out.print("Vehicle: " + vehicle.getName() + " = ");
            if (cities.isEmpty())
                System.out.print("No cities ");
            else
                cities.forEach(city -> System.out.print(city.getName() + " "));
            System.out.print("quality = " + Utils.countQuality(vehicle, cities));
            System.out.println(", distance = " + Utils.countRouteDistance(cities));

        });
        System.out.println("Sum of distance = " + Utils.roundNumber(result.getSum()));

    }

    public static void writeTestDecodingAndEncodingPopulation(List<Individual> population, List<City> cities,
                                                              List<Vehicle> vehicles, Depot depot) {
        //int id = 0;
        for (Individual individual : population) {
            buildTitleOnConsole("Test decoding & encoding = " + individual.getId());
            writeTestDecodingAndEncoding(cities, vehicles, depot, individual.getRoutes());
        }
    }

    public static void writeTestDecodingAndEncoding(List<City> cities, List<Vehicle> vehicles, Depot depot,
                                                    Map<Vehicle, List<City>> routes) {
        Decoder decoder = new Decoder(cities);
        Integer [][] array = decoder.decodeResult(routes);

        buildTitleOnConsole("Decoding");
        for (int i =0; i < vehicles.size(); i++) {
            System.out.println("Vehicle = " + routes.keySet().toArray()[i].toString());
            for (int j = 0; j < cities.size(); j++) {
                System.out.print(array[i][j] + ";");
            }
            System.out.println();
        }

        buildTitleOnConsole("Encoding");
        Encoder encoder = new Encoder(cities, array);
        Result result = encoder.encodeResult(vehicles, Utils.getDepotByCity(depot));
        for (Map.Entry<Vehicle, List<City>> entry : result.getRoutes().entrySet()) {
            System.out.println("Vehicle = " + entry.getKey());
            entry.getValue().forEach(city -> System.out.print(city.getName() + " "));
            System.out.println();System.out.println();
        }
    }

    public static void writeDecodedResultInOneRow(Integer[][] decodedResult) {
        for (Integer[] row : decodedResult) {
            for (Integer number : row) {
                System.out.print(number);
            }
            System.out.print("|");
        }
        System.out.println();
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

    public static void writeTabuArray(int[][] tabuArray, int vecNumber, int cityNumber) {

        System.out.printf("%-10s", "");
        for (int i = 0; i < vecNumber; i++) {
            for (int j = 0; j < cityNumber; j++) {
                String text = String.valueOf(i) + String.valueOf(j);
                System.out.printf("%-10s", text);
            }
        }

        System.out.println();
        System.out.println();

        int vec = 0;
        int city = 0;

        for (int i = 0; i < tabuArray.length; i++) {
            System.out.printf("%-10s", String.valueOf(vec) + String.valueOf(city));
            for (int j = 0; j < tabuArray[i].length; j++) {
                if (i == j) {
                    System.out.printf("%-10s", 'X');
                    //System.out.print("X     ");
                }
                else {
                    System.out.printf("%-10s", String.valueOf(tabuArray[i][j]));
                    //System.out.print(tabuArray[i][j] + "     ");
                }
            }
            city++;
            if (city == cityNumber) {
                city = 0;
                vec++;
            }
            System.out.println();
        }
    }
}
