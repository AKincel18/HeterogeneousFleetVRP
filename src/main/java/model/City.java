package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
public class City extends ModelCommon {
    private boolean isVisited;
    private Coords coords;
}
