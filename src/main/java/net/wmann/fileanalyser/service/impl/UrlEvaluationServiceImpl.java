package net.wmann.fileanalyser.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.accumulator.Builder;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.EvaluationResult;
import net.wmann.fileanalyser.service.EvaluationService;
import net.wmann.fileanalyser.task.UrlEvaluationTask;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service(value="urlEvaluationService")
@Slf4j
public class UrlEvaluationServiceImpl implements EvaluationService {

    private ExecutorService pool;

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
                        .collect(Collectors.toList());

        // Map the List of Future to a Future of List
        CompletableFuture<List<EvaluationResult>> allEvaluationResults =
                CompletableFuture.allOf(evaluationFutures.toArray(new CompletableFuture[evaluationFutures.size()]))
                                 .thenApply(v -> evaluationFutures.stream()
                                                    .map(CompletableFuture::join)
                                                    .collect(Collectors.toList()));

        // Combine all evaluationResults by combining their Errors and Accumulators
        CompletableFuture<EvaluationResult> evaluationResultFuture = allEvaluationResults.thenApply(partResults -> {
            log.info("Finished all url evaluations. Creating result...");
            final EvaluationResult evalResult = new EvaluationResult(buildAccumulators(accumulatorBuilders));
            partResults.forEach(partResult -> {
                evalResult.addErrors(partResult.getErrors());
                evalResult.getAccumulators().forEach(resultAcc -> {
                    for(Accumulator partAcc : partResult.getAccumulators()) {
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
            log.debug(evaluationResult.getAccumulators().toString());
        } catch (Exception e) {
            log.error("Error while getting evaluation future", e);
            evaluationResult = new EvaluationResult(Collections.EMPTY_LIST);
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
