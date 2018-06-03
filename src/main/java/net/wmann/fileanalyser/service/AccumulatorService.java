package net.wmann.fileanalyser.service;

import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.dto.ResponseDto;
import net.wmann.fileanalyser.model.Error;

import java.util.List;

/**
 * This services is used to create a Response from the results of an evaluation
 */
public interface AccumulatorService {

    /**
     * This creates the {@code ResponseDto} needed for a speech file analysis
     *
     * @param accumulators The accumulators which contain the final analysis result
     * @param errors A list of any error that might have occurred during the evaluation
     * @return {@code ResponseDto} which contains the final result of an evaluation
     */
    ResponseDto createSpeechAccumulatorResult(List<Accumulator> accumulators, List<Error> errors);

}
