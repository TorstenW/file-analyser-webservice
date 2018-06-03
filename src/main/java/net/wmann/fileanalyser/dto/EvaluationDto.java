package net.wmann.fileanalyser.dto;

import lombok.Data;

@Data
public class EvaluationDto implements ResponseDto {

    private String mostSpeeches;

    private String mostSecurity;

    private String leastWordy;

}
