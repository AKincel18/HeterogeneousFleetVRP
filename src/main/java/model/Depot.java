package model;

import lombok.Data;

@Data
public class Depot {
    private Coords coords = new Coords();
    private String name;
}
