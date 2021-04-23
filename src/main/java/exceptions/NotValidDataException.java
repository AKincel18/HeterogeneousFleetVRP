package exceptions;

public class NotValidDataException extends InputException {

    public NotValidDataException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
