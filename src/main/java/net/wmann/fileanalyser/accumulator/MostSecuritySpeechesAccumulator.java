package net.wmann.fileanalyser.accumulator;

import lombok.ToString;
import net.wmann.fileanalyser.model.SpeakerCountPair;

import java.util.*;

@ToString
public class MostSecuritySpeechesAccumulator extends AbstractSpeechAccumulator {

    private static final String DOMESTIC_SECURITY_KEY = "Innere Sicherheit";
    private static final int POSITION_TOPIC = 1;

    private final Map<String, SpeakerCountPair> speechesAboutSecurity = new HashMap<>();

    @Override
    public void process(String line) {
        String[] speechDetails = getSpeechDetails(line);
        if(speechDetails[POSITION_TOPIC].equalsIgnoreCase(DOMESTIC_SECURITY_KEY)) {
            increaseValueCountForKeyBy(speechesAboutSecurity, speechDetails[POSITION_SPEAKER], 1);
        }
    }

    @Override
    public String getResult() {
        return getFirstPairAfterSort(new ArrayList<>(speechesAboutSecurity.values()), Comparator.comparingInt(SpeakerCountPair::getCount).reversed()).getSpeaker();
    }

    @Override
    Map<String, SpeakerCountPair> getMap() {
        return speechesAboutSecurity;
    }

    public static class Builder implements net.wmann.fileanalyser.accumulator.Builder {

        @Override
        public Accumulator build() {
            return new MostSecuritySpeechesAccumulator();
        }

    }

}
