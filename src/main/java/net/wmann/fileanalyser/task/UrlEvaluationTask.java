package net.wmann.fileanalyser.task;

import lombok.extern.slf4j.Slf4j;
import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.EvaluationResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class UrlEvaluationTask implements Supplier<EvaluationResult> {

    private final URI uri;
    private final List<Accumulator> accumulators;

    public UrlEvaluationTask(URI uri, List<Accumulator> accumulators) {
        this.uri = uri;
        this.accumulators = accumulators;
    }

    @Override
    public EvaluationResult get() {
        log.info(String.format("Starting evaluation of url: %s", uri.toString()));
        List<Error> errors = new ArrayList<>();
        try (BufferedReader reader = loadCsvFile(uri.toURL())) {
            reader.lines()
                  .skip(1)
                  .forEach(line -> accumulators
                          .forEach(acc -> acc.process(line)));
        } catch (Exception e) {
            log.debug(String.format("Error while trying to process file from %s", uri.toString()), e);
            errors.add(new Error(String.format("File processing error for URL: %s", uri.toString()), e.getClass().getSimpleName(), e.getMessage()));
        }
        log.debug(String.format("Finished url evaluation of url: %s", uri.toString()));
        return new EvaluationResult(accumulators, errors);
    }

    BufferedReader loadCsvFile(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
