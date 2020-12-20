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

        // Sets up the graph to display the moving average
        setUpMovingAverage(Variable.MOVING_AVG);

        // Creating the line chart
        LineChart<Number, Number> linechart = new LineChart<>(xAxis, yAxis);

        // Setting the data to Line chart
        linechart.getData().add(series);

        // Creating a Group object
        Group root = new Group(linechart);

        // Setting title to the Stage
        stage.setTitle("My Covid-19 Graph: 7 Day Moving Average");

        // Creating Buttons
        Button button1 = new Button("Calculate") {
            public void fire() {
                if (true) {
                    setUpMovingAverage(Variable.MOVING_AVG_CALC);
                    // normalizeData(series);
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


    private void setUpMovingAverage(Variable type) {
        if (series != null) {
            series.getData().clear();
        }
        else {
            series = new Series<>();
        }

        // Defining the x axis
        int numEntries = data.getEntries().size();
        xAxis = new NumberAxis(0, numEntries, numEntries / TICKS);
        xAxis.setLabel("Days Since " + data.getEntries().get(0).getDate());

        // Defining the y axis
        double maxPercent = data.findMaxPercentage();
        yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR * HUNDRED_PERCENT
            * maxPercent, CHART_BUFFER_FACTOR * HUNDRED_PERCENT * maxPercent
                / TICKS);

        switch (type) {
            case MOVING_AVG:
                yAxis.setLabel("Percent Positive (7 day moving average)");
                break;
            case MOVING_AVG_CALC:
                yAxis.setLabel("Percent Positive (7 day moving average)");
                break;
            case DAILY_PERCENTAGE:
                yAxis.setLabel("Percent Positive (daily)");
                break;
        }

        // Prepare XYChart.Series object by setting name of data points
        switch (type) {
            case MOVING_AVG:
                series.setName("Given moving average");
                break;
            case MOVING_AVG_CALC:
                series.setName("Calculated moving average)");
                break;
            case DAILY_PERCENTAGE:
                series.setName("Calculated daily)");
                break;
        }

        // Adds the data points to the Series object
        for (Entry entry : data.getEntries()) {
            double percent = -1;
            switch (type) {
                case MOVING_AVG:
                    percent = entry.getMovingPercentage();
                    break;
                case MOVING_AVG_CALC:
                    percent = data.calcMovingAverage(entry);
                    break;
                case DAILY_PERCENTAGE:
                    percent = entry.getPercentage();
                    break;
            }
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


    /**
     * DEPRECATED?
     * 
     * @param str
     *            input (the toString() of an Entry object)
     * @return percentage from the input
     */
    public static double findPercent(String str) {
        System.out.println(str);
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
