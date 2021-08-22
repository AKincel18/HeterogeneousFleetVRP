package model;

import lombok.Data;

@Data
public class Coords {
    private Double latitude;
    private Double longitude;

    public Coords() {
    }

    public Coords(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
