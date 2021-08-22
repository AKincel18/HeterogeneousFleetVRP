package validators;

import exceptions.NotValidDataException;
import model.City;
import model.Vehicle;

import java.util.List;

import static constants.StringConst.AMOUNT_HEADER_ERROR;

public class AmountValidator {

    public void validateAmount(List<Vehicle> vehicles, List<City> cities) throws NotValidDataException {
        double vehicleSumAmount = vehicles.stream().mapToDouble(v -> v.getAmount()).sum();
        double citiesSumAmount = cities.stream().mapToDouble(c -> c.getAmount()).sum();
        if (citiesSumAmount > vehicleSumAmount)
            throw new NotValidDataException(AMOUNT_HEADER_ERROR);
    }
}
