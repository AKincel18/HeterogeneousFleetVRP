package model;

import constants.StringConst;
import exceptions.NotValidDataException;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
abstract class ModelCommon {
    protected String name;
    protected Double amount;
    protected int id;

    public void validData() throws NotValidDataException {
        if (name == null && amount != null)
            throw new NotValidDataException(StringConst.NAME_NOT_VALID, null);
        if (name != null && amount == null)
            throw new NotValidDataException(StringConst.AMOUNT_NOT_VALID, null);
    }

    public boolean isNull() {
        return name == null && amount == null;
    }
}
