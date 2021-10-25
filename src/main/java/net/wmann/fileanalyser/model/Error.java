package net.wmann.fileanalyser.model;

public record Error(String error, String exception, String message) {

    // For json deserialization only
    private Error() {
        this(null, null, null);
    }
}
