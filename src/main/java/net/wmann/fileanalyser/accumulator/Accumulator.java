package net.wmann.fileanalyser.accumulator;

import net.wmann.fileanalyser.model.Error;

import java.util.List;

/**
 * An accumulator is used to evaluate a single line and store the current status of the evaluation
 *
 * @param <R> The type of the result this Accumulator returns
 */
public interface Accumulator <R> {

    /**
     * Evaluates if a line matches the criteria and updates its state if necessary
     *
     * @param line The line which should be evaluated
     */
    void process(String line);

    /**
     * Adds the data of the other accumulater to its own data.<br/><br/>
     * <b>Both accumulators need to be of the same class</b>
     *
     * @param accumulator The other accumulator
     */
    void combine(Accumulator accumulator);

    /**
     *
     * @return The result according to the current state of this instance
     */
    R getResult();

    /**
     *
     * @return All errors encountered so far during evaluation
     */
    List<Error> getErrors();

}
