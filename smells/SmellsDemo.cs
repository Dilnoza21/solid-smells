using System;

namespace Smells;

public class SmellsDemo
{
    static int behaviourOk = 0, total = 0, done = 0;

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
        if (ok) done++;
        Console.WriteLine((ok ? "[DONE] " : "[TODO] ") + id + " smell removed: " + goal + "  (" + measured + ")");
    }

    static void Manual(string id, string note)
    {
        Console.WriteLine("[ ??? ] " + id + " " + note);
    }

    public static void Main(string[] args)
    {
        Meter m;

        Behaviour("01 Duplicate Code", Smell01_DuplicateCode.Demo, "2500.0,9600.0,24000.0");
        m = new Meter("Smell01_DuplicateCode.cs");
        Structure("01", "no duplicated 4-line blocks", m.DuplicateWindows() == 0, "duplicate blocks=" + m.DuplicateWindows());

        Manual("07/08", "Heuristics only approximate Feature Envy and Data Class: also self-review the design.");
        Console.WriteLine($"\nbehaviour green: {behaviourOk}/{total}    smells removed: {done}/12");
    }
}
