using System;
using System.Collections.Generic;
using System.Globalization;

namespace Solid;

/**
 * EXERCISE O: Open/Closed Principle
 *
 * "Software entities should be open for extension, but closed for modification."
 *
 * Every new customer type forces an edit to DiscountCalculator.apply().
 *
 * TASK
 *  1. Marketing wants a STUDENT discount of 15%. First add it the "wrong" way (edit the switch).
 *     Note the diff: which existing, already-tested code did you have to touch?
 *  2. Refactor (Strategy / polymorphism / registry) so that supporting a NEW customer type
 *     needs NO change to DiscountCalculator. Add STUDENT again the "right" way.
 *  3. Remove the switch.
 *
 * DONE WHEN: demo() returns the expected string (including STUDENT) AND there is no `switch`.
 */
public class S2_OCP
{
    public class DiscountCalculator
    {
        public double Apply(string customerType, double amount)
        {
            switch (customerType)
            {
                case "REGULAR":  return amount;
                case "SILVER":   return amount * 0.90;
                case "GOLD":     return amount * 0.80;
                case "EMPLOYEE": return amount * 0.70;
                default: throw new ArgumentException("Unknown type: " + customerType);
            }
        }
    }

    public static string Demo()
    {
        DiscountCalculator calc = new DiscountCalculator();
        List<string> outList = [];
        foreach (string type in new[] { "REGULAR", "SILVER", "GOLD", "EMPLOYEE", "STUDENT" })
        {
            try
            {
                outList.Add(calc.Apply(type, 100.0).ToString("0.0", CultureInfo.InvariantCulture));
            }
            catch (ArgumentException)
            {
                outList.Add("ERR");
            }
        }
        return string.Join(",", outList);
    }
}
