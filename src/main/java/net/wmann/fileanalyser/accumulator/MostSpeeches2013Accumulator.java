package net.wmann.fileanalyser.accumulator;

import lombok.ToString;
import net.wmann.fileanalyser.model.SpeakerCountPair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@ToString
public class MostSpeeches2013Accumulator extends AbstractSpeechAccumulator {

    private static final int POSITION_DATE = 2;

    private final Map<String, SpeakerCountPair> speechesIn2013 = new HashMap<>();

    @Override
    public void process(String line) {
        String[] speechDetails = getSpeechDetails(line);
        if (speechDetails[POSITION_DATE].contains("2013")) {
            increaseValueCountForKeyBy(speechesIn2013, speechDetails[POSITION_SPEAKER], 1);
        }
    }

    @Override
    public String getResult() {
        return getFirstPairAfterSort(new ArrayList<>(speechesIn2013.values()), Comparator.comparingInt(SpeakerCountPair::getCount).reversed()).getSpeaker();
    }

    @Override
    Map<String, SpeakerCountPair> getMap() {
        return speechesIn2013;
    }

    public static class Builder implements net.wmann.fileanalyser.accumulator.Builder {

        @Override
        public Accumulator build() {
            return new MostSpeeches2013Accumulator();
        }

    }
}
