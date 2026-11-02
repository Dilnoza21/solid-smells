import java.util.List;

/**
 * SMELL g: FEATURE ENVY
 *
 * ShippingCalculator is far more interested in Order's data than in its own.
 *
 * TASK
 *  1. Underline every call ShippingCalculator makes to Order's getters.
 *  2. Move Method: put the behaviour where the data lives (Order). Order's getters may become unnecessary.
 *  3. Update demo().
 *
 * DONE WHEN: demo() output is unchanged AND no method calls more than 2 getters on the same object.
 */
public class Smell07_FeatureEnvy {

    static class Order {
        private final double weightKg;
        private final String country;
        private final boolean express;
        private final List<Double> prices;

        Order(double weightKg, String country, boolean express, List<Double> prices) {
            this.weightKg = weightKg;
            this.country = country;
            this.express = express;
            this.prices = prices;
        }

        double getWeightKg() { return weightKg; }
        String getCountry() { return country; }
        boolean isExpress() { return express; }
        List<Double> getPrices() { return prices; }
    }

    static class ShippingCalculator {
        double cost(Order o) {
            double base = o.getWeightKg() * 2.0;
            if (!o.getCountry().equals("US")) base += 15;
            if (o.isExpress()) base *= 1.5;
            if (o.getWeightKg() > 20) base += 10;
            return base;
        }

        double itemsTotal(Order o) {
            double sum = 0;
            for (double p : o.getPrices()) sum += p;
            return sum;
        }
    }

    static String demo() {
        ShippingCalculator calc = new ShippingCalculator();
        Order heavy = new Order(25, "DE", true, List.of(10.0, 5.5));
        Order light = new Order(5, "US", false, List.of());
        return calc.cost(heavy) + "," + calc.cost(light) + "," + calc.itemsTotal(heavy);
    }
}
