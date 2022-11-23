package net.wmann.fileanalyser.service.impl;

import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.accumulator.LeastWordsAccumulator;
import net.wmann.fileanalyser.model.EvaluationResult;
import net.wmann.fileanalyser.task.UrlEvaluationTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlEvaluationServiceTest {

    @Spy
    UrlEvaluationServiceImpl evaluationService = new UrlEvaluationServiceImpl(Executors.newFixedThreadPool(2));

    @Mock
    UrlEvaluationTask taskMock;

    @BeforeEach
    public void setup() {
        doReturn(taskMock).when(evaluationService).getUrlEvaluationTask(any(), any());
    }

    @Test
    public void evaluateUris() throws Exception {
        EvaluationResult expectedResult = createEvaluationResult();
        when(taskMock.get()).thenReturn(createEvaluationResult());

        EvaluationResult testResult = evaluationService.evaluateFiles(Collections.singletonList(new URI("google.com")),
                                                                      Collections.singletonList(new LeastWordsAccumulator.Builder()));

        assertEquals(expectedResult.toString(), testResult.toString(), "Evaluation result not correct");
    }

    private EvaluationResult createEvaluationResult() {
        Accumulator accumulator = new LeastWordsAccumulator();
        accumulator.process("Alexander Abel, Bildungspolitik, 2012-10-30, 5310");
        return new EvaluationResult(Collections.singletonList(accumulator));
    }

}
