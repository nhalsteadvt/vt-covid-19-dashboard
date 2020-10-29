import java.io.IOException;

/**
 * 
 */

/**
 * @author nhalstead
 *
 */
public class Runner {

    /**
     * Contains main method for project
     * 
     * @param args
     *            Unused; future usage is considered
     * @throws IOException
     *             Thrown if the File is not found
     */
    public static void main(String[] args) throws IOException {
        DataParser parser = new DataParser("Data\\dashboard.txt");
        System.out.println(parser.toString());
    }

}
