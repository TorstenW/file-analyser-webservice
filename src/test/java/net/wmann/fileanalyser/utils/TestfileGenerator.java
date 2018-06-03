package net.wmann.fileanalyser.utils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestfileGenerator {

    private static final List<String> NAMES = Arrays.asList("Karolyn Schlegel",
            "Eura Bonavita",
            "Juli Zeno",
            "Tova Strother",
            "Han Mcpeak",
            "Neva Crosswhite",
            "Kurt Durden",
            "Kara Bernat",
            "Carli Every",
            "Lashay Carper",
            "Carlotta Shahan",
            "Bertram Cardinal",
            "Horace Raven",
            "Leonor Myhre",
            "Venetta Diebold",
            "Candance Becraft",
            "Edison Soucie",
            "Warren Brunt",
            "Delila Sherburne",
            "Meda Geno");

    private static final List<String> TOPCIS = Arrays.asList("Bildungspolitik",
            "Kohlesubventionen",
            "Innere Sicherheit",
            "Verkehrspolitik",
            "Bundeshaushalt",
            "Außenpolitik",
            "Familienpolitik");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final long DATE_IN_2011_MILIS = 1300000000000L;
    private static final long DELTA_ABOUT_6_YEARS_MILIS = 200000000000L;

    private static final String TARGET_PATH = "";
    private static final int LINES_TO_CREATE_COUNT = 2000000; // 2000000 is about 100 MB filesize

    public static void main(String... args) throws Exception {
        Path path = Paths.get(TARGET_PATH);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("Redner, Thema, Datum, Wörter\n");

            for(int i = 0; i < LINES_TO_CREATE_COUNT; i++) {
                StringBuilder b = new StringBuilder();
                b.append(NAMES.get(ThreadLocalRandom.current().nextInt(0, NAMES.size()))).append(", ");
                b.append(TOPCIS.get(ThreadLocalRandom.current().nextInt(0, TOPCIS.size()))).append(", ");
                b.append(DATE_FORMAT.format(createRandomDate())).append(", ");
                b.append(ThreadLocalRandom.current().nextInt(1, 10001)).append("\n");
                writer.write(b.toString());
            }
        }
    }

    private static Date createRandomDate() {
        Random rnd = new Random();
        return new Date(DATE_IN_2011_MILIS + (Math.abs(rnd.nextLong()) % (DELTA_ABOUT_6_YEARS_MILIS)));
    }

}
