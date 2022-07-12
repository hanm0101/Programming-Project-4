import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.beans.value.*;
import javafx.scene.control.*;
import java.util.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
/**
 * Write a description of class NotLoggedWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class NotLoggedWindow extends Panel
{
    private static Stage stage;
    GridPane MiddlePane = new GridPane();
    private List<AirbnbListing> recommendedProp = new ArrayList<>();
    private DataAccount dataAccount;
    private ArrayList<AirbnbListing> listing;
    private ArrayList<AirbnbListing> favProps;
    private AirbnbDataLoader dataLoader;
    private Recommendations recommendations = new Recommendations();
    private Scene scene;
    private Input input;

    /**
     * Constructor for objects of class NotLoggedWindow
     */
    public NotLoggedWindow(Input input){
        this.input = input;
        this.dataAccount = input.getDataAccount();
        initial();
    }
    
    public Scene getScene(){
        return scene;
    }
    
    public void initial()
    {
        this.stage = stage;
        dataLoader = new AirbnbDataLoader();
        listing = dataLoader.load();
        // Create a Button or any control item
        Label fromLabel = new Label("From:");
        Label toLabel = new Label("To:");

        // Create the root pane 
        GridPane pane = new GridPane();
        pane.setMinSize(733,606);

        // Create the top pane 
        TilePane TopPane = new TilePane();
        TopPane.setMinSize(733,40);
        TopPane.getStyleClass().add("pane");
        TopPane.setPadding(new Insets(1, 5, 1, 1));
        Label title = new Label("Features  Page");
        title.setId("Maintitle");
        TopPane.getChildren().add(title);

        // Create the bottom pane 
        GridPane BOTTOMpane = new GridPane();
        BOTTOMpane.setMinSize(733, 33);
        BOTTOMpane.getStyleClass().add("pane");
        BOTTOMpane.setPadding(new Insets(0,10,0,10));

        // Sets the size of the middle pane 
        createMiddlePane();
        
        //Create a spacer pane
        Pane spacer = new Pane();
        spacer.setMinSize(565, 33);

        // Add the secondary panes into the main pane
        pane.add(TopPane, 0, 0);
        pane.add(MiddlePane, 0,1);
        pane.add(BOTTOMpane, 0, 2);

        //Create the next and forward buttons
        Button nextButton = new Button(">>>");
        nextButton.setId("forwardButton");
        Button backButton = new Button("<<<");
        backButton.setId("backButton");
        backButton.setMinSize(70, 10);
        nextButton.setMinSize(70, 10);

        //Add the buttons to the bottom pane.
        BOTTOMpane.add(backButton, 0, 0);
        BOTTOMpane.add(spacer, 1, 0);
        BOTTOMpane.add(nextButton, 2, 0);

        nextButton.setOnAction(this::nextPanel);
        backButton.setOnAction(this::previousPanel);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        scene = new Scene(pane, 733,606);
        scene.getStylesheets().add("Design.css");
    }
    
    private void createMiddlePane(){
        MiddlePane.setMinSize(733, 533);
        MiddlePane.getStyleClass().add("pane");
        MiddlePane.setAlignment(Pos.CENTER);
        Label instructions1 = new Label("If you want to see something here, you should login !");
        Label instructions2 = new Label("Click on next to go back to the login panel !");
        MiddlePane.add(instructions1, 0,0);
        MiddlePane.add(instructions2, 0,1);
    }
}