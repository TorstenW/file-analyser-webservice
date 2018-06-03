package net.wmann.fileanalyser.exception;

import java.util.List;

public class InvalidUrlException extends IllegalArgumentException {

    private List<String> invalidUrls;

    private static final String ERROR_MESSAGE = "The following URLs are not valid: ";

    public InvalidUrlException(List<String> invalidUrls) {
        this.invalidUrls = invalidUrls;
    }

    public String getErrorMessage() {
        return ERROR_MESSAGE + String.join(", ", invalidUrls);
    }

}
