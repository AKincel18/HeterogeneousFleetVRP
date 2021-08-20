package commons.algorithms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.City;
import model.Vehicle;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class Result {
    private Map<Vehicle, List<City>> routes;
    private Double sum;

    public Result(Map<Vehicle, List<City>> routes, Double sum) {
        this.routes = routes;
        this.sum = sum;
    }

    public Result(Result currentResult) {
        this.routes = currentResult.getRoutes();
        this.sum = currentResult.getSum();
    }

    public boolean isBetter(Result result) {
        return sum < result.getSum();
    }
}
