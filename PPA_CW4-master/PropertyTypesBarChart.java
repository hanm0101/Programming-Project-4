import java.util.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;

/**
 * Displays the number of each room type in each borough
 * in the form of a stacked bar chart
 * y axis - number of properties
 * x axis - borough
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19
 */
public class PropertyTypesBarChart extends Application
{
    private PropertyTypesBarChartLoader loader = new PropertyTypesBarChartLoader();
    //define the x axis
    private CategoryAxis xAxis = new CategoryAxis();
    //define y axis
    private NumberAxis yAxis = new NumberAxis();
    //create the stacked bar chart
    private StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);
    @Override
    public void start(Stage stage) throws Exception
    {
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(Resources.ALL_BOROUGHS)));
        xAxis.setLabel("Boroughs");
        xAxis.setTickLabelRotation(90.0);
        yAxis.setLabel("number of room type");
        sbc.setTitle("number of types of room in each borough");

        sharedPaneDesign();

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(sbc, 800,500);
        stage.setTitle("Bar Chart");
        stage.setScene(scene);
        scene.getStylesheets().add("Design.css");
        // Show the Stage (window)
        stage.show();
    }

    /**
     * Set the max value
     */
    public void setToValue(int max){
        loader.setToValue(max);
    }

    /**
     * Set the min value
     */
    public void setFromValue(int min){
        loader.setToValue(min);
    }

    /**
     * The shared code for both
     * the small and large bar chart
     */
    private void sharedPaneDesign()
    {
        sbc.setId("sbc");
        //prepare xychart.series objects by setting data taken from
        //https://www.tutorialspoint.com/javafx/stacked_bar_chart.htm
        XYChart.Series<String, Number> privateRoomSeries = new XYChart.Series<>();
        privateRoomSeries.setName(Resources.PRIVATE_ROOM);

        XYChart.Series<String, Number> entireHomeSeries = new XYChart.Series<>();
        entireHomeSeries.setName(Resources.ENTIRE_HOME);

        XYChart.Series<String, Number> sharedRoomSeries = new XYChart.Series<>();
        sharedRoomSeries.setName(Resources.SHARED_ROOM);

        for(Map.Entry<String,Integer> entry : loader.getPrivateRoomCount().entrySet()){
            privateRoomSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        // for shared rooms
        for(Map.Entry<String,Integer> entry : loader.getSharedRoomCount().entrySet()){
            sharedRoomSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        //for entire homes/apt
        for(Map.Entry<String,Integer> entry : loader.getEntireHomeCount().entrySet()){
            entireHomeSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        sbc.getData().addAll(privateRoomSeries, entireHomeSeries, sharedRoomSeries);
    }

    /**
     * This returns the smaller
     * version of the bar chart for the statGUI
     * class
     * @return  pane the pane containing the chart
     */
    public HBox getPane()
    {
        HBox pane = new HBox();
        sharedPaneDesign();
        xAxis.setTickLabelsVisible(false);
        pane.getChildren().add(sbc);
        sbc.setCategoryGap(0);
        pane.setHgrow(sbc, Priority.ALWAYS);
        return pane;
    }

}