package net.wmann.fileanalyser.dto;

import net.wmann.fileanalyser.model.Error;

import java.util.ArrayList;
import java.util.List;

public record ErrorListDto(int errorsFound, List<Error> errors) implements ResponseDto {

    public ErrorListDto(List<Error> errors) {
        this(errors.size(), new ArrayList<>(errors));
    }

    public ErrorListDto(Error... errors) {
        this(errors.length, List.of(errors));
    }
}
