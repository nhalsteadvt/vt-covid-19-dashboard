/**
 * 
 */

/**
 * @author nhalstead
 *
 */
public class Entry {
    private int day;
    private int tests;
    private int positive;

    public Entry(int day, int tests, int positive) {
        this.day = day;
        this.tests = tests;
        this.positive = positive;
    }


    public int getDay() {
        return day;
    }


    public int getTests() {
        return tests;
    }


    public int getPositive() {
        return positive;
    }


    public double calcPercentage() {
        if (tests == 0) {
            return 0;
        }
        return ((double)positive) / tests;
    }


    public String toString() {
        String ans = String.format("%d: %d/%d = %.4f%s", day, positive, tests,
            100 * calcPercentage(), "%");
        return ans;
        // "" + day + ": " + positive + "/" + tests + " = "
        // + calcPercentage();
    }

}
