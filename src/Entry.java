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

}
