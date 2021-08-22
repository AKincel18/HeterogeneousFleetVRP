package validators;

import exceptions.NotValidDataException;
import model.City;

import static constants.StringConst.*;

public class CityValidator {

    public void validateCity(City validatedCity, int rowCounter) throws NotValidDataException {

        if (validatedCity.getName() == null)
            throw new NotValidDataException(CITY_NAME_NOT_GIVEN_HEADER_ERROR + rowCounter);

        if (validatedCity.getAmount() == null)
            throw new NotValidDataException(CITY_AMOUNT_NOT_GIVEN_HEADER_ERROR + rowCounter);

        validateCoords(validatedCity.getCoords().getLongitude(), validatedCity.getCoords().getLatitude(), rowCounter);
    }

    protected void validateCoords(Double longitude, Double latitude, int rowCounter) throws NotValidDataException {
        if (latitude == null)
            if (this instanceof DepotValidator)
                throw new NotValidDataException(DEPOT_LATITUDE_NOT_GIVEN_HEADER_ERROR + rowCounter);
            else
                throw new NotValidDataException(CITY_LATITUDE_NOT_GIVEN_HEADER_ERROR + rowCounter);

        if (longitude == null)
            if (this instanceof DepotValidator)
                throw new NotValidDataException(DEPOT_LONGITUDE_NOT_GIVEN_HEADER_ERROR + rowCounter);
            else
                throw new NotValidDataException(CITY_LONGITUDE_NOT_GIVEN_HEADER_ERROR + rowCounter);
    }

}
