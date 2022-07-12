import java.util.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.text.TextAlignment;

/**
 * Displays the information of a selected property in the chosen borough 
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19
 */
public class IndivPropertyWindow extends Application
{
    private AirbnbListing current;
    private Stage stage;
    private BorderPane root;
    private GridPane bottomPane = new GridPane();
    private AnchorPane content;
    private static IndivPropertyEngine engine;
    private CheckMenuItem toggleFavProperty = new CheckMenuItem("Set favourite property");
    
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        content = setContent();
        
        //create the menubar
        MenuBar menubar = makeMenuBar();
        
        createBottomPane();
        // Create the root pane
        root = new BorderPane();
        root.setMinSize(650, 300);

        root.setCenter(content);
        root.setBottom(bottomPane);
        root.setTop(menubar);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(root, 650,300);
        scene.getStylesheets().add("Design.css");
        stage.setTitle(current.getName()+" in " + current.getNeighbourhood() );
        stage.setScene(scene);
        stage.setResizable(false);
        // Show the Stage (window)
        stage.show();
    }

    /**
     * create the pane with the buttons that allow you to view next and previous properties 
     */
    private void createBottomPane()
    {
        bottomPane.setMinSize(650, 33);
        bottomPane.getStyleClass().add("pane");
        bottomPane.setPadding(new Insets(0,10,0,10));
        //Create the next and forward buttons
        Button nextButton = new Button(">>>");
        nextButton.setId("forwardButton");
        Button backButton = new Button("<<<");
        backButton.setId("backButton");
        backButton.setMinSize(70, 10);
        nextButton.setMinSize(70, 10);

        //Create a spacer pane
        Pane spacer = new Pane();
        spacer.setMinSize(489, 33);

        //Add the buttons to the bottom pane.
        bottomPane.add(backButton, 0, 0);
        bottomPane.add(spacer, 1, 0);
        bottomPane.add(nextButton, 2, 0);

        //next and back buttons respond to clicks
        nextButton.setOnAction(e-> changePanel(true));
        backButton.setOnAction(e-> changePanel(false));
    }
    
    /**
     * view either the next or the previous property in the borough.
     * @param getNextPanel true to view next property, false to view the previous 
     */
    private void changePanel(boolean getNextPanel){
        //change the current property to the new property
        if(getNextPanel){
            current = engine.getNextProperty();
            toggleFavProperty.setSelected(engine.isFavoriteProperty());
        }
        else{
            current = engine.getPrevProperty();
            toggleFavProperty.setSelected(engine.isFavoriteProperty());
        }
        //update the vbox in the root layout node
        root.getChildren().remove(content);
        content = setContent();
        root.setCenter(content);
        stage.setTitle(current.getName()+" in " + current.getNeighbourhood() );
    }
    
    /**
     *  load in which property the user wants to view and loads in the list of properties in the chosen borough 
     *  initialises the individual property engine here
     *  @param List<AirbnbListing> properties in the chosen borough
     *  @param AirbnbListing the property you want to view individually 
     */
    public void loadProperties(List<AirbnbListing> boroughListings, AirbnbListing currentListing){
        engine = new IndivPropertyEngine(boroughListings, currentListing);
        current = currentListing;
    }
    
    /**
     * makes the menu bar containing the menu 'Options' which gives the option to set the current property as a favourite
     */
    private MenuBar makeMenuBar()
    {
        MenuBar menuBar = new MenuBar();
        Menu optionsMenu = new Menu("Options");
        menuBar.setId("menuBar");
        //only if a user is logged in can this menu item be seen <-
        toggleFavProperty.setSelected(engine.isFavoriteProperty());
        //^^^^^^^^^^^
        toggleFavProperty.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        String message = "";
                        if(engine.toggleFavouriteProp()){ //if the property is now a favourite 
                            message = current.getName() + " has been set as your favourite property";
                        }
                        else {
                            message = current.getName() + " is no longer your favourite property";
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(current.getName()+" in " + current.getNeighbourhood());
                        alert.setHeaderText(message);
                        alert.showAndWait();
                    }
                });
        optionsMenu.getItems().addAll(toggleFavProperty);
        
        if(engine.getInput().getLoggedIn()){//if the user is logged in, 
            menuBar.getMenus().addAll(optionsMenu);
        }
        
        
        return menuBar;
    }

    /**
     * sets the property's information into the anchor pane 
     * @AnchorPane contains the current property's information
     */
    private AnchorPane setContent(){
        AnchorPane  propBox = new AnchorPane ();
        propBox.setMinSize(650, 200);
        VBox infoBox = new VBox();
        VBox priceBox = new VBox();
        
        Label name = new Label(current.getName());
        name.setId("propName");
        Label borough = new Label("Borough: " + current.getNeighbourhood());
        Label roomType = new Label("Room Type: " + current.getRoom_type());
        Label numReviews = new Label("Number Of Reviews: " + current.getNumberOfReviews());
        Label monthReviews = new Label("Reviews Per Month: " + current.getReviewsPerMonth());
        Label lastReview = new Label("Last Review Date: " + current.getLastReview());
        Label hostName = new Label("Host: " + current.getHost_name());
        Label listHosting = new Label("Number Of Properties Host Has: " + current.getCalculatedHostListingsCount());
        Label availability = new Label("Availability Out Of 365 Days: " + current.getAvailability365());
        Label propID = new Label("Property ID: " + current.getId());
        Label hostID = new Label("Host ID: " + current.getHost_id());
        Label price = new Label("£" + current.getPrice());
        Label priceInfo = new Label(" p/night");
        Label spaceLbl = new Label(" ");
        Label spaceLbl2 = new Label(" ");
        Label hostLbl = new Label("Host Information:");
        hostLbl.setId("hostInfo");
        price.setId("propPrice");
        priceBox.getChildren().addAll(price, priceInfo);
        infoBox.getChildren().addAll(name,spaceLbl2,propID, borough,roomType,numReviews,spaceLbl,hostLbl, hostName, hostID, listHosting, priceBox);
        propBox.getChildren().addAll(infoBox,priceBox);
        propBox.setLeftAnchor(infoBox, 5.0);
        propBox.setRightAnchor(priceBox, 5.0);
        propBox.setId("propBox");

        return propBox;
    }
    
    /**
     * intialises the user's username in the engine
     * @param Username user's username
     */
    public static void setInput(Input input){
       engine.setInput(input);
    }
    
}