import java.util.ArrayList;

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
    private double movingPercentage;
    private double percentage;

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
    public Entry(int day, int tests, int positive, double movingPercentage) {
        this.day = day;
        this.tests = tests;
        this.positive = positive;
        this.movingPercentage = movingPercentage;
        this.percentage = calcPercentage();
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
     * Gets the 7-day moving positive percentage of Entry (and previous 6 days)
     * 
     * @return double 7-day moving percentage
     */
    public double getMovingPercentage() {
        return movingPercentage;
    }


    public double getPercentage() {
        return percentage;
    }


    /**
     * This method calculates the percentage positive out of the total tests.
     * If the denominator (tests performed) is zero, zero is returned.
     * 
     * @return double representing the percentage positive
     */
    private double calcPercentage() {
        if (tests == 0) {
            return 0;
        }
        return ((double)positive) / tests;
    }


    public double calcMovingAverage(ArrayList<Entry> entries) {
        int idx = -1;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i) == this) {
                idx = i;
            }
        }
        if (idx < 6) {
            return -1;
        }

        int pos = 0;
        int tst = 0;
        for (int i = idx; i >= idx - 6; i--) {
            pos += entries.get(i).getPositive();
            tst += entries.get(i).getTests();
        }

        if (tst == 0) {
            return -1;
        }
        return ((double)pos) / tst;
    }


    public static double findTotalInfected(ArrayList<Entry> entries) {
        double ans = 0;
        for (Entry entry : entries) {
            ans += entry.getPositive();
        }
        return ans;
    }


    /**
     * Overrides the default toString() method to represent all stored data
     * Includes the percentage positive to 4 decimal points
     */
    @Override
    public String toString() {
        String ans = String.format("%d: %d/%d = %.4f%s", day, positive, tests,
            100 * getPercentage(), "%");
        return ans;
    }

}
