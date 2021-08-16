package model;

import constants.StringConst;
import exceptions.NotValidDataException;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
public class City extends ModelCommon {
    private boolean isVisited;
    private Coords coords;


/*    @Builder
    public City(String name, Double amount, boolean isVisited, Coords coords) {
        super(name, amount);
        this.isVisited = isVisited;
        this.coords = coords;
    }*/

    public void validDataCity() throws NotValidDataException {
        validData();
        if (coords.getLongitude() == null && coords.getLatitude() != null)
            throw new NotValidDataException(StringConst.LONGITUDE_NOT_VALID, null);

        if (coords.getLongitude() != null && coords.getLatitude() == null) {
            throw new NotValidDataException(StringConst.LATITUDE_NOT_VALID, null);
        }
    }

    public boolean isNullCity() {
        return coords.getLatitude() == null && coords.getLongitude() == null && isNull();
    }



}
