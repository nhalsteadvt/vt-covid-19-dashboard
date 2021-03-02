import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author nhalstead
 *         The DataParser class deals with parsing the given file dashboard.txt
 *         from the Data folder to create Entry objects and place them into an
 *         ArrayList organized from least to most recent
 *
 */
public class DataParser {

    private ArrayList<Entry> dataCollection; // contains Entry objects
    private File dash;

    /**
     * DataParser object is used to parse a file to collect relevant data into
     * Entry objects
     * 
     * @param file
     *            String with assumed valid file location
     * @throws IOException
     *             Thrown if the file is not found
     */
    public DataParser(String file) throws IOException {
        this.dash = null;
        dataCollection = new ArrayList<>();

        // this block ensures the file exists where it is expected
        try {
            dash = new File(file);
            if (!dash.exists()) {
                throw new FileNotFoundException(file);
            }
        }
        catch (FileNotFoundException err) {
            System.out.println(err.getMessage());
        }

        // call to method to add Entry objects to the dataCollection object
        parseFile(dash);
    }


    /**
     * Gets the ArrayList of Entry objects
     * 
     * @return ArrayList<Entry> dataCollection
     */
    public ArrayList<Entry> getEntries() {
        return dataCollection;
    }


    /**
     * Finds the maximum percentage in the list of Entry objects
     * Excludes entries above 100%
     * 
     * @return double representing the maximum percentage
     */
    public double findMaxPercentage() {
        double ans = -1;
        for (Entry entry : dataCollection) {
            double percent = entry.getPercentage();
            if (percent > ans && percent <= 1) {
                ans = entry.getPercentage();
            }
        }
        return ans;
    }


    public double findMaxMovingPercentage() {
        double ans = -1;
        for (Entry entry : dataCollection) {
            double percent = entry.getMovingPercentage();
            if (percent > ans && percent <= 1) {
                ans = entry.getMovingPercentage();
            }
        }
        return ans;
    }


    /**
     * Finds the maximum daily infected in the list of Entry objects
     * 
     * @return int representing the maximum daily positive number of cases
     */
    public int findMaxPositive() {
        int ans = -1;
        for (Entry entry : dataCollection) {
            double positive = entry.getPositive();
            if (positive > ans) {
                ans = entry.getPositive();
            }
        }
        return ans;
    }


    /**
     * Finds the maximum daily tests in the list of Entry objects
     * 
     * @return int representing the maximum daily number of tests
     */
    public int findMaxTests() {
        int ans = -1;
        for (Entry entry : dataCollection) {
            double tests = entry.getTests();
            if (tests > ans) {
                ans = entry.getTests();
            }
        }
        return ans;
    }


////// DESCRIPTION TODO
    public double calcMovingAverage(Entry entry) {
        int idx = -1;
        for (int i = 0; i < dataCollection.size(); i++) {
            if (dataCollection.get(i) == entry) {
                idx = i;
            }
        }
        if (idx == -1) {
            return -1;
        }
        if (idx < 1) {
            return entry.getPercentage();
        }

        int pos = 0;
        int tst = 0;
        for (int i = idx; i >= idx - 6; i--) {
            if (i < 0) {
                break;
            }
            pos += dataCollection.get(i).getPositive();
            tst += dataCollection.get(i).getTests();
        }

        if (tst == 0) {
            return -1;
        }
        return ((double)pos) / tst;
    }


    /**
     * Calculates the total number of positive cases up to a certain entry in
     * the dataset
     * 
     * @param entry
     *            the stopping date in calculating the total
     * @return int representing the total number of cases up until entry
     */
    public int calcTotalCases(Entry entry) {
        int idx = -1;
        for (int i = 0; i < dataCollection.size(); i++) {
            if (dataCollection.get(i) == entry) {
                idx = i;
            }
        }
        if (idx == -1) {
            return -1;
        }

        int pos = 0;
        for (int i = idx; i >= 0; i--) {
            pos += dataCollection.get(i).getPositive();
        }

        return pos;
    }


    /**
     * Calculates the total number of tests up to a certain entry in
     * the dataset
     * 
     * @param entry
     *            the stopping date in calculating the total
     * @return int representing the total number of tests up until entry
     */
    public int calcTotalTests(Entry entry) {
        int idx = -1;
        for (int i = 0; i < dataCollection.size(); i++) {
            if (dataCollection.get(i) == entry) {
                idx = i;
            }
        }
        if (idx == -1) {
            return -1;
        }

        int pos = 0;
        for (int i = idx; i >= 0; i--) {
            pos += dataCollection.get(i).getTests();
        }

        return pos;
    }


