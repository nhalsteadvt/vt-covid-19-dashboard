import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author nhalstead
 *
 */
public class Visualizer extends Application {
    private DataParser data;

    @Override
    public void start(Stage stage) throws IOException {
        String file = this.getParameters().getUnnamed().get(0);
        data = new DataParser(file);
        System.out.println(data.toString());
        System.out.println(Entry.findMaxPercentage(data.getEntries()));

        // Defining the x axis
        int numEntries = data.getEntries().size();
        NumberAxis xAxis = new NumberAxis(0, numEntries, numEntries / 10);
        xAxis.setLabel("Days Since 08/03/20");

        // Defining the y axis
        double maxPercent = Entry.findMaxPercentage(data.getEntries());
        NumberAxis yAxis = new NumberAxis(0, 1.2 * 100 * maxPercent, 0.12 * 100
            * maxPercent);
        yAxis.setLabel("Percent positive");

        // Creating the line chart
        LineChart linechart = new LineChart(xAxis, yAxis);

        // Prepare XYChart.Series objects by setting data
        XYChart.Series series = new XYChart.Series();
        series.setName("Daily percent positive");

        for (Entry entry : data.getEntries()) {
            double percent = entry.calcPercentage();
            if (percent <= 1) {
                series.getData().add(new XYChart.Data(entry.getDay(), 100
                    * percent));
            }
        }

        // Setting the data to Line chart
        linechart.getData().add(series);

        // Creating a Group object
        Group root = new Group(linechart);

        // Setting title to the Stage
        stage.setTitle("My Covid-19 Graph");

        // Creating Buttons
        Button button1 = new Button("Clear") {
            public void fire() {
                System.out.println("Goodbye");
                series.getData().clear();
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
        gridPane.add(button1, 45, 0);

        // add gridPane to root
        root.getChildren().add(gridPane);

        // Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }
}
