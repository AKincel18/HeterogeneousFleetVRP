package validators;

import exceptions.NotValidDataException;
import model.Depot;

import static constants.StringConst.DEPOT_NAME_NOT_GIVEN_HEADER_ERROR;

public class DepotValidator extends CityValidator {

    public void validateDepot(Depot validatedDepot, int rowCounter) throws NotValidDataException {

        if (validatedDepot.getName() == null)
            throw new NotValidDataException(DEPOT_NAME_NOT_GIVEN_HEADER_ERROR + rowCounter);

        validateCoords(validatedDepot.getCoords().getLongitude(), validatedDepot.getCoords().getLatitude(), rowCounter);
    }
}
