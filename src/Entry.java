/**
 * @author nhalstead
 *         The Entry class is used for storing a single point of data (one day)
 */
public class Entry {
    private int day;
    private String date;
    private int tests;
    private int positive;
    private double movingPercentage;
    private double percentage;

    /**
     * 
     * @param day
     *            how many days since the earliest entry
     *            (starting at 0)
     * @param date
     *            the date represented as MM/DD
     * @param tests
     *            how many tests were performed that day
     * @param positive
     *            how many of those tests were positive for COVID-19
     * @param movingPercentage
     *            the listed 7 day moving average of percent positive tests
     */
    public Entry(
        int day,
        String date,
        int tests,
        int positive,
        double movingPercentage) {
        this.day = day;
        this.date = date;
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
     * Gets the date of the entry
     * 
     * @return String of date in MM/DD format
     */
    public String getDate() {
        return date;
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


    /**
     * Gets the daily percentage of positive cases divided by tests performed
     * Note: this may return erroneous percentages due to data backlogs
     * 
     * @return double percentage of daily positive tests over daily total tests
     */
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


    /**
     * Overrides the default toString() method to represent all stored data
     * Includes the percentage positive to 4 decimal points
     */
    @Override
    public String toString() {
        String ans = String.format("%d: %s | Daily Cases Over Tests %4d/%-4d",
            day, date, positive, tests); // 100 * getPercentage()
        return ans;
    }

}
