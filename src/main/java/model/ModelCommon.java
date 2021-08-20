package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
abstract class ModelCommon {
    protected String name;
    protected Double amount;
    protected int id;

}
