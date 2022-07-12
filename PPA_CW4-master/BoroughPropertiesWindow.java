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
 * Displays all the properties in a certain borough in a tile-like grid. 
 * For each property the following fields are shown: 
 * the property's name, the host's name, number of reviews, price and minimum number of nights, 
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19 
 */
public class BoroughPropertiesWindow extends Application
{
    private static BoroughPropertiesEngine engine; 
    //GUI 
    private Stage stage;
    private TilePane tile;
    private ScrollPane root;
    private VBox vbox;
    private Scene scene;
    //all the vboxes to be displayed, each one holding one property
    private HashMap<AirbnbListing, VBox> listingBoxes;
    
    //chosen borough 
    private String neighbourhood = "Barnet"; //default value 
    /**
     * initialises the initial window containing the tile pane of properties and the menubar
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        listingBoxes = new HashMap<>();
        
        this.stage = stage;

        this.root = new ScrollPane();
        root.setHbarPolicy(ScrollBarPolicy.NEVER);
        root.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        root.setMinSize(750, 500);
        root.setFitToWidth(true);
        //load all properties in the chosen borough
        engine.matchCriteriaAndLoad();
        //generate the intial tile of all the proeprties 
        tile = loadTilePane(engine.getListings());
        tile.setId("tile");

        this.vbox = new VBox();
        
        //make the menu bar
        makeMenuBar(vbox, stage);

        vbox.getChildren().add(tile);
        root.setContent(vbox);
        // JavaFX must have a Scene (window content) inside a Stage (window)
        this.scene = new Scene(root, 750, 500);
        scene.getStylesheets().add("Design.css");
        stage.setTitle(neighbourhood);
        stage.setScene(scene);
        stage.setResizable(false);
        // Show the Stage (window)
        stage.show();

    }
    
    /**
     * sets the borough for which the properties are displayed.
     * called before the start method as the borough needs to be known in order to display the correct properties
     * @param borough chosen borough
     */
    public void loadCriteria(String borough, int  minPrice, int  maxPrice, int numDays){
        neighbourhood = borough;
        engine = new BoroughPropertiesEngine(neighbourhood);
        engine.loadCriteria(borough, minPrice, maxPrice, numDays);
    }

