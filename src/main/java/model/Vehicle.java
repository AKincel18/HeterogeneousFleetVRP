package model;

import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class Vehicle extends ModelCommon {
    private List<City> route;
}
