package net.wmann.fileanalyser.task;

import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.EvaluationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class UrlEvaluationTask implements Supplier<EvaluationResult> {

    private static final Logger log = LogManager.getLogger();

    private final URI uri;
    private final List<Accumulator> accumulators;

    public UrlEvaluationTask(URI uri, List<Accumulator> accumulators) {
        this.uri = uri;
        this.accumulators = accumulators;
    }

    @Override
    public EvaluationResult get() {
        log.info("Starting evaluation of url: %s".formatted(uri));
        List<Error> errors = new ArrayList<>();
        try (BufferedReader reader = loadCsvFile(uri.toURL())) {
            reader.lines()
                  .skip(1)
                  .forEach(line -> accumulators
                          .forEach(acc -> acc.process(line)));
        } catch (Exception e) {
            log.debug("Error while trying to process file from %s".formatted(uri), e);
            errors.add(new Error("File processing error for URL: %s".formatted(uri), e.getClass().getSimpleName(), e.getMessage()));
        }
        log.debug("Finished url evaluation of url: %s".formatted(uri));
        return new EvaluationResult(accumulators, errors);
    }

    BufferedReader loadCsvFile(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
