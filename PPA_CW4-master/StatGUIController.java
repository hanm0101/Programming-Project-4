import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.*;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.*;
import javafx.scene.Node;
import javafx.collections.*;
import javafx.geometry.Pos;
/**
 * StatGUIController is the controller class of the statistics GUI. 
 * It stores the behaviours of the GUI for statistics
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29-03-2019
 */
public class StatGUIController
{
    // instance variables - replace the example below with your own
    private static Integer to =6000;
    private static Integer from= 100;
    private boolean valid = false;
    private Alert alert = new Alert(AlertType.ERROR, "The values selected are invalid !");
    private Stats stats;
    private AvgPropertyChart avgChart = new AvgPropertyChart();
    private PropertyTypesBarChart propBarChart = new PropertyTypesBarChart();
    private static Input input;
    // connecting  instances to FXML file
    @FXML
    private GridPane gridPane = new GridPane();
    @FXML
    private Label upperLeftLabel = new Label();
    @FXML
    private Label upperRightLabel = new Label();
    @FXML
    private Label lowerLeftLabel = new Label();
    @FXML
    private Label lowerRightLabel = new Label();
    @FXML
    private ChoiceBox<Integer> fromCB= new ChoiceBox<>();
    @FXML
    private ChoiceBox<Integer> toCB= new ChoiceBox<>();
    @FXML
    private Button preBox1 = new Button();
    @FXML
    private Button nextBox1 = new Button();
    @FXML
    private Button preBox2 = new Button();
    @FXML
    private Button nextBox2 = new Button();
    @FXML
    private Button preBox3 = new Button();
    @FXML
    private Button nextBox3 = new Button();
    @FXML
    private Button preBox4 = new Button();
    @FXML
    private Button nextBox4 = new Button();
    @FXML
    private BorderPane lineChart = new BorderPane();
    @FXML
    private BorderPane barChart = new BorderPane();
    

    @FXML
    public void initialize(){
        stats = new Stats(input.getStatData());
        to = input.getToValue();
        from = input.getFromValue();
        //initialize the statistic values with price range considered
        stats.averageReviews(from, to); 
        stats.availableProperties(from, to);
        stats.homesAndAppartments(from, to);
        stats.mostExpensiveBorough(from, to);
        stats.findDistance(from, to);
        stats.mostFavouritedProperty(from, to);
        stats.bestBorough(from, to);
        stats.worstBorough(from, to);
        stats.addStat();
        stats.addValuesArray();
        stats.addFirstFour();
        stats.disjoinAL();
        //initialize the four first values in GUI 
        upperLeftLabel.setText(stats.returnStatLabel(0));
        stats.removeAvailable(stats.returnStatLabel(0)); //remove from available to store the old value
        upperRightLabel.setText(stats.returnStatLabel(1));
        stats.removeAvailable(stats.returnStatLabel(1));//remove from available to store the old value
        lowerLeftLabel.setText(stats.returnStatLabel(2));
        stats.removeAvailable(stats.returnStatLabel(2)); //remove from available to store the old value
        lowerRightLabel.setText(stats.returnStatLabel(3));
        stats.removeAvailable(stats.returnStatLabel(3)); //remove from available to store the old value

        addValuesTo(); //add values to choice box
        addValuesFrom();
        toCB.setValue(to); //set the value to choice box
        fromCB.setValue(from);
        
        ObservableList<Node> gridChildren = gridPane.getChildren(); //get children of the gridpane
        
        avgChart.setFromValue(from); //pass from to values
        avgChart.setToValue(to);
        HBox linePane = avgChart.getPane(); //put hbox in the grid
        Tooltip tooltip = new Tooltip("Click on the graph to expand it"); //instruction given
        Tooltip.install(linePane, tooltip);
        linePane.setOnMouseClicked(e-> viewGraph()); //mouse event
        lineChart.setCenter(linePane);

        propBarChart.setFromValue(from);//pass from to values
        propBarChart.setToValue(to);
        HBox barPane = propBarChart.getPane();//put hbox in the grid
        Tooltip.install(barPane, tooltip); //instruction given
        barPane.setOnMouseClicked(e-> viewBarChart()); //mouse event
        barChart.setCenter(barPane);

        // skip first two elements because they belong to row 0
        for (int i= 0; i<= gridChildren.size() - 3; i++){ //find the stat boxes
            BorderPane statBox = (BorderPane) gridChildren.get(i); //get borderpane
            ObservableList<Node> statBoxElements = statBox.getChildren(); //get elements in the panes
            Label statLabel = (Label) statBoxElements.get(2); //find the label

            Button leftB = (Button) statBoxElements.get(0); // find the left button
            leftB.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stats.addAvailable(statLabel.getText()); //add to available (new value) to the back
                        statLabel.setText(stats.getAvailable(0)); //show the first value from the list
                        stats.removeAvailable(stats.getAvailable(0)); //remove the first value 
                    }
                });

            Button rightB = (Button) statBoxElements.get(1); // find the right button
            rightB.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stats.addToFirstPositionAvailable(statLabel.getText()); //add to available in the front(new value)
                        statLabel.setText(stats.getAvailable(3)); //show the last value 
                        stats.removeAvailable(stats.getAvailable(3)); //remove the last value
                    }
                });
        }
    }

    /**
     * Add values to the choice box to
     */
    @FXML
    private void addValuesTo(){
        toCB.getItems().addAll(0, 100, 250, 500, 1000, 2000, 3000, 4000, 5000, 6000);
    }

    /**
     * Add values to the choice box from
     */
    @FXML
    private void addValuesFrom(){
        fromCB.getItems().addAll(0, 100, 250, 500, 1000, 2000, 3000, 4000, 5000, 6000);
    }

    /**
     * Listen to the changes for the choice box to
     */
    private void listenTo(){
        //Listens to changes to the "to" dropBox selected value.
        toCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    to = toCB.getItems().get((Integer) new_value);
                    if(to !=null && from !=null && from > to){
                        Optional<ButtonType> result = alert.showAndWait();
                        valid = false;
                    }
                    else if(to !=null && from !=null) valid = true;
                }
            }  );
    }
    
    /**
     * Listen to the changes for the choice box from
     */
    private void listenFrom(){
        //Listens to changes to the "from" dropBox selected value.
        fromCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    to = toCB.getItems().get((Integer) new_value);
                    if(to !=null && from !=null && from > to){
                        Optional<ButtonType> result = alert.showAndWait();
                        valid = false;
                    }
                    else if(to !=null && from !=null) valid = true;
                }
            }  );
    }
    
    /**
     * Set the To value for price range
     * @param  toMain value from login window
     */
    public static void setToValue(int toMain)
    {
        to = toMain;
    }

    /**
     * Set the From value for price range
     *
     * @param  fromMAin value from login window
     */
    public static void setFromValue(int fromMain)
    {
        from = fromMain;
    }

    /**
     * Show the line chart in the grid with passed on to, from values
     */
    public void viewGraph()
    {
        try{
            AvgPropertyChart.setFromValue(from);
            AvgPropertyChart.setToValue(to);
            new AvgPropertyChart().start(new Stage());
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Show the bar chart in the grid with passed on to, from values
     */
    public void viewBarChart()
    {
        try{
            propBarChart.setFromValue(from);
            propBarChart.setToValue(to);
            new PropertyTypesBarChart().start(new Stage());
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void setInput(Input newInput){
        input = newInput;
    }
}

