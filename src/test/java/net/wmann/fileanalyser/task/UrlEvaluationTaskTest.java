package net.wmann.fileanalyser.task;

import net.wmann.fileanalyser.accumulator.LeastWordsAccumulator;
import net.wmann.fileanalyser.model.EvaluationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class UrlEvaluationTaskTest {

    UrlEvaluationTask evaluationTask;

    @Before
    public void setup() throws Exception {
        evaluationTask = spy(new UrlEvaluationTask(new URI("http://google.com"), Arrays.asList(new LeastWordsAccumulator())));
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/static/valid_example_speeches.csv")));
        doReturn(reader).when(evaluationTask).loadCsvFile(any());
    }

    @Test
    public void getEvaluationResult() {
        String expectedResultString = "EvaluationResult(accumulators=[LeastWordsAccumulator(wordCount={Alexander Abel=SpeakerCountPair(speaker=Alexander Abel, count=6221), "
                + "Bernhard Belling=SpeakerCountPair(speaker=Bernhard Belling, count=1210), "
                + "Caesare Collins=SpeakerCountPair(speaker=Caesare Collins, count=1119)})], errors=[])";

        EvaluationResult taskResult = evaluationTask.get();

        assertEquals(expectedResultString, taskResult.toString());
    }

    @Test
    public void getWithInvalidUrl() throws Exception {
        String expectedResultString = "Error(error=File processing error for URL: google.com, exception=IllegalArgumentException, message=URI is not absolute)";

        EvaluationResult taskResult = new UrlEvaluationTask(new URI("google.com"), Arrays.asList(new LeastWordsAccumulator())).get();

        assertEquals(expectedResultString, taskResult.getErrors().get(0).toString());
    }

}
