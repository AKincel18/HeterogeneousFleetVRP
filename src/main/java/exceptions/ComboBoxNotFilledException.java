package exceptions;

public class ComboBoxNotFilledException extends InputException {

    public ComboBoxNotFilledException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
