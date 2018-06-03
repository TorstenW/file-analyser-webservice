package net.wmann.fileanalyser.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.wmann.fileanalyser.accumulator.Accumulator;
import net.wmann.fileanalyser.accumulator.LeastWordsAccumulator;
import net.wmann.fileanalyser.accumulator.MostSecuritySpeechesAccumulator;
import net.wmann.fileanalyser.accumulator.MostSpeeches2013Accumulator;
import net.wmann.fileanalyser.dto.ErrorListDto;
import net.wmann.fileanalyser.dto.EvaluationDto;
import net.wmann.fileanalyser.dto.ResponseDto;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.service.AccumulatorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccumulatorServiceImpl implements AccumulatorService {

    @Override
    public ResponseDto createSpeechAccumulatorResult(List<Accumulator> accumulators, List<Error> errors) {
        EvaluationDto evaluationDto = new EvaluationDto();
        for(Accumulator accumulator : accumulators) {
            if(!accumulator.getErrors().isEmpty()) {
                errors.addAll(accumulator.getErrors());
                continue;
            }
            if(errors.isEmpty()) {
                if (accumulator.getClass() == MostSpeeches2013Accumulator.class) {
                    evaluationDto.setMostSpeeches(((MostSpeeches2013Accumulator)accumulator).getResult());
                } else if (accumulator.getClass() == MostSecuritySpeechesAccumulator.class) {
                    evaluationDto.setMostSecurity(((MostSecuritySpeechesAccumulator)accumulator).getResult());
                } else if (accumulator.getClass() == LeastWordsAccumulator.class) {
                    evaluationDto.setLeastWordy(((LeastWordsAccumulator)accumulator).getResult());
                }
            }
        }
        if(!errors.isEmpty()) {
            return new ErrorListDto(errors);
        }
        return evaluationDto;
    }

}
