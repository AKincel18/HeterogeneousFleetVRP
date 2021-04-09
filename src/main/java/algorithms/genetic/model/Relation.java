package algorithms.genetic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Relation {
    private int a, b;

    @Override
    public String toString() {
        return "(" + a + ";" + b + ")";
    }
}
