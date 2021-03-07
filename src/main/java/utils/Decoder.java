package utils;

import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;

import java.util.List;
import java.util.Map;

import static utils.Utils.sortRoutesByVehicleId;

@RequiredArgsConstructor
public class Decoder {

    private final List<City> cities;
    private Integer[][] decodedResult;

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
}
