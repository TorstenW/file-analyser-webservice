package net.wmann.fileanalyser.controller;

import net.wmann.fileanalyser.accumulator.Builder;
import net.wmann.fileanalyser.accumulator.LeastWordsAccumulator;
import net.wmann.fileanalyser.accumulator.MostSecuritySpeechesAccumulator;
import net.wmann.fileanalyser.accumulator.MostSpeeches2013Accumulator;
import net.wmann.fileanalyser.converter.UriConverter;
import net.wmann.fileanalyser.dto.ErrorListDto;
import net.wmann.fileanalyser.dto.EvaluationDto;
import net.wmann.fileanalyser.dto.ResponseDto;
import net.wmann.fileanalyser.exception.InvalidUrlException;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.EvaluationResult;
import net.wmann.fileanalyser.service.AccumulatorService;
import net.wmann.fileanalyser.service.EvaluationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SpeechController {

    private static final Logger log = LogManager.getLogger();

    private final EvaluationService evaluationService;
    private final AccumulatorService accumulatorService;

    public SpeechController(EvaluationService evaluationService, AccumulatorService accumulatorService) {
        this.evaluationService = evaluationService;
        this.accumulatorService = accumulatorService;
    }

    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public ResponseDto evaluate(@RequestParam(name = "url") List<String> urls, HttpServletResponse response) {
        log.info("Received GET request to /evaluation with urls={}", urls);
        List<URI> uris = UriConverter.convert(urls);
        ResponseDto result = new EvaluationDto();

        if(!uris.isEmpty()) {
            List<Builder> accumulatorBuilders = Stream.of(new MostSpeeches2013Accumulator.Builder(),
                    new MostSecuritySpeechesAccumulator.Builder(),
                    new LeastWordsAccumulator.Builder())
                    .collect(Collectors.toList());

            EvaluationResult evaluationResult = evaluationService.evaluateFiles(uris, accumulatorBuilders);

            if(evaluationResult.hasAccumulator()) {
                result = accumulatorService.createSpeechAccumulatorResult(evaluationResult.accumulators(), evaluationResult.errors());
            } else {
                result = new ErrorListDto(evaluationResult.errors());
            }
        }

        if(result.getClass() == ErrorListDto.class) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        log.debug("Returning: {}", result);
        return result;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUrlException.class)
    public ErrorListDto invalidUrlException(InvalidUrlException e) {
        Error error = new Error("An error occurred while parsing the requested urls", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorListDto(error);
    }

}
