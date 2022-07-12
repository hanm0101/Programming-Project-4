import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import java.util.*;
import javafx.scene.layout.*;

/**
 * The GUI for the line graph which displays
 * the average price of a property per borough for the
 * minimum number of nights to stay.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class AvgPropertyChart extends Application
{
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series series = new XYChart.Series();
    private final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
    private static int to;
    private static int from;
    @Override
    public void start(Stage stage) throws Exception
    {
        //code from https://docs.oracle.com/javafx/2/charts/line-chart.htm used as guidance
        //defining the axes

        //creating the chart;
        lineChart.setTitle("Average Property Price Per Borough");
        xAxis.setLabel("Name of Borough");
        lineChart.setCreateSymbols(true); //display a dot for each x value
        sharedPaneDesign();
        series.setName("Average Price For The Minimum Number of Nights");

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene  = new Scene(lineChart,800,600);
        stage.setTitle("Line Graph");
        scene.getStylesheets().add("Design.css");
        stage.setScene(scene);
        stage.show();// Show the Stage (window)
    }

    /**
     * set the maximum to the passed in value
     * @param  to the value the max is set to
     */
    public static void setToValue(int max)
    {
        to = max;
    }

    /**
     * set the minimum to the passed in value
     * @param  from the value the min is set to
     */
    public static void setFromValue(int min)
    {
        from = min;
    }

    /**
     * Add the borough name and its average to the graph
     * @param  series the graph to add the data to
     */
    private void addData(XYChart.Series series)
    {
        //create an arraylist to store x values
        AvgPropertyLoader avgProp = new AvgPropertyLoader();
        avgProp.createMap(from, to);
        List<String> xNames = new ArrayList<>();
        Collections.addAll(xNames, "Enfield", "Westminster", "Hillingdon", "Havering",
            "Wandsworth", "Lewisham", "Tower Hamlets", "Hounslow", "Redbridge", "Southwark",
            "Camden", "Bromley", "Lambeth", "Kensington and Chelsea", "Islington", "Barnet",
            "Richmond upon Thames", "Kingston upon Thames", "Harrow", "Sutton", "Haringey",
            "Brent", "Bexley", "Hackney", "Greenwich", "Hammersmith and Fulham", "Waltham Forest",
            "Merton", "Croydon", "Newham", "Ealing", "City of London","Barking and Dagenham");

        for(int i=0; i<xNames.size(); i++){
            double avg = avgProp.getAvg(xNames.get(i), from, to); //get the average for the specific property
            series.getData().add(new XYChart.Data(xNames.get(i), avg));//add it to the graph
        }
    }

    /**
     *The shared code both the large and small
     *graph use
     */
    private void sharedPaneDesign()
    {
        lineChart.setId("lineChart");
        addData(series);
        lineChart.getData().add(series);
    }

    /**
     * Create the small graph for the stats
     * class
     * @return pane the pane containing the graph
     */
    public HBox getPane()
    {
        HBox pane = new HBox();
        lineChart.setCreateSymbols(false); //display a dot for each x value
        sharedPaneDesign();
        xAxis.setTickLabelsVisible(false);//remove X axis values from small diagram
        pane.getChildren().add(lineChart);
        pane.setHgrow(lineChart, Priority.ALWAYS);
        return pane;
    }

}