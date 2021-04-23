package commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

public class TextFieldInteger extends TextField {

    private final ObjectProperty<Integer> number = new SimpleObjectProperty<>();

    public final Integer getValue() {
        return number.get();
    }

    public final void setValue(Integer value) {
        number.set(value);
    }

    public ObjectProperty<Integer> numberProperty() {
        return number;
    }

    @SuppressWarnings("unused")
    public TextFieldInteger() {
        this(0);
    }


    public TextFieldInteger(Integer value) {
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

    private void parseAndFormatInput() {
        String input = getText();
        if (input == null || input.length() == 0) {
            return;
        }

        int newValue;
        try {
            newValue = Integer.parseInt(input);
            setValue(Math.max(newValue, 0));
            setText(String.valueOf(number.get()));
            selectAll();
        } catch (NumberFormatException e) {
            setText(String.valueOf(number.get()));

        }
    }

}
