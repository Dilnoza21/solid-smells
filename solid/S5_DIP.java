/**
 * EXERCISE D: Dependency Inversion Principle
 *
 * "High-level modules should not depend on low-level modules. Both should depend on abstractions."
 *
 * OrderService (business policy) creates its own MySQL repository and SMTP mailer, so you cannot
 * test it without a database and a mail server, or swap either one.
 *
 * TASK
 *  1. Introduce abstractions owned by the high-level code (OrderRepository, EmailSender).
 *  2. Make MySqlOrderRepository / SmtpEmailSender implement them.
 *  3. Inject the dependencies through OrderService's constructor. OrderService must contain no `new`.
 *  4. Write two in-memory FAKES and use them in demo() to report what was saved and emailed.
 *
 * DONE WHEN: demo() returns the expected string AND OrderService contains no `new`.
 */
public class S5_DIP {

    static class MySqlOrderRepository {
        void save(int orderId) { /* pretend: INSERT INTO orders ... */ }
    }

    static class SmtpEmailSender {
        void send(String to, String body) { /* pretend: open SMTP connection ... */ }
    }

    static class OrderService {
        private final MySqlOrderRepository repository = new MySqlOrderRepository();
        private final SmtpEmailSender mailer = new SmtpEmailSender();

        String place(int orderId, String customerEmail) {
            repository.save(orderId);
            mailer.send(customerEmail, "Order " + orderId + " confirmed");
            return "placed=" + orderId;
        }
    }

    static String demo() {
        String result = new OrderService().place(42, "a@b.com");
        // TODO: after applying DIP, inject fakes and report what they captured instead of "?"
        return result + ";saved=[?];emailed=[?]";
    }
}
