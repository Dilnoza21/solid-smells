import java.util.Locale;

/**
 * SMELL b: LONG METHOD
 *
 * TASK
 *  1. Mark the distinct "paragraphs" inside summarize() with comments (validate, sum/min/max, spread, ...).
 *  2. Replace each paragraph with a well-named method (Extract Method). Replace comments with names.
 *  3. summarize() itself should read like a table of contents.
 *
 * DONE WHEN: demo() output is unchanged AND no method is longer than 15 non-blank lines.
 */
public class Smell02_LongMethod {

    static String summarize(int[] scores) {
        if (scores == null || scores.length == 0) {
            return "no data";
        }
        int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int s : scores) {
            sum += s;
            if (s < min) min = s;
            if (s > max) max = s;
        }
        double mean = (double) sum / scores.length;
        double variance = 0;
        for (int s : scores) {
            variance += (s - mean) * (s - mean);
        }
        double stdDev = Math.sqrt(variance / scores.length);
        int passed = 0;
        for (int s : scores) {
            if (s >= 50) passed++;
        }
        double passRate = 100.0 * passed / scores.length;
        String grade;
        if (mean >= 90) grade = "A";
        else if (mean >= 75) grade = "B";
        else if (mean >= 50) grade = "C";
        else grade = "F";
        return String.format(Locale.ROOT, "n=%d sum=%d min=%d max=%d mean=%.2f sd=%.2f pass=%.1f%% grade=%s",
                scores.length, sum, min, max, mean, stdDev, passRate, grade);
    }

    static String demo() {
        return summarize(new int[]{40, 60, 80, 100});
    }
}
