package model;

import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ToString(callSuper = true, exclude = "route")
@SuperBuilder
public class Vehicle extends ModelCommon {
    //@Getter private final int id;
    private final List<City> route;
}
