import java.util.*;

/**
 * EXERCISE S: Single Responsibility Principle
 *
 * "A class should have one, and only one, reason to change."
 *
 * Invoice below has FOUR reasons to change (pricing, presentation, persistence, notification).
 *
 * TASK
 *  1. Above each method, write which actor/reason would force it to change.
 *  2. Split Invoice into cohesive classes (e.g. Invoice, InvoicePrinter, InvoiceRepository, InvoiceMailer).
 *  3. Rewrite demo() to use your new classes. It must still return EXACTLY the expected string.
 *
 * DONE WHEN: behaviour check stays green AND the file declares >= 4 types.
 */
public class S1_SRP {

    static class Invoice {
        private final String customer;
        private final List<String> names = new ArrayList<>();
        private final List<Double> prices = new ArrayList<>();
        final List<String> outbox = new ArrayList<>();       // pretend SMTP server
        final Map<String, String> db = new HashMap<>();      // pretend database

        Invoice(String customer) { this.customer = customer; }

        void addItem(String name, double price) { names.add(name); prices.add(price); }

        double total() {
            double sum = 0;
            for (double p : prices) sum += p;
            return Math.round(sum * 1.2 * 100) / 100.0;       // 20% VAT
        }

        String toText() {
            StringBuilder sb = new StringBuilder("Invoice for " + customer + "\n");
            for (int i = 0; i < names.size(); i++) {
                sb.append(" - ").append(names.get(i)).append(": ").append(prices.get(i)).append("\n");
            }
            sb.append("Total (incl. VAT): ").append(total());
            return sb.toString();
        }

        void save() { db.put(customer, toText()); }

        void email(String address) {
            outbox.add("to=" + address + " body=" + toText().length() + " chars");
        }
    }

    static String demo() {
        Invoice inv = new Invoice("ACME");
        inv.addItem("Widget", 10.0);
        inv.addItem("Gadget", 25.5);
        inv.save();
        inv.email("billing@acme.com");
        return inv.toText().replace("\n", "|") + ";saved=" + inv.db.size() + ";outbox=" + inv.outbox;
    }
}
