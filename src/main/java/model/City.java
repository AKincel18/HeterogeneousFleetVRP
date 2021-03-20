package model;

import constants.StringConst;
import exceptions.NotValidDataException;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
public class City extends ModelCommon {
    @Getter @Setter private boolean isVisited;
    @Getter @Setter private Coords coords;


/*    @Builder
    public City(String name, Double amount, boolean isVisited, Coords coords) {
        super(name, amount);
        this.isVisited = isVisited;
        this.coords = coords;
    }*/

    public void validDataCity() throws NotValidDataException {
        validData();
        if (coords.getLongitude() == null && coords.getLatitude() != null)
            throw new NotValidDataException(StringConst.LONGITUDE_NOT_VALID);

        if (coords.getLongitude() != null && coords.getLatitude() == null) {
            throw new NotValidDataException(StringConst.LATITUDE_NOT_VALID);
        }
    }

    public boolean isNullCity() {
        return coords.getLatitude() == null && coords.getLongitude() == null && isNull();
    }



}
