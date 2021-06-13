package exceptions;

public class SaveLocationNotGivenException extends InputException {

    public SaveLocationNotGivenException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
