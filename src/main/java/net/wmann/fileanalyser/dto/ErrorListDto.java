package net.wmann.fileanalyser.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.wmann.fileanalyser.model.Error;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ErrorListDto implements ResponseDto {

    private final int errorsFound;
    private final List<Error> errors = new ArrayList<>();

    // For json deserialization only
    private ErrorListDto() {
        errorsFound = 0;
    }

    public ErrorListDto(List<Error> errors) {
        this.errors.addAll(errors);
        errorsFound = errors.size();
    }

    public ErrorListDto(Error... errors) {
        for(Error error : errors) {
            this.errors.add(error);
        }
        errorsFound = errors.length;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public int getErrorsFound() {
        return errorsFound;
    }
}
