package net.wmann.fileanalyser.model;

import lombok.Data;

@Data
public class Error {

    private final String error;

    private final String exception;

    private final String message;

    // For json deserialization only
    private Error() {
        error = exception = message = null;
    }

    public Error(String error, String exception, String message) {
        this.error = error;
        this.exception = exception;
        this.message = message;
    }
}
