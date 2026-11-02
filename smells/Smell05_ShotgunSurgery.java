import java.util.Locale;

/**
 * SMELL e: SHOTGUN SURGERY
 *
 * One small change (e.g. "show money as '12,00 EUR' instead of '$12.00'") forces tiny edits in many classes.
 *
 * TASK
 *  1. Count how many places you would have to edit for that change.
 *  2. Pull the knowledge into ONE place (Move Method / Inline Class / a Money formatter) so the same change
 *     becomes a single edit.
 *
 * DONE WHEN: demo() output is unchanged AND the money format string appears at most once in the file.
 *
 * Think: why is this the "opposite" of Divergent Change (d)? (Answer in a comment.)
 */
public class Smell05_ShotgunSurgery {

    static class Cart {
        String line(String item, double price) {
            return item + " " + String.format(Locale.ROOT, "$%.2f", price);
        }
    }

    static class Invoice {
        String total(double amount) {
            return "Total: " + String.format(Locale.ROOT, "$%.2f", amount);
        }
    }

    static class Receipt {
        String paid(double amount) {
            return "Paid " + String.format(Locale.ROOT, "$%.2f", amount);
        }
    }

    static class ShippingEmail {
        String subject(double amount) {
            return "Your order of " + String.format(Locale.ROOT, "$%.2f", amount) + " shipped";
        }
    }

    static String demo() {
        return new Cart().line("pen", 1.5) + "|" + new Invoice().total(12) + "|"
                + new Receipt().paid(12) + "|" + new ShippingEmail().subject(12);
    }
}
