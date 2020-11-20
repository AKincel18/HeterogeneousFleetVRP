package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class ModelCommon {
    protected String name;
    protected Double amount;

    public boolean isNotNull(){
        return name != null || amount != null;
    }
}
