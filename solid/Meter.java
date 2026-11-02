import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;

/**
 * Heuristic source-code metrics used by the runner to tell you whether a refactoring
 * "took". It is NOT a real parser: it is good enough for feedback, not for grading
 * adversarial code. Do not modify.
 */
final class Meter {
    final String raw;   // comments removed, string literals kept
    final String code;  // comments removed, string/char literal contents blanked

    Meter(String fileName) {
        String src = read(fileName);
        this.raw = strip(src, false);
        this.code = strip(src, true);
    }

    private static String read(String f) {
        try { return Files.readString(Path.of(f)); }
        catch (Exception e) { throw new RuntimeException("Run the demo from inside its folder: " + e); }
    }

    static String strip(String s, boolean blankStrings) {
        StringBuilder out = new StringBuilder();
        int i = 0, n = s.length();
        while (i < n) {
            char c = s.charAt(i);
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                while (i < n && s.charAt(i) != '\n') { out.append(' '); i++; }
            } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                while (i < n && !(s.charAt(i) == '*' && i + 1 < n && s.charAt(i + 1) == '/')) {
                    out.append(s.charAt(i) == '\n' ? '\n' : ' '); i++;
                }
                if (i < n) { out.append("  "); i += 2; }
            } else if (c == '"' || c == '\'') {
                char q = c;
                out.append(q); i++;
                while (i < n && s.charAt(i) != q) {
                    if (s.charAt(i) == '\\' && i + 1 < n) {
                        out.append(blankStrings ? "  " : s.substring(i, i + 2)); i += 2;
                    } else { out.append(blankStrings ? ' ' : s.charAt(i)); i++; }
                }
                if (i < n) { out.append(q); i++; }
            } else { out.append(c); i++; }
        }
        return out.toString();
    }

    // ---------- simple counters ----------
    int count(String regex) {
        Matcher m = Pattern.compile(regex).matcher(code);
        int n = 0; while (m.find()) n++; return n;
    }

    int countRaw(String literal) {
        int n = 0, i = 0;
        while ((i = raw.indexOf(literal, i)) >= 0) { n++; i += literal.length(); }
        return n;
    }

    /** classes + interfaces + enums + records declared in the file */
    int types() { return count("\\b(class|interface|enum|record)\\s+\\w+"); }
    int switches() { return count("\\bswitch\\b"); }

    // ---------- duplicate code: identical windows of 4 meaningful lines ----------
    int duplicateWindows() {
        List<String> lines = new ArrayList<>();
        for (String l : raw.split("\n")) {
            String t = l.trim().replaceAll("\\s+", " ");
            if (t.isEmpty() || t.matches("[{}();,\\s]+") || t.equals("@Override") || t.startsWith("import ")) continue;
            lines.add(t);
        }
        Set<String> seen = new HashSet<>(), dups = new HashSet<>();
        for (int i = 0; i + 4 <= lines.size(); i++) {
            String w = String.join("\n", lines.subList(i, i + 4));
            if (!seen.add(w)) dups.add(w);
        }
        return dups.size();
    }

    // ---------- methods ----------
    record Method(String name, int params, int lines, String body) {}

    private static final Set<String> NOT_NAMES =
            Set.of("if", "for", "while", "switch", "catch", "synchronized", "return", "else", "try", "new");
    private static final Set<String> NOT_TYPES =
            Set.of("new", "return", "else", "record", "class", "interface", "enum", "throw");
    private static final Pattern HEADER = Pattern.compile(
            "(?m)^[ \\t]*(?:(?:public|private|protected|static|final|abstract|synchronized|default)\\s+)*"
          + "(?:<[^>]+>\\s+)?(?:([\\w.?]+(?:<[^>]*>)?(?:\\[\\])*)\\s+)?(\\w+)\\s*\\(([^()]*)\\)\\s*"
          + "(?:throws\\s+[\\w.,\\s]+)?\\{");

    List<Method> methods() {
        List<Method> result = new ArrayList<>();
        Matcher m = HEADER.matcher(code);
        while (m.find()) {
            String type = m.group(1), name = m.group(2);
            if (NOT_NAMES.contains(name) || (type != null && NOT_TYPES.contains(type))) continue;
            int open = m.end() - 1, end = matchBrace(open);
            String whole = code.substring(m.start(), Math.min(end + 1, code.length()));
            int lines = (int) whole.lines().filter(l -> !l.isBlank()).count();
            result.add(new Method(name, paramCount(m.group(3)), lines, whole));
        }
        return result;
    }

    private int matchBrace(int open) {
        int depth = 0;
        for (int j = open; j < code.length(); j++) {
            char c = code.charAt(j);
            if (c == '{') depth++;
            else if (c == '}' && --depth == 0) return j;
        }
        return code.length() - 1;
    }

    static int paramCount(String p) {
        p = p.trim();
        if (p.isEmpty()) return 0;
        int depth = 0, n = 1;
        for (char c : p.toCharArray()) {
            if (c == '<') depth++; else if (c == '>') depth--; else if (c == ',' && depth == 0) n++;
        }
        return n;
    }

    int longestMethod() { return methods().stream().mapToInt(Method::lines).max().orElse(0); }
    int maxParams() { return methods().stream().mapToInt(Method::params).max().orElse(0); }

    /** Feature-envy heuristic: most getter calls (getX()/isX()) on a single receiver inside one method. */
    int maxGetterCallsOnOneReceiver() {
        int best = 0;
        Pattern p = Pattern.compile("\\b(\\w+)\\.(?:get|is)[A-Z]\\w*\\(\\)");
        for (Method mt : methods()) {
            Map<String, Integer> perReceiver = new HashMap<>();
            Matcher m = p.matcher(mt.body());
            while (m.find()) perReceiver.merge(m.group(1), 1, Integer::sum);
            for (int v : perReceiver.values()) best = Math.max(best, v);
        }
        return best;
    }

    /** Number of `new` expressions inside the body of the named class (-1 if the class is missing). */
    int newInsideClass(String cls) {
        Matcher m = Pattern.compile("\\bclass\\s+" + cls + "\\b[^{]*\\{").matcher(code);
        if (!m.find()) return -1;
        String body = code.substring(m.end() - 1, matchBrace(m.end() - 1) + 1);
        Matcher n = Pattern.compile("\\bnew\\b").matcher(body);
        int c = 0; while (n.find()) c++;
        return c;
    }
}
