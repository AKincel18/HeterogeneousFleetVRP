package commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

public class TextFieldDouble extends TextField {

    private final ObjectProperty<Double> number = new SimpleObjectProperty<>();

    //default values
    private double minValue = 0.0;
    private double maxValue = 1.0;

    @SuppressWarnings("unused")
    public TextFieldDouble() {
        super();
        initHandlers();
        setValue(0.0);
    }

    public final Double getValue() {
        return number.get();
    }

    public final void setValue(Double value) {
        minValue = value;
        number.set(value);
    }

    @SuppressWarnings("unused")
    public final double getMaxValue() {
        return maxValue;
    }

    @SuppressWarnings("unused")
    public final void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public ObjectProperty<Double> numberProperty() {
        return number;
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

    private void parseAndFormatInput() {
        String input = getText();
        if (input == null || input.length() == 0) {
            return;
        }

        double newValue;
        try {
            newValue = Double.parseDouble(input);
            if (newValue > maxValue) {
                number.set(maxValue);
            } else number.set(Math.max(newValue, minValue));
            setText(String.valueOf(number.get()));
            selectAll();

        } catch (NumberFormatException e) {
            setText(String.valueOf(number.get()));

        }
    }

}
