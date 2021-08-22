package validators;

import exceptions.NotValidDataException;
import model.Vehicle;

import static constants.StringConst.VEHICLE_AMOUNT_NOT_GIVEN_HEADER_ERROR;
import static constants.StringConst.VEHICLE_NAME_NOT_GIVEN_HEADER_ERROR;

public class VehicleValidator {

    public void validateVehicle(Vehicle validatedVehicle, int rowCounter) throws NotValidDataException {

        if (validatedVehicle.getName() == null && validatedVehicle.getAmount() != null)
            throw new NotValidDataException(VEHICLE_NAME_NOT_GIVEN_HEADER_ERROR + rowCounter);

        if (validatedVehicle.getName() != null && validatedVehicle.getAmount() == null)
            throw new NotValidDataException(VEHICLE_AMOUNT_NOT_GIVEN_HEADER_ERROR + rowCounter);
    }

    public boolean isVehicle(Vehicle vehicle) {
        return vehicle.getAmount() != null && vehicle.getName() != null;
    }
}
