package net.wmann.fileanalyser.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ExampleFileControllerApi {

    @ApiOperation(value = "Returns example csv speech file",
            notes = "Returns an example csv file which can be used to test the speech analyser endpoint.\n" +
                    "Valid prefixes are: 'valid' and 'invalid'; returning a valid or invalid example file respectively",
            response = ResponseEntity.class,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of example file", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity exampleFile(String prefix) throws IOException;
}
