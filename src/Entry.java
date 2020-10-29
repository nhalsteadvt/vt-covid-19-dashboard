/**
 * 
 */

/**
 * @author nhalstead
 *         The Entry class is used for storing a single point of data (one day)
 */
public class Entry {
    private int day;
    private int tests;
    private int positive;

    /**
     * 
     * @param day
     *            how many days since the earliest entry
     *            (starting at 0)
     * @param tests
     *            how many tests were performed that day
     * @param positive
     *            how many of those tests were positive for Covid-19
     */
    public Entry(int day, int tests, int positive) {
        this.day = day;
        this.tests = tests;
        this.positive = positive;
    }


    /**
     * Gets the day of Entry
     * 
     * @return int day
     */
    public int getDay() {
        return day;
    }


    /**
     * Gets the tests of Entry
     * 
     * @return int tests
     */
    public int getTests() {
        return tests;
    }


    /**
     * Gets the positive of Entry
     * 
     * @return int positive
     */
    public int getPositive() {
        return positive;
    }


    /**
     * This method calculates the percentage positive out of the total tests.
     * If the denominator (tests performed) is zero, zero is returned.
     * 
     * @return double representing the percentage positive
     */
    public double calcPercentage() {
        if (tests == 0) {
            return 0;
        }
        return ((double)positive) / tests;
    }


    /**
     * Overrides the default toString() method to represent all stored data
     * Includes the percentage positive to 4 decimal points
     */
    @Override
    public String toString() {
        String ans = String.format("%d: %d/%d = %.4f%s", day, positive, tests,
            100 * calcPercentage(), "%");
        return ans;
    }

}