    /**
     * Finds the total number of positive cases by adding up positive number
     * from each entry
     * 
     * @return int representing total infected in dataset
     */
    public int findTotalInfected() {
        int ans = 0;
        for (Entry entry : dataCollection) {
            ans += entry.getPositive();
        }
        return ans;
    }


    /**
     * Finds the total number of tests by adding up daily testing number
     * from each entry
     * 
     * @return int representing total tests in dataset
     */
    public int findTotalTests() {
        int ans = 0;
        for (Entry entry : dataCollection) {
            ans += entry.getTests();
        }
        return ans;
    }


    /**
     * This method will parse a file and add entries to the dataCollection
     * object based on contents of the file
     * 
     * Precondition: The file is of correct format (which it will be unless
     * tampered with)
     * 
     * Postcondition: The file is left unaltered; there are no data leaks
     * 
     * @param dash
     *            The File object that will be parsed for entries
     * @throws IOException
     *             Thrown if the File is not found
     */
    private void parseFile(File dash) throws IOException {
        RandomAccessFile dashReader = new RandomAccessFile(dash, "r");
        int numLines = lineCounter(dash);
        dashReader.readLine(); // reads first line with headings and no data
        int currLineNum = 1;

        // This block parses the rest of the file with the data
        while (currLineNum < numLines) {
// dashReader.read();
// dashReader.seek(dashReader.getFilePointer() - 1);
            String line = dashReader.readLine();
// System.out.println(line);
            if (line == null) {
                break;
            }
            currLineNum++;
            int day = numLines - currLineNum;

            // the string of the entry after Date
            String noDate = line.substring(line.indexOf("\t") + 1);
            // the string of the entry after Tests
            String noTests = noDate.substring(noDate.indexOf("\t") + 1);
            // the string of the entry after Pos
            String noPos = noTests.substring(noTests.indexOf("\t") + 1); // unused
            // the string of the entry after 7d Mov Avg Pos
            String noMovAvgPos = noPos.substring(noPos.indexOf("\t") + 1);

            // gets the date from the .txt file and formats it to be MM/DD
            String date = line.substring(0, line.indexOf("\t"));
            if (date.indexOf('/') == 1) {
                date = "0" + date;
            }
            if (date.length() != 5) {
                date = date.substring(0, 3) + "0" + date.substring(3);
            }
            // gets the number of tests from the truncated line
            int tests = Integer.parseInt((noDate.substring(0, noDate.indexOf(
                "\t"))).replace(",", ""));
            // gets the number of positive tests from the truncated line
            int positive = Integer.parseInt((noTests.substring(0, noTests
                .indexOf("\t"))).replace(",", ""));
            // gets the 7 day moving average from the truncated line
            double moving = Double.parseDouble((noMovAvgPos.substring(0,
                noMovAvgPos.indexOf("%")))) / 100;

            // adds a new entry to dataCollection at the front (for order)
            Entry temp = new Entry(day, date, tests, positive, moving);
            dataCollection.add(0, temp);
        }

        dashReader.close();
    }


    /**
     * This method counts how many lines are in the file
     * 
     * @param file
     *            The input file
     * @return int for how many visible lines there are in the file
     * @throws IOException
     *             Thrown if the File is not found
     */
    private int lineCounter(File file) throws IOException {
        RandomAccessFile fileReader = new RandomAccessFile(file, "r");
        int numLines = 0;
        int lastByte = 0;

        // This block parses the file and increments numLines for each line
        while (lastByte != -1) {
            lastByte = fileReader.read();
            if (fileReader.readLine() == null) {
                break;
            }
            numLines++;
        }

        fileReader.close();
        return numLines;
    }


    /**
     * Overrides default toString() method to return all stored Entry objects
     * String builder is used for efficiency
     * Last char (deleted) is extra newline
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataCollection.size(); i++) {
            sb.append(dataCollection.get(i).toString());
            sb.append(" | Total Cases Over Tests " + String.format(
                "%4d/%-5d | 7Day Moving Avg %.4f%%", calcTotalCases(
                    dataCollection.get(i)), calcTotalTests(dataCollection.get(
                        i)), 100 * calcMovingAverage(dataCollection.get(i))));
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
