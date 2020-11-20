package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class City extends ModelCommon {
    @Getter @Setter private boolean isVisited;
}
