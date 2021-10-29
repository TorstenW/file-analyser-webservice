package net.wmann.fileanalyser.accumulator;

import net.wmann.fileanalyser.exception.InvalidLineException;
import net.wmann.fileanalyser.model.Error;
import net.wmann.fileanalyser.model.SpeakerCountPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public sealed abstract class AbstractSpeechAccumulator implements Accumulator
        permits LeastWordsAccumulator, MostSecuritySpeechesAccumulator, MostSpeeches2013Accumulator {

    private static final Logger log = LogManager.getLogger();

    private final SpeakerCountPair INVALID_SPEAKER_COUNT_PAIR = new SpeakerCountPair(null, -1);
    private final List<Error> foundErrors = new ArrayList<>();

    static final int POSITION_SPEAKER = 0;
    private static final String LINE_SEPARATOR = ", ";
    private static final int EXPECTED_ELEMENTS_PER_LINE = 4;

    @Override
    public List<Error> getErrors() {
        return foundErrors;
    }

    @Override
    public void combine(Accumulator accumulator) {
        if (this.getClass() != accumulator.getClass()) {
            throw new IllegalArgumentException("Combine in class '%s' called with class '%s'".formatted(
                    this.getClass().getSimpleName(), accumulator.getClass().getSimpleName()));
        }

        Collection<SpeakerCountPair> otherPairs = ((AbstractSpeechAccumulator) accumulator).getMap().values();
        otherPairs.forEach(pair -> increaseValueCountForKeyBy(this.getMap(), pair.getSpeaker(), pair.getCount()));
    }

    String[] getSpeechDetails(String line) {
        String[] speechDetails = line.split(LINE_SEPARATOR);
        if (speechDetails.length != EXPECTED_ELEMENTS_PER_LINE) {
            throw new InvalidLineException("Line: '%s'".formatted(line));
        }
        return speechDetails;
    }

    void increaseValueCountForKeyBy(Map<String, SpeakerCountPair> map, String key, int delta) {
        SpeakerCountPair pair;
        if (map.containsKey(key)) {
            log.trace("%s: Increase key '%s' by delta '%d'".formatted(this.getClass().getSimpleName(), key, delta));
            pair = map.get(key);
            pair.increaseCount(delta);
            log.trace("%s: Key '%s' has value '%d' now".formatted(this.getClass().getSimpleName(), key, delta));
        } else {
            log.trace("%s: Create key '%s' with value '%d'".formatted(this.getClass().getSimpleName(), key, delta));
            pair = new SpeakerCountPair(key, delta);
        }
        map.put(key, pair);
    }

    SpeakerCountPair getFirstPairAfterSort(List<SpeakerCountPair> pairs, Comparator<SpeakerCountPair> comparator) {
        if (pairs.isEmpty()) {
            log.debug("%s: No solution found".formatted(this.getClass().getSimpleName()));
            return INVALID_SPEAKER_COUNT_PAIR;
        }
        pairs.sort(comparator);
        // return null for speaker in case result is not distinct
        if (pairs.size() > 1 && pairs.get(0).getCount() == pairs.get(1).getCount()) {
            log.debug("%s: No distinct solution found.".formatted(this.getClass().getSimpleName()));
            return INVALID_SPEAKER_COUNT_PAIR;
        }
        return pairs.get(0);
    }

    abstract Map<String, SpeakerCountPair> getMap();

}
