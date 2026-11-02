import java.util.function.Supplier;

/**
 * Runner. Compile everything and run from inside this folder:
 *   javac *.java && java SmellsDemo
 *
 * [PASS]/[FAIL]  behaviour check (demo() output). Green from the start: it is your SAFETY NET.
 *                A refactoring must never turn it red.
 * [DONE]/[TODO]  structural check (heuristic): has the smell actually been removed?
 */
public class SmellsDemo {
    static int behaviourOk = 0, total = 0, done = 0;

    static void behaviour(String id, Supplier<String> demo, String expected) {
        total++;
        String actual;
        try { actual = demo.get(); } catch (Throwable t) { actual = "threw " + t; }
        boolean ok = expected.equals(actual);
        if (ok) behaviourOk++;
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + id + " behaviour");
        if (!ok) System.out.println("         expected: " + expected + "\n         actual:   " + actual);
    }

    static void structure(String id, String goal, boolean ok, String measured) {
        if (ok) done++;
        System.out.println((ok ? "[DONE] " : "[TODO] ") + id + " smell removed: " + goal + "  (" + measured + ")");
    }

    static void manual(String id, String note) {
        System.out.println("[ ??? ] " + id + " " + note);
    }

    public static void main(String[] args) {
        Meter m;

        behaviour("01 Duplicate Code", Smell01_DuplicateCode::demo, "2500.0,9600.0,24000.0");
        m = new Meter("Smell01_DuplicateCode.java");
        structure("01", "no duplicated 4-line blocks", m.duplicateWindows() == 0, "duplicate blocks=" + m.duplicateWindows());

        behaviour("02 Long Method", Smell02_LongMethod::demo, "n=4 sum=280 min=40 max=100 mean=70.00 sd=22.36 pass=75.0% grade=C");
        m = new Meter("Smell02_LongMethod.java");
        structure("02", "longest method <= 15 lines", m.longestMethod() <= 15, "longest=" + m.longestMethod());

        behaviour("03 Large Class", Smell03_LargeClass::demo, "1 Main St, 75001 Paris, FR;true;**** **** **** 1234;true;en/dark/news;true");
        m = new Meter("Smell03_LargeClass.java");
        structure("03", ">= 4 types (class was split)", m.types() >= 4, "types=" + m.types());

        behaviour("04 Divergent Change", Smell04_DivergentChange::demo, "pen: 2.40|laptop: 972.00~<ul><li>pen: 2.40</li><li>laptop: 972.00</li></ul>");
        m = new Meter("Smell04_DivergentChange.java");
        structure("04", ">= 4 types (one per axis of change)", m.types() >= 4, "types=" + m.types());

        behaviour("05 Shotgun Surgery", Smell05_ShotgunSurgery::demo, "pen $1.50|Total: $12.00|Paid $12.00|Your order of $12.00 shipped");
        m = new Meter("Smell05_ShotgunSurgery.java");
        int fmt = m.countRaw("%.2f");
        structure("05", "money format appears once", fmt <= 1, "occurrences of the format=" + fmt);

        behaviour("06 Long Parameter List", Smell06_LongParameterList::demo, "Ada Lovelace <ada@x.org> @ 1 Main St, N1 London, UK [news]|Welcome Ada Lovelace from London, UK (ada@x.org)");
        m = new Meter("Smell06_LongParameterList.java");
        structure("06", "max 3 parameters per method", m.maxParams() <= 3, "max params=" + m.maxParams());

        behaviour("07 Feature Envy", Smell07_FeatureEnvy::demo, "107.5,10.0,15.5");
        m = new Meter("Smell07_FeatureEnvy.java");
        structure("07", "<= 2 getter calls on one object per method", m.maxGetterCallsOnOneReceiver() <= 2,
                  "max getter calls on one object=" + m.maxGetterCallsOnOneReceiver());

        behaviour("08 Data Class", Smell08_DataClass::demo, "balance=70.0;w1=true;w2=false");
        m = new Meter("Smell08_DataClass.java");
        int setters = m.count("\\bset[A-Z]\\w*\\s*\\(");
        structure("08", "no setters", setters == 0, "setter declarations/calls=" + setters);

        behaviour("09 Data Clumps", Smell09_DataClumps::demo, "111;true;HQ@1.5,2.5");
        m = new Meter("Smell09_DataClumps.java");
        structure("09", "max 3 parameters per method", m.maxParams() <= 3, "max params=" + m.maxParams());

        behaviour("10 Primitive Obsession", Smell10_PrimitiveObsession::demo, "true;(555) 123-4567;0.3;GOLD:0.2");
        m = new Meter("Smell10_PrimitiveObsession.java");
        structure("10", ">= 4 types (domain concepts got types)", m.types() >= 4, "types=" + m.types());

        behaviour("11 Switch Statements", Smell11_SwitchStatements::demo, "circle:3.14/6.28;rect:6.00/10.00;triangle:6.00/12.00");
        m = new Meter("Smell11_SwitchStatements.java");
        structure("11", "no switch", m.switches() == 0, "switches=" + m.switches());

        behaviour("12 Speculative Generality", Smell12_SpeculativeGenerality::demo, "== Sales ==|a|b");
        m = new Meter("Smell12_SpeculativeGenerality.java");
        int abs = m.count("\\b(interface|abstract)\\b");
        structure("12", "no interface/abstract and <= 2 types", abs == 0 && m.types() <= 2,
                  "interface/abstract=" + abs + ", types=" + m.types());

        manual("07/08", "Heuristics only approximate Feature Envy and Data Class: also self-review the design.");
        System.out.printf("%nbehaviour green: %d/%d    smells removed: %d/12%n", behaviourOk, total, done);
    }
}
