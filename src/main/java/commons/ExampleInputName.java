package commons;

public enum ExampleInputName {
    DATA1 ("Data1"),
    DATA2 ("Data2"),
    DATA3 ("Data3");

    private final String name;

    ExampleInputName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
