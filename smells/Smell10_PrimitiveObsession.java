/**
 * SMELL j: PRIMITIVE OBSESSION
 *
 * Domain concepts are modelled with raw String / double / int, so rules are scattered, invalid values slip
 * through, and nothing stops you passing a phone number where an email is expected.
 *
 * TASK
 *  Replace primitives with small domain types (Replace Data Value with Object, Replace Type Code with Enum/Class):
 *    - email as String         -> Email (validates itself)
 *    - phone as String         -> PhoneNumber (knows how to format itself)
 *    - money as double         -> Money (why is double a bad choice for money? comment!)
 *    - tier as int 0/1/2       -> enum Tier (name + discount live here)
 *
 * DONE WHEN: demo() output is unchanged AND the file declares >= 4 types.
 */
public class Smell10_PrimitiveObsession {

    static boolean isValidEmail(String email) {
        return email != null && email.matches("[^@\\s]+@[^@\\s]+\\.[a-z]+");
    }

    static String formatPhone(String phone) {
        return "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6);
    }

    static double addMoney(double a, double b) {
        return Math.round((a + b) * 100) / 100.0;
    }

    static String tierName(int tier) {
        return tier == 2 ? "GOLD" : tier == 1 ? "SILVER" : "BASIC";
    }

    static double tierDiscount(int tier) {
        return tier == 2 ? 0.2 : tier == 1 ? 0.1 : 0.0;
    }

    static String demo() {
        return isValidEmail("a@b.com") + ";" + formatPhone("5551234567") + ";" + addMoney(0.1, 0.2)
                + ";" + tierName(2) + ":" + tierDiscount(2);
    }
}
