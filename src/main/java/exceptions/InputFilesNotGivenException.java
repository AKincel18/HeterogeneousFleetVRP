package exceptions;

public class InputFilesNotGivenException extends InputException {

    public InputFilesNotGivenException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
