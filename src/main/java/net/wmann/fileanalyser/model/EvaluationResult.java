package net.wmann.fileanalyser.model;

import net.wmann.fileanalyser.accumulator.Accumulator;

import java.util.ArrayList;
import java.util.List;

public record EvaluationResult(List<Accumulator> accumulators, List<Error> errors) {

    public EvaluationResult(List<Accumulator> accumulators) {
        this(new ArrayList<>(accumulators), new ArrayList<>());
    }

    public void addErrors(List<Error> errors) {
        this.errors.addAll(errors);
    }

    public void addError(Error error) {
        this.errors.add(error);
    }

    public boolean hasAccumulator() {
        return null != accumulators && !accumulators.isEmpty();
    }
}
