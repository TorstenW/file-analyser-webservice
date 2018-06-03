package net.wmann.fileanalyser.model;

import lombok.Data;
import net.wmann.fileanalyser.accumulator.Accumulator;

import java.util.ArrayList;
import java.util.List;

@Data
public class EvaluationResult {

    private final List<Accumulator> accumulators;

    private final List<Error> errors;

    public EvaluationResult(List<Accumulator> accumulators) {
        this.accumulators = accumulators;
        this.errors = new ArrayList<>();
    }

    public EvaluationResult(List<Accumulator> accumulators, List<Error> errors) {
        this.accumulators = accumulators;
        this.errors = errors;
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
