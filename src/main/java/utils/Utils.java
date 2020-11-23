package utils;

import constants.StringConst;
import model.Coords;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    /**
     * https://www.geeksforgeeks.org/program-distance-two-points-earth/
     * @param c1 coords1
     * @param c2 coords2
     * @return distance between two coords earth (in kilometers)
     */
    public static Double countDistance(Coords c1, Coords c2){
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        double lon1 = Math.toRadians(c1.getLongitude());
        double lon2 = Math.toRadians(c2.getLongitude());
        double lat1 = Math.toRadians(c1.getLatitude());
        double lat2 = Math.toRadians(c2.getLatitude());

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    public static void buildTitleOnConsole(String title) {
        System.out.println(StringConst.SPACE_MARKER_BIG);
        System.out.println(StringConst.SPACE_MARKER_SMALL + title + StringConst.SPACE_MARKER_SMALL);
        System.out.println(StringConst.SPACE_MARKER_BIG);
    }

    public static List<Integer> generateListOfNumbers(int size) {
        List<Integer> numbers = IntStream.rangeClosed(0, size - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
        return numbers;
    }
}
