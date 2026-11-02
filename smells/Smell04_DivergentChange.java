import java.util.*;

/**
 * SMELL d: DIVERGENT CHANGE
 *
 * One class, many unrelated reasons to change. Different requests all land in ProductReport:
 *   - "the data now arrives as JSON"          -> parse()
 *   - "tech products get 15% off"             -> finalPrice()
 *   - "we need a Markdown report"             -> another toXxx()
 *
 * TASK
 *  1. For each of those three requests, list which methods you'd edit. Notice they're always the same class.
 *  2. Split along the axes of change (Extract Class): input parsing, pricing rules, rendering.
 *  3. Adding a Markdown renderer should not touch parsing or pricing.
 *
 * DONE WHEN: demo() output is unchanged AND the file declares >= 4 types.
 *
 * Think: how is this different from Large Class (c)? (Answer in a comment.)
 */
public class Smell04_DivergentChange {

    static class ProductReport {
        // reason to change #1: input format
        private List<String[]> parse(String raw) {
            List<String[]> rows = new ArrayList<>();
            for (String line : raw.split(";")) rows.add(line.split(","));
            return rows;
        }

        // reason to change #2: pricing rules
        private double finalPrice(String[] row) {
            double price = Double.parseDouble(row[1]) * 1.2;
            if (row[2].equals("tech")) price *= 0.9;
            return price;
        }

        // reason to change #3: output formats
        String toText(String raw) {
            StringBuilder sb = new StringBuilder();
            for (String[] row : parse(raw)) {
                if (sb.length() > 0) sb.append("|");
                sb.append(row[0]).append(": ").append(String.format(Locale.ROOT, "%.2f", finalPrice(row)));
            }
            return sb.toString();
        }

        String toHtml(String raw) {
            StringBuilder sb = new StringBuilder("<ul>");
            for (String[] row : parse(raw)) {
                sb.append("<li>").append(row[0]).append(": ")
                  .append(String.format(Locale.ROOT, "%.2f", finalPrice(row))).append("</li>");
            }
            return sb.append("</ul>").toString();
        }
    }

    static String demo() {
        ProductReport r = new ProductReport();
        String raw = "pen,2.00,office;laptop,900.00,tech";
        return r.toText(raw) + "~" + r.toHtml(raw);
    }
}
