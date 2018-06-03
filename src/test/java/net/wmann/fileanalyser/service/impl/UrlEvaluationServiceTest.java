package net.wmann.fileanalyser.service.impl;

import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.accumulator.LeastWordsAccumulator;
import net.wmann.fileanalyser.model.EvaluationResult;
import net.wmann.fileanalyser.task.UrlEvaluationTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlEvaluationServiceTest {

    @Spy
    UrlEvaluationServiceImpl evaluationService = new UrlEvaluationServiceImpl(Executors.newFixedThreadPool(2));

    @Mock
    UrlEvaluationTask taskMock;

    @Before
    public void setup() {
        doReturn(taskMock).when(evaluationService).getUrlEvaluationTask(any(), any());
    }

    @Test
    public void evaluateUris() throws Exception {
        EvaluationResult expectedResult = createEvaluationResult();
        when(taskMock.get()).thenReturn(createEvaluationResult());

        EvaluationResult testResult = evaluationService.evaluateFiles(Collections.singletonList(new URI("google.com")),
                                                                      Collections.singletonList(new LeastWordsAccumulator.Builder()));

        assertEquals("Evaluation result not correct", expectedResult.toString(), testResult.toString());
    }

    private EvaluationResult createEvaluationResult() {
        Accumulator accumulator = new LeastWordsAccumulator();
        accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 5310");
        return new EvaluationResult(Collections.singletonList(accumulator));
    }

}
