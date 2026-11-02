using System;

namespace Solid;

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
public class S4_ISP
{
    public interface Machine
    {
        string Print(string doc);
        string Scan(string doc);
        string Fax(string doc);
    }

    public class MultiFunctionPrinter : Machine
    {
        public string Print(string doc) => "print:" + doc;
        public string Scan(string doc)  => "scan:" + doc;
        public string Fax(string doc)   => "fax:" + doc;
    }

    public class BasicPrinter : Machine
    {
        public string Print(string doc) => "print:" + doc;
        public string Scan(string doc)  => throw new InvalidOperationException("no scanner");
        public string Fax(string doc)   => throw new InvalidOperationException("no fax");
    }

    public static string SendFax(Machine machine, string doc)
    {
        return machine.Fax(doc);
    }

    public static string Demo()
    {
        Machine mfp = new MultiFunctionPrinter();
        Machine basic = new BasicPrinter();
        return "MFP[" + mfp.Print("a") + "," + mfp.Scan("a") + "," + SendFax(mfp, "a") + "];Basic[" + basic.Print("b") + "]";
    }
}
