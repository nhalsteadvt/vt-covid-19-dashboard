import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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
    private Stage stage;
    private DataParser data;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Series<Number, Number> series;
    private LineChart<Number, Number> linechart;
    private Group root;
    private GridPane gridPane;
    private Button[] buttons;

    private boolean firstTimeSetUp;
    private boolean normalized;
    private Variable type = Variable.MOVING_AVG; // which variable type the
                                                 // graph
                                                 // is displaying
    private final int HUNDRED_PERCENT = 100; // Used for spacing Y axis
    private final double CHART_BUFFER_FACTOR = 1.2; // Factor by how much extra
                                                    // room is above Y axis
    private final int TICKS = 10; // Number of ticks on graph for X and Y axes

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        normalized = false;
        String file = this.getParameters().getUnnamed().get(0);
        data = new DataParser(file);

        System.out.println(data.toString());
        System.out.println("total infected: " + data.findTotalInfected());

        // Sets up the graph to display the moving average
        firstTimeSetUp = true;
        setUpMovingAverage(type);
        firstTimeSetUp = false;

        // Creating the line chart
        linechart = new LineChart<>(xAxis, yAxis);

        // Setting the data to Line chart
        linechart.getData().add(series);

        // Creating a Group object
        root = new Group(linechart);

        // Setting title to the Stage
        stage.setTitle("My Covid-19 Graph");

        // Creating a Grid Pane
        gridPane = new GridPane();

        // Setting size for the pane
        gridPane.setMinSize(600, 400);

        // Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        // Creating Buttons
        buttonSetUp();

        // add gridPane to root
        root.getChildren().add(gridPane);

        // Creating a scene object
        Scene scene = new Scene(root, 650, 400);

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }


    private void setUpMovingAverage(Variable type) {
        normalized = false;
        if (series != null) {
            series.getData().clear();
        }
        else {
            series = new Series<>();
        }

        // Defining the x axis
        int numEntries = data.getEntries().size();
        if (firstTimeSetUp) {
            xAxis = new NumberAxis(0, numEntries, numEntries / TICKS);
        }
        else {
            xAxis.setUpperBound(numEntries);
            xAxis.setTickUnit(numEntries / TICKS);
        }
        xAxis.setLabel("Days Since " + data.getEntries().get(0).getDate());

        // Defining the y axis
        double maxPercent = data.findMaxPercentage();
        int totalInfected = data.findTotalInfected();
        int totalTests = data.findTotalTests();
        int maxPositive = data.findMaxPositive();
        int maxTests = data.findMaxTests();

// System.out.println("type: " + type);
// System.out.println("first: " + firstTimeSetUp);

        if (firstTimeSetUp) {
            switch (type) {
                case TOTAL_POS:
                    yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR
                        * totalInfected, CHART_BUFFER_FACTOR * totalInfected
                            / TICKS);
                    break;
                case TOTAL_TESTS:
                    yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR
                        * totalInfected, CHART_BUFFER_FACTOR * totalTests
                            / TICKS);
                    break;
                case DAILY_POS:
                    yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR * maxPositive,
                        CHART_BUFFER_FACTOR * maxPositive / TICKS);
                    break;
                case DAILY_TESTS:
                    yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR * maxTests,
                        CHART_BUFFER_FACTOR * maxTests / TICKS);
                    break;
                default:
                    yAxis = new NumberAxis(0, CHART_BUFFER_FACTOR
                        * HUNDRED_PERCENT * maxPercent, CHART_BUFFER_FACTOR
                            * HUNDRED_PERCENT * maxPercent / TICKS);

            }
        }
        else {
            switch (type) {
                case TOTAL_POS:
                    yAxis.setUpperBound(CHART_BUFFER_FACTOR * totalInfected);
                    yAxis.setTickUnit(CHART_BUFFER_FACTOR * totalInfected
                        / TICKS);
                    break;
                case TOTAL_TESTS:
                    yAxis.setUpperBound(CHART_BUFFER_FACTOR * totalTests);
                    yAxis.setTickUnit(CHART_BUFFER_FACTOR * totalTests / TICKS);
                    break;
                case DAILY_POS:
                    yAxis.setUpperBound(CHART_BUFFER_FACTOR * maxPositive);
                    yAxis.setTickUnit(CHART_BUFFER_FACTOR * maxPositive
                        / TICKS);
                    break;
                case DAILY_TESTS:
                    yAxis.setUpperBound(CHART_BUFFER_FACTOR * maxTests);
                    yAxis.setTickUnit(CHART_BUFFER_FACTOR * maxTests / TICKS);
                    break;
                default:
                    yAxis.setUpperBound(CHART_BUFFER_FACTOR * HUNDRED_PERCENT
                        * maxPercent);
                    yAxis.setTickUnit(CHART_BUFFER_FACTOR * HUNDRED_PERCENT
                        * maxPercent / 10);
            }
        }

        switch (type) {
            default:
            case MOVING_AVG:
                yAxis.setLabel("Percent Positive (7 day moving average)");
                break;
            case MOVING_AVG_CALC:
                yAxis.setLabel("Percent Positive (7 day moving average)");
                break;
            case DAILY_POS:
                yAxis.setLabel("Positive Cases (daily)");
                break;
            case DAILY_TESTS:
                yAxis.setLabel("Tests (daily)");
                break;
            case TOTAL_POS:
                yAxis.setLabel("Total Positive");
                break;
            case TOTAL_TESTS:
                yAxis.setLabel("Total Tests");
                break;
        }

        // Prepare XYChart.Series object by setting name of data points
        switch (type) {
            case MOVING_AVG:
                series.setName("Given moving average");
                break;
            case MOVING_AVG_CALC:
                series.setName("Calculated moving average");
                break;
            case DAILY_POS:
                series.setName("Daily Cases");
                break;
            case DAILY_TESTS:
                series.setName("Daily Tests");
                break;
            case TOTAL_POS:
                series.setName("Total Positive Cases");
                break;
            case TOTAL_TESTS:
                series.setName("Total Tests");
                break;
            default:
                series.setName("Data Unknown");
                break;

        }

        // Adds the data points to the Series object
        for (Entry entry : data.getEntries()) {
            double value = -1;
            switch (type) {
                default:
                case MOVING_AVG:
                    value = HUNDRED_PERCENT * entry.getMovingPercentage();
                    break;
                case MOVING_AVG_CALC:
                    value = HUNDRED_PERCENT * data.calcMovingAverage(entry);
                    break;
                case DAILY_POS:
                    value = entry.getPositive();
                    break;
                case DAILY_TESTS:
                    value = entry.getTests();
                    break;
                case TOTAL_POS:
                    value = data.calcTotalCases(entry);
                    break;
                case TOTAL_TESTS:
                    value = data.calcTotalTests(entry);
                    break;

            }
            series.getData().add(new Data<Number, Number>(entry.getDay(),
                value));
        }

    }


    /**
     * Creates the button array and each button
     * Then adds button to gridpane
     */
    private void buttonSetUp() {
        buttons = new Button[7];

        buttons[0] = new Button("Normalize") {
            public void fire() {
                if (normalized != true) {
                    normalized = true;
                    normalizeData(series);
                }
            }
        };

        buttons[1] = new Button("Given Mov Avg") {
            public void fire() {
                if (type != Variable.MOVING_AVG) {
                    type = Variable.MOVING_AVG;
                    setUpMovingAverage(Variable.MOVING_AVG);
                }
            }
        };

        buttons[2] = new Button("Calc Mov Avg") {
            public void fire() {
                if (type != Variable.MOVING_AVG_CALC) {
                    type = Variable.MOVING_AVG_CALC;
                    setUpMovingAverage(Variable.MOVING_AVG_CALC);
                }
            }
        };

        buttons[3] = new Button("Daily Positive") {
            public void fire() {
                if (type != Variable.DAILY_POS) {
                    type = Variable.DAILY_POS;
                    setUpMovingAverage(Variable.DAILY_POS);
                }
            }
        };

        buttons[4] = new Button("Daily Tests") {
            public void fire() {
                if (type != Variable.DAILY_TESTS) {
                    type = Variable.DAILY_TESTS;
                    setUpMovingAverage(Variable.DAILY_TESTS);
                }
            }
        };

        buttons[5] = new Button("Total Positive") {
            public void fire() {
                if (type != Variable.TOTAL_POS) {
                    type = Variable.TOTAL_POS;
                    setUpMovingAverage(Variable.TOTAL_POS);
                }
            }
        };

        buttons[6] = new Button("Total Tests") {
            public void fire() {
                if (type != Variable.TOTAL_TESTS) {
                    type = Variable.TOTAL_TESTS;
                    setUpMovingAverage(Variable.TOTAL_TESTS);
                }
            }
        };

        // Arranging all the nodes in the grid
        gridPane.add(buttons[0], 48, 16);
        gridPane.add(buttons[1], 48, 0);
        gridPane.add(buttons[2], 48, 2);
        gridPane.add(buttons[3], 48, 4);
        gridPane.add(buttons[4], 48, 6);
        gridPane.add(buttons[5], 48, 8);
        gridPane.add(buttons[6], 48, 10);

    }


    private void normalizeData(Series<Number, Number> series) {
        for (int i = data.getEntries().size() - 1; i >= 0; i--) {
            double value = findValue(series.getData().get(i).toString());
            if (value <= 0 || (value > 100 && type == Variable.MOVING_AVG
                || type == Variable.MOVING_AVG_CALC)) {
                series.getData().remove(i);
            }
        }
        this.normalized = true;
    }


    /**
     * Finds the percentage (or value) from a point in a Series object toString
     * 
     * @param str
     *            input (the toString() of an Entry object)
     * @return percentage from the input
     */
    public static double findValue(String str) {
        int idx1 = str.indexOf(',') + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = idx1; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) || str.charAt(i) == '.' || str
                .charAt(i) == '-') {
                sb.append(str.charAt(i));
            }
            else {
                break;
            }
        }
        return Double.parseDouble(sb.toString());
    }
}
