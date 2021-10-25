package net.wmann.fileanalyser.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EvaluationDto implements ResponseDto {

    private String mostSpeeches;

    private String mostSecurity;

    private String leastWordy;

    public String getMostSpeeches() {
        return mostSpeeches;
    }

    public void setMostSpeeches(String mostSpeeches) {
        this.mostSpeeches = mostSpeeches;
    }

    public String getMostSecurity() {
        return mostSecurity;
    }

    public void setMostSecurity(String mostSecurity) {
        this.mostSecurity = mostSecurity;
    }

    public String getLeastWordy() {
        return leastWordy;
    }

    public void setLeastWordy(String leastWordy) {
        this.leastWordy = leastWordy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("mostSpeeches", mostSpeeches)
                .append("mostSecurity", mostSecurity)
                .append("leastWordy", leastWordy)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EvaluationDto that = (EvaluationDto) o;

        return new EqualsBuilder().append(mostSpeeches, that.mostSpeeches).append(mostSecurity, that.mostSecurity).append(leastWordy, that.leastWordy).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mostSpeeches).append(mostSecurity).append(leastWordy).toHashCode();
    }
}
