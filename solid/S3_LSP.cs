using System;
using System.Collections.Generic;
using System.Text;

namespace Solid;

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
public class S3_LSP
{
    public abstract class Bird
    {
        public abstract string Name();
        public abstract int Fly();   // metres
    }

    public class Sparrow : Bird
    {
        public override string Name() => "Sparrow";
        public override int Fly() => 10;
    }

    public class Eagle : Bird
    {
        public override string Name() => "Eagle";
        public override int Fly() => 50;
    }

    public class Penguin : Bird
    {
        public override string Name() => "Penguin";
        public override int Fly() => throw new InvalidOperationException("Penguins can't fly");
        public int Swim() => 5;
    }

    public static string Demo()
    {
        var sb = new StringBuilder();
        var birds = new List<Bird> { new Sparrow(), new Eagle(), new Penguin() };
        foreach (Bird b in birds)
        {
            try
            {
                int metres = b.Fly();
                sb.Append(b.Name()).Append(":fly=").Append(metres).Append("m;");
            }
            catch (InvalidOperationException)
            {
                sb.Append(b.Name()).Append(":CRASH;");
            }
        }
        return sb.ToString().TrimEnd(';');
    }
}
