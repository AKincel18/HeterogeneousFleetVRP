package exceptions;

import lombok.Getter;

@Getter
public abstract class InputException extends Exception {
    private final String headerError;
    private final String contentError;

    public InputException(String headerError, String contentError) {
        super();
        this.headerError = headerError;
        this.contentError = contentError;
    }
}
