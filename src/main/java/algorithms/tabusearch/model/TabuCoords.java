package algorithms.tabusearch.model;

import lombok.Getter;
import lombok.ToString;

@ToString(exclude = {"rowTabu", "columnTabu"})
public class TabuCoords {
    @Getter private final int vehicleHigher;
    @Getter private final int cityHigher;
    @Getter private final int vehicleLower;
    @Getter private final int cityLower;
    @Getter private final int rowTabu;
    @Getter private final int columnTabu;

    public TabuCoords(int vehicle, int city, int vehicle2, int city2, int cityNumber) {
        vehicleHigher = Math.max(vehicle, vehicle2);
        vehicleLower = Math.min(vehicle, vehicle2);

        if (vehicleHigher == vehicleLower) {
            cityHigher = Math.max(city, city2);
            cityLower = Math.min(city, city2);
        }
        else {
            cityHigher = vehicleHigher == vehicle ? city : city2;
            cityLower = vehicleLower == vehicle ? city : city2;
        }

        rowTabu = vehicleLower * cityNumber + cityLower;
        columnTabu = vehicleHigher * cityNumber + cityHigher;
    }

}
