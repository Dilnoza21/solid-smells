/**
 * SMELL h: DATA CLASS
 *
 * BankAccount is a bag of getters/setters; all the rules live in AccountService, so any code can put an
 * account into an invalid state (negative balance, frozen but still withdrawing...).
 *
 * TASK
 *  1. List the invariants the rules protect.
 *  2. Move the behaviour (deposit / withdraw / freeze) INTO BankAccount, remove the public setters and
 *     make the class protect its own invariants (Encapsulate, Move Method).
 *
 * DONE WHEN: demo() output is unchanged AND no setters remain.
 */
public class Smell08_DataClass {

    static class BankAccount {
        private double balance;
        private boolean frozen;

        double getBalance() { return balance; }
        void setBalance(double balance) { this.balance = balance; }
        boolean isFrozen() { return frozen; }
        void setFrozen(boolean frozen) { this.frozen = frozen; }
    }

    static class AccountService {
        void deposit(BankAccount a, double amount) {
            if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
            a.setBalance(a.getBalance() + amount);
        }

        boolean withdraw(BankAccount a, double amount) {
            if (a.isFrozen() || amount > a.getBalance()) return false;
            a.setBalance(a.getBalance() - amount);
            return true;
        }

        void freeze(BankAccount a) { a.setFrozen(true); }
    }

    static String demo() {
        BankAccount acc = new BankAccount();
        AccountService svc = new AccountService();
        svc.deposit(acc, 100);
        boolean w1 = svc.withdraw(acc, 30);
        svc.freeze(acc);
        boolean w2 = svc.withdraw(acc, 10);
        return "balance=" + acc.getBalance() + ";w1=" + w1 + ";w2=" + w2;
    }
}
