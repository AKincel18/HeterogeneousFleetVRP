package commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

public class TextFieldDouble extends TextField {

    private final ObjectProperty<Double> number = new SimpleObjectProperty<>();
    private double minValue = 0.0;
    private double maxValue = 1.0;

    public final Double getValue() {
        return number.get();
    }

    public final void setValue(Double value) {
        number.set(value);
    }

    public ObjectProperty<Double> numberProperty() {
        return number;
    }

    public void setFieldRanges(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @SuppressWarnings("unused")
    public TextFieldDouble() {
        this(0.0);
    }


    public TextFieldDouble(Double value) {
        super();
        initHandlers();
        setValue(value);
    }

    private void initHandlers() {

        setOnAction(event -> parseAndFormatInput());

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                parseAndFormatInput();
            }
        });

        numberProperty().addListener((observable, oldValue, newValue) -> setText(String.valueOf(newValue)));
    }

    /**
     * Tries to parse the user input to a number according to the provided
     * NumberFormat
     */
    private void parseAndFormatInput() {
        String input = getText();
        if (input == null || input.length() == 0) {
            return;
        }

        double newValue;
        try {
            newValue = Double.parseDouble(input);
            if (newValue > maxValue) {
                setValue(maxValue);
            } else setValue(Math.max(newValue, minValue));
            setText(String.valueOf(number.get()));
            selectAll();

        } catch (NumberFormatException e) {
            setText(String.valueOf(number.get()));

        }
    }

}
