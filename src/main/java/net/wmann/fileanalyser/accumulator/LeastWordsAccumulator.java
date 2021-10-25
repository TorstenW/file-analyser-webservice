package net.wmann.fileanalyser.accumulator;

import net.wmann.fileanalyser.model.SpeakerCountPair;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

public class LeastWordsAccumulator extends AbstractSpeechAccumulator {

    private static final int POSITION_WORD_COUNT = 3;

    private final Map<String, SpeakerCountPair> wordCount = new HashMap<>();

    @Override
    public void process(String line) {
        String[] speechDetails = getSpeechDetails(line);
        increaseValueCountForKeyBy(wordCount, speechDetails[POSITION_SPEAKER], Integer.parseInt(speechDetails[POSITION_WORD_COUNT]));
    }

    @Override
    public String getResult() {
        return getFirstPairAfterSort(new ArrayList<>(wordCount.values()), Comparator.comparingInt(SpeakerCountPair::getCount)).getSpeaker();
    }

    @Override
    Map<String, SpeakerCountPair> getMap() {
        return wordCount;
    }

    public static class Builder implements net.wmann.fileanalyser.accumulator.Builder {

        @Override
        public Accumulator build() {
            return new LeastWordsAccumulator();
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("wordCount", wordCount)
                .toString();
    }
}
