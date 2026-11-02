/**
 * EXERCISE I: Interface Segregation Principle
 *
 * "Clients should not be forced to depend on methods they do not use."
 *
 * BasicPrinter is forced to implement scan() and fax() just to satisfy Machine.
 *
 * TASK
 *  1. Split Machine into role interfaces (e.g. Printer, Scanner, FaxSender).
 *  2. MultiFunctionPrinter implements all of them; BasicPrinter implements only Printer.
 *  3. Change sendFax() so it accepts ONLY what it needs (a FaxSender, not a whole Machine).
 *
 * DONE WHEN: demo() still returns the expected string, UnsupportedOperationException is gone
 *            and the file declares >= 3 interfaces.
 */
public class S4_ISP {

    interface Machine {
        String print(String doc);
        String scan(String doc);
        String fax(String doc);
    }

    static class MultiFunctionPrinter implements Machine {
        public String print(String doc) { return "print:" + doc; }
        public String scan(String doc)  { return "scan:" + doc; }
        public String fax(String doc)   { return "fax:" + doc; }
    }

    static class BasicPrinter implements Machine {
        public String print(String doc) { return "print:" + doc; }
        public String scan(String doc)  { throw new UnsupportedOperationException("no scanner"); }
        public String fax(String doc)   { throw new UnsupportedOperationException("no fax"); }
    }

    static String sendFax(Machine machine, String doc) {
        return machine.fax(doc);
    }

    static String demo() {
        Machine mfp = new MultiFunctionPrinter();
        Machine basic = new BasicPrinter();
        return "MFP[" + mfp.print("a") + "," + mfp.scan("a") + "," + sendFax(mfp, "a") + "];Basic[" + basic.print("b") + "]";
    }
}
