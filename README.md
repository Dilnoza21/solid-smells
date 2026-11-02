# SOLID Principles & Code Smells: Refactoring Exercises (Java 17+)

You will **analyse and critique** badly designed code, **explain** the anti-patterns (code smells) in it,
and **refactor** it without changing behaviour.

```
solid/    5 exercises  (S, O, L, I, D)      run: cd solid  && javac *.java && java SolidDemo
smells/   12 exercises + a review capstone  run: cd smells && javac *.java && java SmellsDemo
solutions/  reference solutions + ANSWER_KEY.md   (instructor copy: remove before handing out)
```

## How the exercises work

Every exercise is **one Java file** with a header comment (the task) and a `demo()` method.

| Marker | Meaning |
|--------|---------|
| `[PASS]` / `[FAIL]` | **Behaviour check**: does `demo()` return the exact expected string? For smells (and S1, S4) it is green at the start: it's your **safety net**. Never leave it red. |
| `[TODO]` / `[DONE]` | **Structure check**: a heuristic "smell meter" (`Meter.java`) that tells you whether the refactoring really happened (e.g. "no `switch` left", "max 3 parameters"). |

Rules: edit only the exercise file (you may add classes **inside** it, and you may rewrite `demo()` to use your new design as long as it returns the same string). Don't edit `Meter.java` or the runners. The meter is a heuristic, so passing it doesn't prove good design. You still need to be able to justify your solution.

For each exercise, **before refactoring write a 1-2 sentence diagnosis in a comment** (what is wrong, why it hurts), then refactor, then name the refactoring you used.

---

## Part 1: SOLID (`solid/`)

| File | Principle | Your job | Done when |
|------|-----------|----------|-----------|
| `S1_SRP.java` | Single Responsibility | Split an `Invoice` that has four reasons to change | >= 4 types |
| `S2_OCP.java` | Open/Closed | Add a STUDENT discount the wrong way, then refactor so new types need no edits | no `switch`, STUDENT works |
| `S3_LSP.java` | Liskov Substitution | Fix a `Penguin extends Bird` that can't `fly()` | no `UnsupportedOperationException` |
| `S4_ISP.java` | Interface Segregation | Split the fat `Machine` interface | >= 3 interfaces, no `UnsupportedOperationException` |
| `S5_DIP.java` | Dependency Inversion | Invert the hard-wired MySQL/SMTP dependencies; test with fakes | no `new` inside `OrderService` |

### SOLID reflection questions (answer in 2-3 sentences each)
1. S1: How did you decide where to draw the boundaries? How is "reason to change" different from "does one thing"?
2. S2: When is a `switch` perfectly acceptable? (Think: a factory at the edge of the system, or a closed set of values.)
3. S3: State the Liskov contract of `Bird.fly()` in terms of pre/post-conditions. Which rule does `Penguin` break?
   Give a second example with `Rectangle`/`Square`.
4. S4: How are ISP and SRP related? How are they different?
5. S5: Who should *own* the `OrderRepository` interface, the service or the database module? Why does it matter?
6. Which two principles interacted most in your solutions? Show where.

---

## Part 2: Code smells (`smells/`)

| # | File | Smell (a-l) | Your job (see the file header for details) |
|---|------|-------------|--------------------------------------------|
| 01 | `Smell01_DuplicateCode` | a. Duplicate code | Remove the three near-identical bonus calculations |
| 02 | `Smell02_LongMethod` | b. Long method | Break `summarize()` into well-named pieces |
| 03 | `Smell03_LargeClass` | c. Large class | Split the god-class `UserAccount` |
| 04 | `Smell04_DivergentChange` | d. Divergent change | Separate parsing, pricing and rendering; explain how this differs from (c) |
| 05 | `Smell05_ShotgunSurgery` | e. Shotgun surgery | Make a currency-format change a one-place edit; explain the contrast with (d) |
| 06 | `Smell06_LongParameterList` | f. Long parameter list | Reduce 9-parameter methods to <= 3 |
| 07 | `Smell07_FeatureEnvy` | g. Feature envy | Move behaviour to where the data lives |
| 08 | `Smell08_DataClass` | h. Data class | Give `BankAccount` real behaviour and remove its setters |
| 09 | `Smell09_DataClumps` | i. Data clumps | Introduce a type for the `(lat, lon)` clump |
| 10 | `Smell10_PrimitiveObsession` | j. Primitive obsession | Introduce `Email`, `PhoneNumber`, `Money`, `Tier` |
| 11 | `Smell11_SwitchStatements` | k. Switch statements | Replace the repeated switches with polymorphism |
| 12 | `Smell12_SpeculativeGenerality` | l. Speculative generality | Delete the unused flexibility |

### Smell worksheet (fill in **in your own words** before looking at any reference)

| Smell | Definition (1 sentence) | One symptom you can *see* in code | Why is it harmful? | Refactoring(s) that fix it | Which exercise showed it? |
|-------|-------------------------|-----------------------------------|--------------------|----------------------------|---------------------------|
| a. Duplicate code | | | | | |
| b. Long method | | | | | |
| c. Large class | | | | | |
| d. Divergent change | | | | | |
| e. Shotgun surgery | | | | | |
| f. Long parameter list | | | | | |
| g. Feature envy | | | | | |
| h. Data class | | | | | |
| i. Data clumps | | | | | |
| j. Primitive obsession | | | | | |
| k. Switch statements | | | | | |
| l. Speculative generality | | | | | |

### Smell reflection questions
1. **Distinguish:** (a) Large Class vs. Divergent Change, (b) Divergent Change vs. Shotgun Surgery,
   (c) Data Class vs. Feature Envy, (d) Data Clumps vs. Primitive Obsession.
2. Which smell(s) map to which SOLID principle? Fill in: Switch Statements -> ___, Large Class -> ___,
   Divergent Change -> ___, Speculative Generality -> ___ (hint: not every smell violates a SOLID principle!).
3. Pick one smell and argue a case where leaving it in is the *right* call.
4. Duplicate code: is *all* duplication bad? (Consider two things that look the same but change for different reasons.)
5. Refactoring needs a safety net. What was yours here, and what would you use in a real project?

---

## Part 3: Critique a design (`smells/ReviewMe.java` + `REVIEW_TEMPLATE.md`)

Act as the reviewer of a pull request. Read `ReviewMe.java` (no refactoring tools!) and complete
`REVIEW_TEMPLATE.md`: findings table (>= 10 findings, each with smell, evidence, harm, proposed refactoring,
and SOLID principle if relevant), your top-3 priorities, a redesign sketch, and one "argue both sides" item.

---

## Self-check checklist
- [ ] `SolidDemo`: all five behaviours `[PASS]` and all structures `[DONE]`
- [ ] `SmellsDemo`: all twelve behaviours `[PASS]` and all structures `[DONE]`
- [ ] Every exercise has a written diagnosis and the name of the refactoring used
- [ ] Worksheet and reflection questions completed
- [ ] `REVIEW_TEMPLATE.md` completed

## Suggested grading
SOLID exercises 30 pts, smell exercises 36, worksheet 12, review capstone 16, reflection 6 (details in `solutions/ANSWER_KEY.md`).
