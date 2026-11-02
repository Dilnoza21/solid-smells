import java.util.List;
import java.util.Locale;

/**
 * SMELL l: SPECULATIVE GENERALITY
 *
 * "We might need other formats / subclasses / locales some day..." Today there is exactly ONE format,
 * ONE subclass, a hook nobody overrides, and a parameter nobody reads. All that flexibility has a cost
 * (indirection, more code to read) and no benefit.
 *
 * TASK
 *  1. List every piece of speculative machinery (interface with one implementation, abstract class with one
 *     subclass, unused hook, unused parameter).
 *  2. Remove it (Collapse Hierarchy, Inline Class, Remove Parameter) leaving the simplest code that works.
 *
 * DONE WHEN: demo() output is unchanged AND there is no `interface`/`abstract` and at most 2 types.
 *
 * Think: when WOULD it be right to keep the abstraction? (YAGNI vs. a real, present second implementation.)
 */
public class Smell12_SpeculativeGenerality {

    interface ReportFormat {
        String format(String title, List<String> lines);
    }

    static class PlainFormat implements ReportFormat {
        public String format(String title, List<String> lines) {
            return "== " + title + " ==|" + String.join("|", lines);
        }
    }

    static abstract class AbstractReportGenerator {
        abstract ReportFormat createFormat();

        protected void beforeRender() { }   // hook: nobody overrides it

        String generate(String title, List<String> lines, Locale locale /* never used */) {
            beforeRender();
            return createFormat().format(title, lines);
        }
    }

    static class ReportGenerator extends AbstractReportGenerator {
        ReportFormat createFormat() { return new PlainFormat(); }
    }

    static String demo() {
        return new ReportGenerator().generate("Sales", List.of("a", "b"), Locale.ROOT);
    }
}
