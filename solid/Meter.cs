using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;

namespace Solid
{
    internal class Meter
    {
        public string Raw { get; }
        public string Code { get; }

        public Meter(string fileName)
        {
            string src = Read(fileName);
            Raw = Strip(src, false);
            Code = Strip(src, true);
        }

        private static string Read(string f)
        {
            try
            {
                if (File.Exists(f)) return File.ReadAllText(f);
                string path = Path.Combine(Directory.GetCurrentDirectory(), f);
                if (File.Exists(path)) return File.ReadAllText(path);
                path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, f);
                if (File.Exists(path)) return File.ReadAllText(path);
                return File.ReadAllText(f);
            }
            catch (Exception e)
            {
                throw new InvalidOperationException("Run the demo from inside its folder: " + e.Message, e);
            }
        }

        public static string Strip(string s, bool blankStrings)
        {
            var sb = new System.Text.StringBuilder();
            int i = 0, n = s.Length;
            while (i < n)
            {
                char c = s[i];
                if (c == '/' && i + 1 < n && s[i + 1] == '/')
                {
                    while (i < n && s[i] != '\n') { sb.Append(' '); i++; }
                }
                else if (c == '/' && i + 1 < n && s[i + 1] == '*')
                {
                    while (i < n && !(s[i] == '*' && i + 1 < n && s[i + 1] == '/'))
                    {
                        sb.Append(s[i] == '\n' ? '\n' : ' '); i++;
                    }
                    if (i < n) { sb.Append("  "); i += 2; }
                }
                else if (c == '"' || c == ''')
                {
                    char q = c;
                    sb.Append(q); i++;
                    while (i < n && s[i] != q)
                    {
                        if (s[i] == '\\' && i + 1 < n)
                        {
                            sb.Append(blankStrings ? "  " : s.Substring(i, 2)); i += 2;
                        }
                        else { sb.Append(blankStrings ? ' ' : s[i]); i++; }
                    }
                    if (i < n) { sb.Append(q); i++; }
                }
                else { sb.Append(c); i++; }
            }
            return sb.ToString();
        }

        public int Count(string pattern) => Regex.Matches(Code, pattern).Count;

        public int CountRaw(string literal)
        {
            int n = 0, i = 0;
            while ((i = Raw.IndexOf(literal, i, StringComparison.Ordinal)) >= 0) { n++; i += literal.Length; }
            return n;
        }

        public int Types() => Count(@"\b(class|interface|enum|struct|record)\s+\w+");
        public int Switches() => Count(@"\bswitch\b");

        public int DuplicateWindows()
        {
            var lines = new List<string>();
            foreach (var l in Raw.Split('\n'))
            {
                string t = Regex.Replace(l.Trim(), @"\s+", " ");
                if (string.IsNullOrEmpty(t) || Regex.IsMatch(t, @"^[{}();,\s]+$") || t.Equals("[Override]") || t.StartsWith("using ")) continue;
                lines.Add(t);
            }
            var seen = new HashSet<string>();
            var dups = new HashSet<string>();
            for (int i = 0; i + 4 <= lines.Count; i++)
            {
                string w = string.Join("\n", lines.GetRange(i, 4));
                if (!seen.Add(w)) dups.Add(w);
            }
            return dups.Count;
        }

        public record MethodInfo(string Name, int Params, int Lines, string Body);

        private static readonly HashSet<string> NotNames = new() { "if", "for", "while", "switch", "catch", "lock", "return", "else", "try", "new" };
        private static readonly HashSet<string> NotTypes = new() { "new", "return", "else", "record", "class", "interface", "enum", "throw", "struct" };
        private static readonly Regex Header = new(@"(?m)^[ \t]*(?:(?:public|private|protected|internal|static|readonly|virtual|override|abstract|async)\s+)*(?:<[^>]+>\s+)?(?:([\w.?]+(?:<[^>]*>)?(?:\[\])*)\s+)?(\w+)\s*\(([^()]*)\)\s*\{");

        public List<MethodInfo> Methods()
        {
            var result = new List<MethodInfo>();
            foreach (Match m in Header.Matches(Code))
            {
                string type = m.Groups[1].Value;
                string name = m.Groups[2].Value;
                if (NotNames.Contains(name) || (!string.IsNullOrEmpty(type) && NotTypes.Contains(type))) continue;
                int open = m.Index + m.Length - 1;
                int end = MatchBrace(open);
                string whole = Code.Substring(m.Index, Math.Min(end - m.Index + 1, Code.Length - m.Index));
                int lines = whole.Split('\n').Count(l => !string.IsNullOrWhiteSpace(l));
                result.Add(new MethodInfo(name, ParamCount(m.Groups[3].Value), lines, whole));
            }
            return result;
        }

        private int MatchBrace(int open)
        {
            int depth = 0;
            for (int j = open; j < Code.Length; j++)
            {
                char c = Code[j];
                if (c == '{') depth++;
                else if (c == '}' && --depth == 0) return j;
            }
            return Code.Length - 1;
        }

        public static int ParamCount(string p)
        {
            p = p.Trim();
            if (string.IsNullOrEmpty(p)) return 0;
            int depth = 0, n = 1;
            foreach (char c in p)
            {
                if (c == '<') depth++; else if (c == '>') depth--; else if (c == ',' && depth == 0) n++;
            }
            return n;
        }

        public int LongestMethod() => Methods().Select(m => m.Lines).DefaultIfEmpty(0).Max();
        public int MaxParams() => Methods().Select(m => m.Params).DefaultIfEmpty(0).Max();

        public int MaxGetterCallsOnOneReceiver()
        {
            int best = 0;
            var p = new Regex(@"\b(\w+)\.(?:Get|Is|[A-Z])\w*\(\)");
            foreach (var mt in Methods())
            {
                var perReceiver = new Dictionary<string, int>();
                foreach (Match m in p.Matches(mt.Body))
                {
                    string receiver = m.Groups[1].Value;
                    perReceiver[receiver] = perReceiver.GetValueOrDefault(receiver, 0) + 1;
                }
                foreach (int v in perReceiver.Values) best = Math.Max(best, v);
            }
            return best;
        }

        public int NewInsideClass(string cls)
        {
            var m = Regex.Match(Code, @"\bclass\s+" + cls + @"\b[^{]*\{");
            if (!m.Success) return -1;
            int start = m.Index + m.Length - 1;
            string body = Code.Substring(start, MatchBrace(start) - start + 1);
            return Regex.Matches(body, @"\bnew\b").Count;
        }
    }
}
