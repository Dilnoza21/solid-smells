import java.util.Locale;

/**
 * SMELL k: SWITCH STATEMENTS (type-code conditionals)
 *
 * The same switch on `type` is repeated in area(), perimeter(), and describe(). Adding a new shape means
 * hunting down every switch (and forgetting one).
 *
 * TASK
 *  Replace Conditional with Polymorphism: one Shape type with an implementation per kind, each knowing its
 *  own area/perimeter/name. Adding "square" should mean adding ONE class.
 *
 * DONE WHEN: demo() output is unchanged AND there is no `switch` left.
 *
 * Think: which SOLID principle does the original design violate? (Answer in a comment.)
 */
public class Smell11_SwitchStatements {

    static class Shape {
        final String type;
        final double a, b;
        Shape(String type, double a, double b) { this.type = type; this.a = a; this.b = b; }
    }

    static double area(Shape s) {
        switch (s.type) {
            case "circle":   return Math.PI * s.a * s.a;
            case "rect":     return s.a * s.b;
            case "triangle": return 0.5 * s.a * s.b;
            default: throw new IllegalArgumentException(s.type);
        }
    }

    static double perimeter(Shape s) {
        switch (s.type) {
            case "circle":   return 2 * Math.PI * s.a;
            case "rect":     return 2 * (s.a + s.b);
            case "triangle": return s.a + s.b + Math.hypot(s.a, s.b);
            default: throw new IllegalArgumentException(s.type);
        }
    }

    static String describe(Shape s) {
        String name;
        switch (s.type) {
            case "circle":   name = "circle"; break;
            case "rect":     name = "rect"; break;
            case "triangle": name = "triangle"; break;
            default: throw new IllegalArgumentException(s.type);
        }
        return String.format(Locale.ROOT, "%s:%.2f/%.2f", name, area(s), perimeter(s));
    }

    static String demo() {
        return describe(new Shape("circle", 1, 0)) + ";" + describe(new Shape("rect", 2, 3))
                + ";" + describe(new Shape("triangle", 3, 4));
    }
}
