/**
 * SMELL a: DUPLICATE CODE
 *
 * TASK
 *  1. Highlight the duplicated lines and describe, in a comment, how the three methods differ.
 *  2. Remove the duplication (name the refactoring you used) without changing behaviour.
 *  3. Bonus: what happens if the rounding rule changes? How many places would you edit before/after?
 *
 * DONE WHEN: demo() output is unchanged AND the meter finds no duplicated 4-line blocks.
 */
public class Smell01_DuplicateCode {

    static double employeeBonus(double salary, int years) {
        double bonus = salary * 0.05;
        if (years > 5) {
            bonus += salary * 0.02;
        }
        if (years > 10) {
            bonus += salary * 0.03;
        }
        bonus = Math.round(bonus * 100) / 100.0;
        return bonus;
    }

    static double managerBonus(double salary, int years) {
        double bonus = salary * 0.10;
        if (years > 5) {
            bonus += salary * 0.02;
        }
        if (years > 10) {
            bonus += salary * 0.03;
        }
        bonus = Math.round(bonus * 100) / 100.0;
        return bonus;
    }

    static double directorBonus(double salary, int years) {
        double bonus = salary * 0.15;
        if (years > 5) {
            bonus += salary * 0.02;
        }
        if (years > 10) {
            bonus += salary * 0.03;
        }
        bonus = Math.round(bonus * 100) / 100.0;
        return bonus;
    }

    static String demo() {
        return employeeBonus(50000, 3) + "," + managerBonus(80000, 7) + "," + directorBonus(120000, 12);
    }
}
