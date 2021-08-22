package commons.client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

public class TextFieldInteger extends TextField {

    private final ObjectProperty<Integer> number = new SimpleObjectProperty<>();

    //default values
    private int minValue = 0;
    private boolean even = false;

    @SuppressWarnings("unused")
    public TextFieldInteger() {
        super();
        initHandlers();
        setValue(0);
    }

    public final Integer getValue() {
        return number.get();
    }

    public final void setValue(Integer value) {
        minValue = value;
        number.set(value);
    }

    @SuppressWarnings("unused")
    public final Boolean getEven() {
        return even;
    }

    @SuppressWarnings("unused")
    public final void setEven(Boolean even) {
        this.even = even;
    }

    public ObjectProperty<Integer> numberProperty() {
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

        int newValue;
        try {
            newValue = Integer.parseInt(input);
            newValue = even ? getEvenValue(newValue) : newValue;
            number.set(Math.max(newValue, minValue));
            setText(String.valueOf(number.get()));
            selectAll();
        } catch (NumberFormatException e) {
            setText(String.valueOf(number.get()));

        }
    }

    private int getEvenValue(int newValue) {
        if (newValue % 2 != 0) {
            return ++newValue;
        }
        return newValue;
    }

}
