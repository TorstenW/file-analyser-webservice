package net.wmann.fileanalyser.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SpeakerCountPair {

    private final String speaker;
    private int count;

    public SpeakerCountPair(String speaker, int count) {
        this.speaker = speaker;
        this.count = count;
    }

    public void increaseCount(int delta) {
        count += delta;
    }

    public int getCount() {
        return this.count;
    }

    public String getSpeaker() {
        return speaker;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("speaker", speaker)
                .append("count", count)
                .toString();
    }
}
