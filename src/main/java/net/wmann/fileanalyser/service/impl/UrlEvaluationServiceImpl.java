package net.wmann.fileanalyser.service.impl;

import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.accumulator.Builder;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.EvaluationResult;
import net.wmann.fileanalyser.service.EvaluationService;
import net.wmann.fileanalyser.task.UrlEvaluationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class UrlEvaluationServiceImpl implements EvaluationService {

    private static final Logger log = LogManager.getLogger();

    private final ExecutorService pool;

    public UrlEvaluationServiceImpl(ExecutorService pool) {
        this.pool = pool;
    }

    @Override
    public EvaluationResult evaluateFiles(final List<URI> uris, final List<Builder> accumulatorBuilders) {
        final long startTime = System.nanoTime();

        // create a Future for each URI
        List<CompletableFuture<EvaluationResult>> evaluationFutures =
                uris.stream()
                        .map(uri -> CompletableFuture.supplyAsync(getUrlEvaluationTask(uri, accumulatorBuilders), pool))
                        .toList();

        // Map the List of Future to a Future of List
        CompletableFuture<List<EvaluationResult>> allEvaluationResults =
                CompletableFuture.allOf(evaluationFutures.toArray(new CompletableFuture[0]))
                                 .thenApply(v -> evaluationFutures.stream()
                                                    .map(CompletableFuture::join)
                                                    .collect(Collectors.toList()));

        // Combine all evaluationResults by combining their Errors and Accumulators
        CompletableFuture<EvaluationResult> evaluationResultFuture = allEvaluationResults.thenApply(partResults -> {
            log.info("Finished all url evaluations. Creating result...");
            final EvaluationResult evalResult = new EvaluationResult(buildAccumulators(accumulatorBuilders));
            partResults.forEach(partResult -> {
                evalResult.addErrors(partResult.errors());
                evalResult.accumulators().forEach(resultAcc -> {
                    for(Accumulator partAcc : partResult.accumulators()) {
                        if(partAcc.getClass() == resultAcc.getClass()) {
                            resultAcc.combine(partAcc);
                            break;
                        }
                    }
                });
            });
            return evalResult;
        });

        EvaluationResult evaluationResult;
        try {
            evaluationResult = evaluationResultFuture.get();
            log.debug(evaluationResult.accumulators().toString());
        } catch (Exception e) {
            log.error("Error while getting evaluation future", e);
            evaluationResult = new EvaluationResult(Collections.emptyList());
            evaluationResult.addError(new Error("Internal Server Error", e.getClass().getSimpleName(), e.getMessage()));
        }

        final long endTime = System.nanoTime();
        log.info("Completed evaluation in " + new DecimalFormat("###.###").format((endTime - startTime)/Math.pow(10,6)) + " milliseconds");
        return evaluationResult;
    }

    UrlEvaluationTask getUrlEvaluationTask(final URI uri, final List<Builder> accumulatorBuilders) {
        return new UrlEvaluationTask(uri, buildAccumulators(accumulatorBuilders));
    }

    private List<Accumulator> buildAccumulators(List<Builder> accumulatorBuilders) {
        return accumulatorBuilders.stream()
                                  .map(Builder::build)
                                  .collect(Collectors.toList());
    }

}
