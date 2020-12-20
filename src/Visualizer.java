import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author nhalstead
 *
 */
public class Visualizer extends Application {
    private DataParser data;
    private Axis<Number> xAxis;
    private Axis<Number> yAxis;
    private Series<Number, Number> series;

    private boolean normalized;
    private final int HUNDRED_PERCENT = 100; // Used for spacing Y axis
    private final double CHART_BUFFER_FACTOR = 1.2; // Factor by how much extra
                                                    // room is above Y axis
    private final int TICKS = 10; // Number of ticks on graph for X and Y axes

    @Override
    public void start(Stage stage) throws IOException {
        normalized = false;
        String file = this.getParameters().getUnnamed().get(0);
        data = new DataParser(file);

        System.out.println(data.toString());
        System.out.println("total infected: " + data.findTotalInfected());

        // Instantiates the Series object
        series = new Series<>();

        // Sets up the graph to display the moving average
        setUpMovingAverage();

        // Creating the line chart
        LineChart<Number, Number> linechart = new LineChart<>(xAxis, yAxis);

        // Setting the data to Line chart
        linechart.getData().add(series);

        // Creating a Group object
        Group root = new Group(linechart);

        // Setting title to the Stage
        stage.setTitle("My Covid-19 Graph");

        // Creating Buttons
        Button button1 = new Button("Normalize") {
            public void fire() {
                if (!normalized) {
                    normalizeData(series);
                }
            }
        };

        // Creating a Grid Pane
        GridPane gridPane = new GridPane();

        // Setting size for the pane
        gridPane.setMinSize(600, 400);

        // Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        // Arranging all the nodes in the grid
        gridPane.add(button1, 48, 0);

        // add gridPane to root
        root.getChildren().add(gridPane);

        // Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }


    private void setUpMovingAverage() {
        // Defining the x axis
        int numEntries = data.getEntries().size();
        xAxis = new NumberAxis(0, numEntries, numEntries / TICKS);
        xAxis.setLabel("Days Since 08/03/20");

        // Defining the y axis
        double maxPercent = data.findMaxPercentage();
        yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR * HUNDRED_PERCENT
            * maxPercent, CHART_BUFFER_FACTOR * HUNDRED_PERCENT * maxPercent
                / TICKS);
        yAxis.setLabel("Percent positive");

        // Prepare XYChart.Series object by setting name of data points
        series.setName("Daily percent positive");

        // Adds the data points to the Series object
        for (Entry entry : data.getEntries()) {
            double percent = data.calcMovingAverage(entry);
            series.getData().add(new Data<Number, Number>(entry.getDay(), 100
                * percent));
        }
    }


    private void normalizeData(Series<Number, Number> series) {
        for (int i = data.getEntries().size() - 1; i >= 0; i--) {
            double percent = findPercent(series.getData().get(i).toString());
            if (percent == 0 || percent > 100) {
                series.getData().remove(i);
            }
        }
        this.normalized = true;
    }


    public static double findPercent(String str) {
        int idx1 = str.indexOf(',') + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = idx1; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) || str.charAt(i) == '.') {
                sb.append(str.charAt(i));
            }
            else {
                break;
            }
        }
        return Double.parseDouble(sb.toString());
    }
}
