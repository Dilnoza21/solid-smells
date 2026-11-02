using System;

namespace Solid;

public class SolidDemo
{
    static int behaviourOk = 0, structureOk = 0, total = 0;

    static void Behaviour(string id, Func<string> demo, string expected)
    {
        total++;
        string actual;
        try { actual = demo(); } catch (Exception t) { actual = "threw " + t.Message; }
        bool ok = expected.Equals(actual);
        if (ok) behaviourOk++;
        Console.WriteLine((ok ? "[PASS] " : "[FAIL] ") + id + " behaviour");
        if (!ok) Console.WriteLine("         expected: " + expected + "\n         actual:   " + actual);
    }

    static void Structure(string id, string goal, bool ok, string measured)
    {
        if (ok) structureOk++;
        Console.WriteLine((ok ? "[DONE] " : "[TODO] ") + id + " structure: " + goal + "  (" + measured + ")");
    }

    public static void Main(string[] args)
    {
        Meter m;

        Behaviour("S1 SRP", S1_SRP.Demo,
            "Invoice for ACME| - Widget: 10.0| - Gadget: 25.5|Total (incl. VAT): 42.6;saved=1;outbox=[to=billing@acme.com body=72 chars]");
        m = new Meter("S1_SRP.cs");
        Structure("S1 SRP", ">= 4 types", m.Types() >= 4, "types=" + m.Types());

        Behaviour("S2 OCP", S2_OCP.Demo, "100.0,90.0,80.0,70.0,85.0");
        m = new Meter("S2_OCP.cs");
        Structure("S2 OCP", "no switch", m.Switches() == 0, "switches=" + m.Switches());

        Behaviour("S3 LSP", S3_LSP.Demo, "Sparrow:fly=10m;Eagle:fly=50m;Penguin:swim=5m");
        m = new Meter("S3_LSP.cs");
        int uoe = m.Count("InvalidOperationException") + m.Count("UnsupportedOperationException");
        Structure("S3 LSP", "no UnsupportedOperationException", uoe == 0, "occurrences=" + uoe);

        Behaviour("S4 ISP", S4_ISP.Demo, "MFP[print:a,scan:a,fax:a];Basic[print:b]");
        m = new Meter("S4_ISP.cs");
        uoe = m.Count("InvalidOperationException") + m.Count("UnsupportedOperationException");
        int ifaces = m.Count(@"\binterface\b");
        Structure("S4 ISP", "no UnsupportedOperationException and >= 3 interfaces", uoe == 0 && ifaces >= 3,
                  "UOE=" + uoe + ", interfaces=" + ifaces);

        Behaviour("S5 DIP", S5_DIP.Demo, "placed=42;saved=[42];emailed=[a@b.com]");
        m = new Meter("S5_DIP.cs");
        int news = m.NewInsideClass("OrderService");
        Structure("S5 DIP", "no `new` inside OrderService", news == 0, "new inside OrderService=" + news);

        Console.WriteLine($"\nbehaviour green: {behaviourOk}/{total}    refactorings done: {structureOk}/5");
    }
}
