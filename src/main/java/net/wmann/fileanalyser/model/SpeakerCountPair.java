package net.wmann.fileanalyser.model;

import lombok.ToString;

@ToString
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
}
