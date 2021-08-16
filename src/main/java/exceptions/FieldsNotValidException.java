package exceptions;

public class FieldsNotValidException extends InputException {

    public FieldsNotValidException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
