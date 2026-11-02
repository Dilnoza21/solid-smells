using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;

namespace Solid;

/**
 * EXERCISE S: Single Responsibility Principle
 *
 * "A class should have one, and only one, reason to change."
 *
 * Invoice below has FOUR reasons to change (pricing, presentation, persistence, notification).
 *
 * TASK
 *  1. Above each method, write which actor/reason would force it to change.
 *  2. Split Invoice into cohesive classes (e.g. Invoice, InvoicePrinter, InvoiceRepository, InvoiceMailer).
 *  3. Rewrite demo() to use your new classes. It must still return EXACTLY the expected string.
 *
 * DONE WHEN: behaviour check stays green AND the file declares >= 4 types.
 */
public class S1_SRP
{
    public class Invoice
    {
        private readonly string customer;
        private readonly List<string> names = [];
        private readonly List<double> prices = [];
        public readonly List<string> outbox = [];       // pretend SMTP server
        public readonly Dictionary<string, string> db = [];      // pretend database

        public Invoice(string customer) { this.customer = customer; }

        public void AddItem(string name, double price) { names.Add(name); prices.Add(price); }

        public double Total()
        {
            double sum = 0;
            foreach (double p in prices) sum += p;
            return Math.Round(sum * 1.2 * 100) / 100.0;       // 20% VAT
        }

        public string ToText()
        {
            var sb = new StringBuilder("Invoice for " + customer + "\n");
            for (int i = 0; i < names.Count; i++)
            {
                sb.Append(" - ").Append(names[i]).Append(": ").Append(prices[i].ToString("0.0", CultureInfo.InvariantCulture)).Append("\n");
            }
            sb.Append("Total (incl. VAT): ").Append(Total().ToString("0.0", CultureInfo.InvariantCulture));
            return sb.ToString();
        }

        public void Save() { db[customer] = ToText(); }

        public void Email(string address)
        {
            outbox.Add("to=" + address + " body=" + ToText().Length + " chars");
        }
    }

    public static string Demo()
    {
        Invoice inv = new Invoice("ACME");
        inv.AddItem("Widget", 10.0);
        inv.AddItem("Gadget", 25.5);
        inv.Save();
        inv.Email("billing@acme.com");
        return inv.ToText().Replace("\n", "|") + ";saved=" + inv.db.Count + ";outbox=[" + string.Join(", ", inv.outbox) + "]";
    }
}
