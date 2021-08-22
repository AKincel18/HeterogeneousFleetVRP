package commons.client;

import exceptions.NotValidDataException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValidationResult {
    private final boolean isValid;
    private final NotValidDataException exception;
}
