using System;

namespace Smells;

/**
 * SMELL a: DUPLICATE CODE
 *
 * TASK
 *  1. Highlight the duplicated lines and describe, in a comment, how the three methods differ.
 *  2. Remove the duplication (name the refactoring you used) without changing behaviour.
 *  3. Bonus: what happens if the rounding rule changes? How many places would you edit before/after?
 *
 * DONE WHEN: demo() output is unchanged AND the meter finds no duplicated 4-line blocks.
 */
public class Smell01_DuplicateCode
{
    public static double EmployeeBonus(double salary, int years)
    {
        double bonus = salary * 0.05;
        if (years > 5) bonus += salary * 0.02;
        if (years > 10) bonus += salary * 0.03;
        return Math.Round(bonus * 100) / 100.0;
    }

    public static double ManagerBonus(double salary, int years)
    {
        double bonus = salary * 0.10;
        if (years > 5) bonus += salary * 0.02;
        if (years > 10) bonus += salary * 0.03;
        return Math.Round(bonus * 100) / 100.0;
    }

    public static double DirectorBonus(double salary, int years)
    {
        double bonus = salary * 0.15;
        if (years > 5) bonus += salary * 0.02;
        if (years > 10) bonus += salary * 0.03;
        return Math.Round(bonus * 100) / 100.0;
    }

    public static string Demo()
    {
        return EmployeeBonus(50000, 3).ToString("0.0", System.Globalization.CultureInfo.InvariantCulture) + ","
             + ManagerBonus(80000, 7).ToString("0.0", System.Globalization.CultureInfo.InvariantCulture) + ","
             + DirectorBonus(120000, 12).ToString("0.0", System.Globalization.CultureInfo.InvariantCulture);
    }
}
