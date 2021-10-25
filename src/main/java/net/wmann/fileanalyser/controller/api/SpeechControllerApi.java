package net.wmann.fileanalyser.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.wmann.fileanalyser.dto.ErrorListDto;
import net.wmann.fileanalyser.dto.EvaluationDto;
import net.wmann.fileanalyser.dto.ResponseDto;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface SpeechControllerApi {

    @ApiOperation(value = "Evaluates cvs files from URLs",
            notes = "Downloads cvs files from all URLs passed as request parameter.\n"
                    + "The files are analysed and the result is returned.\n"
                    + "These statistics are generated: \n"
                    + "- The politician who gave the most speeches during the year 2013\n"
                    + "- The politician who gave the most speeches about the topic domestic security\n"
                    + "- The politician who said the least amount of words",
            response = EvaluationDto.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful evaluation of csv files", response = EvaluationDto.class),
            @ApiResponse(code = 400, message = "Invalid user request", response = ErrorListDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorListDto.class)}
    )
    ResponseDto evaluate(List<String> urls, HttpServletResponse response);

}
