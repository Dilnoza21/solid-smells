/**
 * SMELL c: LARGE CLASS (a.k.a. God Class / Blob)
 *
 * UserAccount has many fields and methods that fall into clearly separate clusters.
 *
 * TASK
 *  1. Group the fields/methods into clusters (comment each group).
 *  2. Use Extract Class so each cluster becomes its own small class (e.g. Address, PaymentCard, Preferences);
 *     UserAccount keeps only what is really about the account and delegates/composes the rest.
 *  3. Rewrite demo() for the new design; the output must not change.
 *
 * DONE WHEN: demo() output is unchanged AND the file declares >= 4 types.
 */
public class Smell03_LargeClass {

    static class UserAccount {
        // identity
        String username;
        String email;
        String passwordHash = "x";
        // address
        String street;
        String city;
        String zip;
        String country;
        // payment
        String cardNumber;
        int cardExpiryYear;
        int cardExpiryMonth;
        // preferences
        boolean newsletter;
        String language = "en";
        boolean darkMode;

        UserAccount(String username, String email) {
            this.username = username;
            this.email = email;
        }

        void changePassword(String oldHash, String newHash) {
            if (!passwordHash.equals(oldHash)) throw new IllegalArgumentException("wrong password");
            passwordHash = newHash;
        }

        boolean hasValidEmail() { return email.indexOf('@') > 0; }
        String formattedAddress() { return street + ", " + zip + " " + city + ", " + country; }
        boolean hasValidZip() { return zip != null && zip.matches("\\d{5}"); }
        String maskedCard() { return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4); }

        boolean isCardExpired(int year, int month) {
            return cardExpiryYear < year || (cardExpiryYear == year && cardExpiryMonth < month);
        }

        void toggleDarkMode() { darkMode = !darkMode; }

        String describePreferences() {
            return language + (darkMode ? "/dark" : "/light") + (newsletter ? "/news" : "");
        }
    }

    static String demo() {
        UserAccount u = new UserAccount("ann", "ann@x.com");
        u.street = "1 Main St"; u.city = "Paris"; u.zip = "75001"; u.country = "FR";
        u.cardNumber = "4111111111111234"; u.cardExpiryYear = 2025; u.cardExpiryMonth = 6;
        u.toggleDarkMode(); u.newsletter = true;
        return u.formattedAddress() + ";" + u.hasValidZip() + ";" + u.maskedCard() + ";"
                + u.isCardExpired(2026, 1) + ";" + u.describePreferences() + ";" + u.hasValidEmail();
    }
}
