package exceptions;

public class OutputFileNotGivenException extends InputException {

    public OutputFileNotGivenException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
