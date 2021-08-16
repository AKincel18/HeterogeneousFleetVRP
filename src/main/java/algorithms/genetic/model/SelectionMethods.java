package algorithms.genetic.model;

public enum SelectionMethods {
    ROULETTE_WHEEL ("Roulette wheel"),
    RANK ("Rank"),
    TOURNAMENT("Tournament");

    private final String name;

    SelectionMethods(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
