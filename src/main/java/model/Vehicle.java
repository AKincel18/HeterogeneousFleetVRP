package model;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ToString(callSuper = true, exclude = "route")
@SuperBuilder
public class Vehicle extends ModelCommon {
    @Getter private int id;
    private List<City> route;
}
