package exceptions;

import static constants.StringConst.INPUT_DATA_IS_NOT_VALID_HEADER_ERROR;

public class NotValidDataException extends InputException {

    public NotValidDataException(String contentError) {
        super(INPUT_DATA_IS_NOT_VALID_HEADER_ERROR, contentError);
    }

    public NotValidDataException(String headerError, String contentError) {
        super(headerError, contentError);
    }
}
