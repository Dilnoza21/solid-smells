import java.util.*;

/**
 * EXERCISE O: Open/Closed Principle
 *
 * "Software entities should be open for extension, but closed for modification."
 *
 * Every new customer type forces an edit to DiscountCalculator.apply().
 *
 * TASK
 *  1. Marketing wants a STUDENT discount of 15%. First add it the "wrong" way (edit the switch).
 *     Note the diff: which existing, already-tested code did you have to touch?
 *  2. Refactor (Strategy / polymorphism / registry) so that supporting a NEW customer type
 *     needs NO change to DiscountCalculator. Add STUDENT again the "right" way.
 *  3. Remove the switch.
 *
 * DONE WHEN: demo() returns the expected string (including STUDENT) AND there is no `switch`.
 */
public class S2_OCP {

    static class DiscountCalculator {
        double apply(String customerType, double amount) {
            switch (customerType) {
                case "REGULAR":  return amount;
                case "SILVER":   return amount * 0.90;
                case "GOLD":     return amount * 0.80;
                case "EMPLOYEE": return amount * 0.70;
                default: throw new IllegalArgumentException("Unknown type: " + customerType);
            }
        }
    }

    static String demo() {
        DiscountCalculator calc = new DiscountCalculator();
        List<String> out = new ArrayList<>();
        for (String type : List.of("REGULAR", "SILVER", "GOLD", "EMPLOYEE", "STUDENT")) {
            try {
                out.add(String.valueOf(calc.apply(type, 100.0)));
            } catch (IllegalArgumentException e) {
                out.add("ERR");
            }
        }
        return String.join(",", out);
    }
}
