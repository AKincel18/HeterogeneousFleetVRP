package model;

import constants.StringConst;
import exceptions.NotValidDataException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
abstract class ModelCommon {
    protected String name;
    protected Double amount;

    public void validData() throws NotValidDataException {
        if (name == null && amount != null)
            throw new NotValidDataException(StringConst.NAME_NOT_VALID);
        if (name != null && amount == null)
            throw new NotValidDataException(StringConst.AMOUNT_NOT_VALID);
    }

    public boolean isNull() {
        return name == null && amount == null;
    }
}
