/**
 * SMELL f: LONG PARAMETER LIST
 *
 * TASK
 *  1. Which arguments of createUser() are easy to swap by accident without the compiler noticing?
 *  2. Introduce Parameter Object(s) (and/or Preserve Whole Object, Builder) so no method takes more than 3 parameters.
 *  3. Update welcomeLine() and demo() accordingly.
 *
 * DONE WHEN: demo() output is unchanged AND no method has more than 3 parameters.
 */
public class Smell06_LongParameterList {

    static String createUser(String firstName, String lastName, String email,
                             String street, String city, String zip, String country,
                             boolean newsletter, boolean admin) {
        return firstName + " " + lastName + " <" + email + "> @ " + street + ", " + zip + " " + city + ", " + country
                + (newsletter ? " [news]" : "") + (admin ? " [admin]" : "");
    }

    static String welcomeLine(String firstName, String lastName, String email, String city, String country) {
        return "Welcome " + firstName + " " + lastName + " from " + city + ", " + country + " (" + email + ")";
    }

    static String demo() {
        return createUser("Ada", "Lovelace", "ada@x.org", "1 Main St", "London", "N1", "UK", true, false)
                + "|" + welcomeLine("Ada", "Lovelace", "ada@x.org", "London", "UK");
    }
}
