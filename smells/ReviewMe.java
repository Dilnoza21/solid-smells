import java.util.*;

/**
 * CAPSTONE: CODE REVIEW (no refactoring required).
 *
 * You are the reviewer on a pull request. This code compiles and "works", but the design is poor.
 * Read it, then fill in REVIEW_TEMPLATE.md. Do NOT run a refactoring tool first: practise spotting smells by eye.
 *
 * Expect to find all 12 smells from the course plus violations of several SOLID principles.
 * (Not every smell appears exactly once.)
 */
public class ReviewMe {

    static class Item {
        public String name;
        public double price;
        public int qty;
        public String category;
    }

    static abstract class BaseProcessor {
        abstract double tax(Item i);
        void onStart() { }
        void onEnd() { }
    }

    static class OrderProcessor extends BaseProcessor {
        List<Item> items = new ArrayList<>();
        String customerName;
        String street;
        String city;
        String zip;
        String country;
        String cardNumber;
        String paymentType;      // "CARD", "PAYPAL", "INVOICE"
        int priority;            // 0 = normal, 1 = high, 2 = urgent
        double discountRate;
        List<String> log = new ArrayList<>();
        Map<String, String> db = new HashMap<>();

        double tax(Item i) {
            if (i.category.equals("food")) return i.price * i.qty * 0.07;
            if (i.category.equals("book")) return i.price * i.qty * 0.07;
            return i.price * i.qty * 0.19;
        }

        String process(String customerName, String street, String city, String zip, String country,
                       String paymentType, boolean gift, boolean express, int priority) {
            onStart();
            double total = 0;
            for (Item i : items) {
                total += i.price * i.qty;
                total += tax(i);
            }
            total = total - total * discountRate;
            double fee;
            switch (paymentType) {
                case "CARD":    fee = total * 0.029 + 0.30; break;
                case "PAYPAL":  fee = total * 0.034 + 0.35; break;
                case "INVOICE": fee = 5.0; break;
                default: throw new IllegalArgumentException(paymentType);
            }
            total += fee;
            if (express) total += 12.5;
            if (priority == 2) total += 20;
            if (priority == 1) total += 7;
            String label;
            if (country.equals("US")) {
                label = customerName + "\n" + street + "\n" + city + " " + zip;
            } else {
                label = customerName + "\n" + street + "\n" + city + " " + zip + "\n" + country.toUpperCase();
            }
            if (gift) label = label + "\n*** GIFT ***";
            db.put(customerName, label + "|" + total);
            log.add("processed " + customerName + " total=" + total);
            onEnd();
            return label + "\nTOTAL: " + Math.round(total * 100) / 100.0;
        }

        String invoiceHeader(String customerName, String street, String city, String zip, String country) {
            String label;
            if (country.equals("US")) {
                label = customerName + "\n" + street + "\n" + city + " " + zip;
            } else {
                label = customerName + "\n" + street + "\n" + city + " " + zip + "\n" + country.toUpperCase();
            }
            return "INVOICE\n" + label;
        }

        double shippingWeight(Item i) {
            return i.qty * (i.category.equals("book") ? 0.5 : 1.2);
        }

        double averageItemPrice() {
            double s = 0;
            for (Item i : items) s += i.price;
            return items.isEmpty() ? 0 : s / items.size();
        }
    }
}
