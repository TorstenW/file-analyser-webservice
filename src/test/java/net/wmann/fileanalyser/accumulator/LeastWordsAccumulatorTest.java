package net.wmann.fileanalyser.accumulator;

import net.wmann.fileanalyser.exception.InvalidLineException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class LeastWordsAccumulatorTest {

    private LeastWordsAccumulator accumulator = new LeastWordsAccumulator();

    @Test
    public void processLine() {
        accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 5310");
        accumulator.process("Bernhard Belling, Kohlesubventionen, 2012-11-05, 1210");

        assertEquals("Bernhard Belling", accumulator.getResult());
    }

    @Test
    public void processLineTooFewElements() {
        try {
            accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30");
            assertTrue("Test should not reach this point", false);
        } catch (InvalidLineException e) {
            assertEquals("Line: 'Alexander Abel, Bildungspolitik, 2012-10-30'", e.getMessage());
        } catch (Exception e) {
            assertTrue("Test should not reach this point", false);
        }
    }

    @Test
    public void processLineTooManyElements() {
        try {
            accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 8973, asbf");
            assertTrue("Test should not reach this point", false);
        } catch (InvalidLineException e) {
            assertEquals("Line: 'Alexander Abel, Bildungspolitik, 2012-10-30, 8973, asbf'", e.getMessage());
        } catch (Exception e) {
            assertTrue("Test should not reach this point", false);
        }
    }

    @Test
    public void processLineInvalidNumber() {
        try {
            accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, asbf");
            assertTrue("Test should not reach this point", false);
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"asbf\"", e.getMessage());
        } catch (Exception e) {
            assertTrue("Test should not reach this point", false);
        }
    }

}
