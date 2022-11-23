package net.wmann.fileanalyser.accumulator;

import net.wmann.fileanalyser.exception.InvalidLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LeastWordsAccumulatorTest {

    private final LeastWordsAccumulator accumulator = new LeastWordsAccumulator();

    @Test
    public void processLine() {
        accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 5310");
        accumulator.process("Bernhard Belling, Kohlesubventionen, 2012-11-05, 1210");

        assertEquals("Bernhard Belling", accumulator.getResult());
    }

    @Test
    public void processLineTooFewElements() {
        var exception = assertThrows(InvalidLineException.class, () -> accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30"));
        assertEquals("Line: 'Alexander Abel, Bildungspolitik, 2012-10-30'", exception.getMessage());
    }

    @Test
    public void processLineTooManyElements() {
        var exception = assertThrows(InvalidLineException.class, () -> accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 8973, asbf"));
        assertEquals("Line: 'Alexander Abel, Bildungspolitik, 2012-10-30, 8973, asbf'", exception.getMessage());
    }

    @Test
    public void processLineInvalidNumber() {
        var exception = assertThrows(NumberFormatException.class, () -> accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, asbf"));
        assertEquals("For input string: \"asbf\"", exception.getMessage());
    }

}
