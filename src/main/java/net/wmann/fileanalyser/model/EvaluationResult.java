package net.wmann.fileanalyser.model;

import net.wmann.fileanalyser.accumulator.Accumulator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accumulators", accumulators)
                .append("errors", errors)
                .toString();
    }
}
