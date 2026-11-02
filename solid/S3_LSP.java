import java.util.*;

/**
 * EXERCISE L: Liskov Substitution Principle
 *
 * "Subtypes must be substitutable for their base types without breaking the program."
 *
 * Penguin IS-A Bird in biology, but not in this CODE: it cannot honour Bird's fly() contract.
 *
 * TASK
 *  1. Explain in a comment which expectation of Bird's clients Penguin breaks.
 *  2. Redesign the hierarchy so no subclass throws UnsupportedOperationException and no client
 *     needs try/catch or instanceof. (Hint: capabilities as small interfaces, e.g. Flyer, Swimmer.)
 *  3. Rewrite demo() so it reports Penguin as swimming 5m instead of crashing.
 *
 * DONE WHEN: demo() returns the expected string AND UnsupportedOperationException is gone.
 */
public class S3_LSP {

    static abstract class Bird {
        abstract String name();
        abstract int fly();   // metres
    }

    static class Sparrow extends Bird {
        String name() { return "Sparrow"; }
        int fly() { return 10; }
    }

    static class Eagle extends Bird {
        String name() { return "Eagle"; }
        int fly() { return 50; }
    }

    static class Penguin extends Bird {
        String name() { return "Penguin"; }
        int fly() { throw new UnsupportedOperationException("Penguins can't fly"); }
        int swim() { return 5; }
    }

    static String demo() {
        StringBuilder sb = new StringBuilder();
        for (Bird b : List.of(new Sparrow(), new Eagle(), new Penguin())) {
            try {
                int metres = b.fly();
                sb.append(b.name()).append(":fly=").append(metres).append("m;");
            } catch (UnsupportedOperationException e) {
                sb.append(b.name()).append(":CRASH;");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }
}
