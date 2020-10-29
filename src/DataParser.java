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
 *
 */
public class DataParser {

    private static ArrayList<Entry> dataCollection;

    public static void main(String[] args) throws IOException {
        File dash = null;
        dataCollection = new ArrayList<>();

        try {
            dash = new File("Data\\dashboard.txt");
            if (!dash.exists()) {
                throw new FileNotFoundException(args[0]);
            }
        }
        catch (FileNotFoundException err) {
            System.out.println(err.getMessage());
        }

        parseFile(dash);
        for (int i = 0; i < dataCollection.size(); i++) {
            System.out.println(dataCollection.get(i).toString());
        }

    }


    public static void parseFile(File dash) throws IOException {
        RandomAccessFile dashReader = new RandomAccessFile(dash, "r");
        int numLines = lineCounter(dash);
        dashReader.readLine(); // reads first line with headings and no data
        int currLineNum = 1;
        int lastByte = 0;

        while (lastByte != -1) {
            lastByte = dashReader.read();
            String line = dashReader.readLine();
            if (line == null) {
                break;
            }
            // System.out.println(line);
            currLineNum++;
            int day = numLines - currLineNum;
            String noDate = line.substring(line.indexOf("\t") + 1);
            String noTests = noDate.substring(noDate.indexOf("\t") + 1);
            int tests = Integer.parseInt((noDate.substring(0, noDate.indexOf(
                "\t"))).replace(",", ""));
            int positive = Integer.parseInt((noTests.substring(0, noTests
                .indexOf("\t"))).replace(",", ""));
            Entry temp = new Entry(day, tests, positive);
            // System.out.println("temp: " + temp.toString());
            dataCollection.add(0, temp);

        }

        dashReader.close();

    }


    public static int lineCounter(File file) throws IOException {
        RandomAccessFile fileReader = new RandomAccessFile(file, "r");
        int numLines = 0;
        int lastByte = 0;

        while (lastByte != -1) {
            lastByte = fileReader.read();
            fileReader.readLine();
            numLines++;
        }

        fileReader.close();

        return numLines;
    }

}
