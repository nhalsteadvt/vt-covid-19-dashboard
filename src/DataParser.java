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
    private static ArrayList<String> rawDataOrdered;

    public static void main(String[] args) {
        File dash = null;
        dataCollection = new ArrayList<>();
        rawDataOrdered = new ArrayList<>();

        try {
            dash = new File("Data\\dashboard.txt");
            if (!dash.exists()) {
                throw new FileNotFoundException(args[0]);
            }
        }
        catch (FileNotFoundException err) {
            System.out.println(err.getMessage());
        }

    }


    public static void parseFile(File dash) throws IOException {
        RandomAccessFile dashReader = new RandomAccessFile(dash, "r");
        int numLines = lineCounter(dash);

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

        return numLines - 1;
    }

}
