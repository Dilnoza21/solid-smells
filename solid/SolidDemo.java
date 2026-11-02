import java.util.function.Supplier;

/**
 * Runner. Compile everything and run from inside this folder:
 *   javac *.java && java SolidDemo
 *
 * [PASS]/[FAIL]  behaviour check (demo() output). Must be green at the END; never break it.
 * [DONE]/[TODO]  structural check from Meter (heuristic): is the refactoring finished?
 */
public class SolidDemo {
    static int behaviourOk = 0, structureOk = 0, total = 0;

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
        if (ok) structureOk++;
        System.out.println((ok ? "[DONE] " : "[TODO] ") + id + " structure: " + goal + "  (" + measured + ")");
    }

    public static void main(String[] args) {
        Meter m;

        behaviour("S1 SRP", S1_SRP::demo,
            "Invoice for ACME| - Widget: 10.0| - Gadget: 25.5|Total (incl. VAT): 42.6;saved=1;outbox=[to=billing@acme.com body=72 chars]");
        m = new Meter("S1_SRP.java");
        structure("S1 SRP", ">= 4 types", m.types() >= 4, "types=" + m.types());

        behaviour("S2 OCP", S2_OCP::demo, "100.0,90.0,80.0,70.0,85.0");
        m = new Meter("S2_OCP.java");
        structure("S2 OCP", "no switch", m.switches() == 0, "switches=" + m.switches());

        behaviour("S3 LSP", S3_LSP::demo, "Sparrow:fly=10m;Eagle:fly=50m;Penguin:swim=5m");
        m = new Meter("S3_LSP.java");
        int uoe = m.count("UnsupportedOperationException");
        structure("S3 LSP", "no UnsupportedOperationException", uoe == 0, "occurrences=" + uoe);

        behaviour("S4 ISP", S4_ISP::demo, "MFP[print:a,scan:a,fax:a];Basic[print:b]");
        m = new Meter("S4_ISP.java");
        uoe = m.count("UnsupportedOperationException");
        int ifaces = m.count("\\binterface\\b");
        structure("S4 ISP", "no UnsupportedOperationException and >= 3 interfaces", uoe == 0 && ifaces >= 3,
                  "UOE=" + uoe + ", interfaces=" + ifaces);

        behaviour("S5 DIP", S5_DIP::demo, "placed=42;saved=[42];emailed=[a@b.com]");
        m = new Meter("S5_DIP.java");
        int news = m.newInsideClass("OrderService");
        structure("S5 DIP", "no `new` inside OrderService", news == 0, "new inside OrderService=" + news);

        System.out.printf("%nbehaviour green: %d/%d    refactorings done: %d/5%n", behaviourOk, total, structureOk);
    }
}
