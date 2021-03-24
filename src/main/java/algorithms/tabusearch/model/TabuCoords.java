package algorithms.tabusearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TabuCoords {
    private int vehicleHigher;
    private int cityHigher;
    private int vehicleLower;
    private int cityLower;
    private int rowTabu;
    private int columnTabu;

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

    public boolean isSameCoords(TabuCoords coords) {
        return this.columnTabu == coords.getColumnTabu() && this.rowTabu == coords.getRowTabu();
    }
    @Override
    public String toString() {
        return "[" + rowTabu + ", " + columnTabu + "]";
    }
}
