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

    private static ArrayList<Entry> dataCollection; // this contains all of the
                                                    // Entry objects

    public static void main(String[] args) throws IOException {
        File dash = null;
        dataCollection = new ArrayList<>();

        // this block ensures the file exists where it is expected
        try {
            dash = new File("Data\\dashboard.txt");
            if (!dash.exists()) {
                throw new FileNotFoundException(args[0]);
            }
        }
        catch (FileNotFoundException err) {
            System.out.println(err.getMessage());
        }

        // call to method to add Entry objects to the dataCollection object
        parseFile(dash);

        // this block is for debugging purposes
        for (int i = 0; i < dataCollection.size(); i++) {
            System.out.println(dataCollection.get(i).toString());
        }

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
     * @throws IOException
     */
    public static void parseFile(File dash) throws IOException {
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
            String noDate = line.substring(line.indexOf("\t") + 1);
            String noTests = noDate.substring(noDate.indexOf("\t") + 1);
            int tests = Integer.parseInt((noDate.substring(0, noDate.indexOf(
                "\t"))).replace(",", ""));
            int positive = Integer.parseInt((noTests.substring(0, noTests
                .indexOf("\t"))).replace(",", ""));
            Entry temp = new Entry(day, tests, positive);
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
     */
    public static int lineCounter(File file) throws IOException {
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
        System.out.println(numLines);
        return numLines;
    }

}
