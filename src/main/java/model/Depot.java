package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
public class Depot {
    @Getter @Setter private Coords coords = new Coords();
    @Getter @Setter private String name;
}
