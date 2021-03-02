/**
 * @author nhalstead
 *
 */
public class Runner {

    /**
     * Contains main method for project
     * 
     * @param args
     *            Unnecessary, but only one argument (file location) is
     *            permitted
     */
    public static void main(String[] args) {
        String[] backup = new String[] { "Data\\\\dashboard2021S.txt" };
        if (args.length != 1) {
            args = backup;
        }
        Visualizer.launch(Visualizer.class, args);
    }

}
