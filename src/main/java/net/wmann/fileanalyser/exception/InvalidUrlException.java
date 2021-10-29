package net.wmann.fileanalyser.exception;

import java.util.List;

public class InvalidUrlException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "The following URLs are not valid: ";

    public InvalidUrlException(List<String> invalidUrls) {
        super(ERROR_MESSAGE + String.join(", ", invalidUrls));
    }
}
