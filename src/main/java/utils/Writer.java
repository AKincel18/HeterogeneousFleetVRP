package utils;

import algorithms.genetic.model.Individual;
import commons.Result;
import model.City;
import model.Depot;
import model.Vehicle;

import java.util.HashMap;
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
                    writeResult(p.getResult());
                }

        );
    }

    public static void writeResult(Result result) {
        result.setRoutes(sortRoutesByVehicleId(result.getRoutes())); //sort routes
        result.getRoutes().forEach((vehicle, cities) -> {


            System.out.print("Vehicle: " + vehicle.getName() + " = ");
            cities.forEach(city -> System.out.print(city.getName() + "(" + city.getId() + ")" + " "));
            System.out.print("; quality = " + Utils.countQuality(vehicle, cities));
            System.out.println(" ; distance = " + Utils.countRouteDistance(cities));

        });
        System.out.println("Total distance = " + Utils.roundNumber(result.getSum()));

    }

    public static void writeDecodedResultInOneRow(Integer[][] decodedResult) {
        for (Integer[] row : decodedResult) {
            for (Integer number : row) {
                System.out.print(number + ";");
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

    public static void writeTabuArray(int[][] tabuArray) {

//        System.out.printf("%-10s", "");
//        for (int i = 0; i < tabuArray.length; i++) {
//            for (int j = 0; j < tabuArray[i].length; j++) {
//                String text = String.valueOf(i) + String.valueOf(j);
//                System.out.printf("%-10s", text);
//            }
//        }
//
//        System.out.println();
//        System.out.println();


        for (int i = 0; i < tabuArray.length; i++) {
            //System.out.printf("%-10s", String.valueOf(vec) + String.valueOf(city));
            for (int j = 0; j < tabuArray[i].length; j++) {
                if (i == j) {
                    System.out.printf("%-10s", 'X');
                    //System.out.print("X     ");
                }
                else {
                    System.out.printf("%-10s", tabuArray[i][j]);
                    //System.out.print(tabuArray[i][j] + "     ");
                }
            }
            System.out.println();
        }
    }

    public static void writeTabuStats(int[][] tabu) {
        for (int[] row : tabu) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int number : row) {
                if (number != 0) {
                    map.merge(number, 1, Integer::sum);
                }
            }
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", values = " + entry.getValue());
            }
            System.out.println("---------------------------");
        }
    }

    public static void checkSumFreq(int[][] tabu) {
        int sum = 0;
        for (int i = 0; i < tabu.length; i++) {
            for (int j = 0; j < i; j++) {
                sum += tabu[i][j];
            }
        }

        System.out.println("Suma = " + sum);
    }

    public static void checkSumFreq_2(int[][] tabu) {
        int sum = 0;
        for (int[] row : tabu) {
            for (int n : row) {
                sum += n;
            }
        }
        System.out.println("Suma = " + sum);
    }

    public static void writeBigNumber(int[][] tabu) {
        int max = -1;
        int rowNum = 0;
        int maxRow = -1;
        for (int[] row : tabu) {
            for (int number : row) {
                if (max < number)
                {
                    maxRow = rowNum;
                    max = number;
                }
            }
            rowNum++;
        }
        System.out.println("max num = " + max + ", max row = " + maxRow);
    }

    public static void writeDecodedResultInOneRow(List<Integer> decodedResult, Integer[] cutPoints) {
        int leftRange = 0, rightRange, index = 0;
        for (Integer cutPoint : cutPoints) {
            rightRange = cutPoint;
            StringBuilder output = new StringBuilder();
            for (int j = leftRange; j < rightRange; j++) {
                output.append(decodedResult.get(index)).append(";");
                index++;
            }
            leftRange = rightRange;
            output.deleteCharAt(output.length() - 1).append("|");
            System.out.print(output);
        }
        System.out.println();
    }
}
