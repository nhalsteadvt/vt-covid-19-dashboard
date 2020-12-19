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
        int lastByte = 0;

        // This block parses the rest of the file with the data
        while (lastByte != -1) {
            lastByte = dashReader.read();
            String line = dashReader.readLine();
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

            int tests = Integer.parseInt((noDate.substring(0, noDate.indexOf(
                "\t"))).replace(",", ""));
            int positive = Integer.parseInt((noTests.substring(0, noTests
                .indexOf("\t"))).replace(",", ""));
            double moving = Double.parseDouble((noMovAvgPos.substring(0,
                noMovAvgPos.indexOf("\t"))).replace(",", ""));
            Entry temp = new Entry(day, tests, positive, moving);
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
            sb.append(" | " + dataCollection.get(i).calcMovingAverage(
                dataCollection));
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
