package algorithms.localsearch.model;

public enum LocalSearchMethod {
    GREEDY ("Greedy"),
    STEEPEST ("Steepest");

    private final String name;

    LocalSearchMethod(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
