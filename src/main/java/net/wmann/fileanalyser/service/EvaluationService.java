package net.wmann.fileanalyser.service;

import net.wmann.fileanalyser.accumulator.Builder;
import net.wmann.fileanalyser.model.EvaluationResult;

import java.net.URI;
import java.util.List;

/**
 * This service can be used to analyse the content of files.
 */
public interface EvaluationService {

    /**
     * Opens the file from every URI and executes each accumulator on every line of each file.<br/>
     * It creates a new set of accumulators for every thread it uses.
     *
     * @param uris A list of URIs for each file that should be analysed
     * @param accumulatorBuilders A list of Builders to create accumulators for the evaluation
     *
     * @return Returns a {@code EvaluationResult} which contains all final accumulators and every {@code Error}
     */
    EvaluationResult evaluateFiles(List<URI> uris, List<Builder> accumulatorBuilders);

}
