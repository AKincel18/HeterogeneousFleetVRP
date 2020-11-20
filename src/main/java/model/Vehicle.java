package model;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class Vehicle extends ModelCommon {
    private List<City> route;
}