    /**
     * make the menubar with menus 'Sort by' and 'Options'
     * selecting items in 'Sort by' sorts the properties by the chosen field and displays them in this order
     * @param parent the parent Pane
     * @param stage the Stage
     */
    private void makeMenuBar(Pane parent, Stage stage)
    {
        MenuBar menubar = new MenuBar();
        menubar.setId("menuBar");
        parent.getChildren().add(menubar);

        Menu sortMenu = new Menu("Sort by");
            MenuItem hostNameItem = new MenuItem(Resources.HOST_NAME);
                hostNameItem.setOnAction(e->updatePane(stage, Resources.HOST_NAME));
            MenuItem priceItem = new MenuItem(Resources.PRICE);
                priceItem.setOnAction(e->updatePane(stage, Resources.PRICE));
            MenuItem noReviewsItem = new MenuItem(Resources.NUMBER_OF_REVIEWS);
                noReviewsItem.setOnAction(e->updatePane(stage, Resources.NUMBER_OF_REVIEWS));
            MenuItem minNightsItem = new MenuItem(Resources.MIN_NIGHTS);
                minNightsItem.setOnAction(e->updatePane(stage, Resources.MIN_NIGHTS));
        sortMenu.getItems().addAll(hostNameItem, priceItem, noReviewsItem, minNightsItem);

        if(engine.getInput().getLoggedIn()){ //only if user is logged in can they have the option to set a favourite borough
            Menu optionsMenu = new Menu("Options");
            CheckMenuItem favBoroughToggle = new CheckMenuItem("Set favourite borough");
            //only if a user is logged in can this menu item be seen
            favBoroughToggle.setSelected(engine.isFavoriteBorough());
            favBoroughToggle.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        String message = "";
                        if(engine.toggleFavouriteBorough()){ //if the borough is now a favourite 
                            message = neighbourhood + " has been set as your favourite borough";
                        }
                        else {
                            message = neighbourhood + " is no longer your favourite borough";
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(neighbourhood);
                        alert.setHeaderText(message);
                        alert.showAndWait();
                    }
                });
            optionsMenu.getItems().addAll(favBoroughToggle);
            menubar.getMenus().addAll(sortMenu, optionsMenu);
        }
        else{
            menubar.getMenus().addAll(sortMenu);
        }
    }
    
    /**
     * creates the tile pane of unsorted properties that is intially seen when borough property window pops up
     */
    private TilePane startingTile(){
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(0));
        tile.setPrefColumns(3);
        tile.setHgap(8);
        tile.setVgap(8);
        List<AirbnbListing> listings = engine.getListings();
        for(AirbnbListing listing : listings){
            //make a pane for each listing and make it a child of the tilepane
            VBox propertyPane = makePropertyPane(listing);
            tile.getChildren().add(propertyPane);
            tile.setAlignment(Pos.CENTER);
            //add to hashmap
            listingBoxes.put(listing, propertyPane);
        }
        return tile;
    }
    
    /**
     *  makes the individual VBoxes to hold properties' details.
     *  One VBox per property
     *  displays name, host's name, price, minimum number of nights and number of reviews of the property
     *  when the VBox is clicked, another window containing more information on the selected property is opened
     *  @return VBox representing one property
     *  @param listing the property to be represented by the VBox
     */
    private VBox makePropertyPane(AirbnbListing listing){
        //VBox to hold information on individual properties
        VBox propertyPane = new VBox();
        propertyPane.setId("propertyPane");
        propertyPane.setMaxWidth(210);
        propertyPane.setMinWidth(210);
        propertyPane.setOnMouseClicked(e-> viewIndivProperty(listing));

        //propertyPane.setPrefSize(200,100);
        Label name = new Label(Resources.NAME + ": "+ listing.getName());
        name.setId("name");
        name.setMaxWidth(200);
        //name.setMaxHeight(100); //HOW TO MAKE INDEFINITELY/ INFINITE. I WANT HEIGHT TO GROW ACCORDINGLY
        name.setWrapText(true);
        
        Label hostName = new Label(Resources.HOST_NAME + ": "+listing.getHost_name());
        hostName.setId("hostName");
        hostName.setWrapText(true);
        Label price = new Label(Resources.PRICE + ": "+listing.getPrice());
        price.setId("price");
        price.setWrapText(true);
        Label numberOfReviews = new Label(Resources.NUMBER_OF_REVIEWS + ": "+listing.getNumberOfReviews());
        numberOfReviews.setId("numberOfReviews");
        numberOfReviews.setWrapText(true);
        Label minNights = new Label(Resources.MIN_NIGHTS + ": "+listing.getMinimumNights());
        minNights.setId("minNights");
        minNights.setWrapText(true);

        //propertyPane.setOnMouseClicked((e)->updatePane());
            
        propertyPane.getChildren().addAll(name, hostName, price, numberOfReviews, minNights);
        return propertyPane;
    }
    
    /**
     * opens up a new window containing more information on the selected property
     * @param property that you want to view individually
     */
    private void viewIndivProperty(AirbnbListing property){
        try{
            StatData sd = engine.getInput().getStatData();
            sd.updatePropertyCount(property.getId());
            IndivPropertyWindow.setInput(engine.getInput());
            IndivPropertyWindow indiv = new IndivPropertyWindow();
            indiv.loadProperties(engine.getListings(), property);
            indiv.start(new Stage());
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PropViewer");
            alert.setHeaderText("Well that flopped.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        };
    }
    
    /**
     * loads the list of properties to be displayed into a tilepane 
     * @return TilePane tile containing the vboxes containing the properties
     * @param listings the properties to be displayed
     */
    private TilePane loadTilePane(List<AirbnbListing> listings){
        TilePane tile = new TilePane();
        Tooltip tooltip = new Tooltip("Click on a property to view more details."); //display instructions for the user
        Tooltip.install(tile, tooltip);

        tile.setPadding(new Insets(0));
        tile.setPrefColumns(3);
        tile.setHgap(8);
        tile.setVgap(8);
        for(AirbnbListing listing : listings){
            //add the vboxes to the tilepane in order in which they're sorted
            //make a pane for each listing and make it a child of the tilepane
            VBox propertyPane = makePropertyPane(listing);
            tile.getChildren().add(propertyPane);
            tile.setAlignment(Pos.CENTER);
        }
        return tile;
    }
    
    /**
     * loads the new pane containing the sorted properties ready to be
     * displayed
     * @param stage the window's Stage
     * @param sortingBy how are you sorting the properties e.g by hostName
     */
    private void updatePane(Stage stage, String sortingBy){
        vbox.getChildren().remove(tile);
        tile = loadTilePane(engine.sortBy(sortingBy));
        vbox.getChildren().add(tile);
    }
    
    /**
     * set's the Input class, which contains all of the 
     * data the user chose.
     * @param input an instance of input
     */
    public static void setInput(Input input){
        engine.setInput(input);
    }
}
