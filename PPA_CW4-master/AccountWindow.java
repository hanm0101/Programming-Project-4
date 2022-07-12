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
 * Write a description of JavaFX class AccountWindow here.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class AccountWindow extends Panel
{
    private static Stage stage;
    VBox AccountPane = new VBox();
    VBox RecommendPane = new VBox();
    ScrollPane sp = new ScrollPane(RecommendPane);
    private List<AirbnbListing> recommendedProp = new ArrayList<>();
    private DataAccount dataAccount;
    private ArrayList<AirbnbListing> listing;
    private ArrayList<AirbnbListing> favProps;
    private AirbnbDataLoader dataLoader;
    private Recommendations recommendations = new Recommendations();
    private Scene scene;
    private Input input;

    public AccountWindow( Input input){
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

        setAccountInfo();
        createRecommendPane();

        //Create a spacer pane
        Pane spacer = new Pane();
        spacer.setMinSize(565, 33);

        // Add the secondary panes into the main pane
        pane.add(TopPane, 0, 0);
        pane.add(AccountPane, 0,1);
        pane.add(RecommendPane, 0,2);
        pane.add(BOTTOMpane, 0, 3);

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

    private void setAccountInfo(){
        AccountPane.setMinSize(725, 270);
        AccountPane.getStyleClass().add("pane");
        AccountPane.setPadding(new Insets(10));
        Label accountTitle = new Label("Your Favourites: ");
        accountTitle.setId("title");
        AccountPane.getChildren().add(accountTitle);
        
        String loggedUser = input.getUsername();
        Label FavBorLab = new Label("Favorite borough : " + dataAccount.userFavoriteBor(loggedUser));
        Label FavPropLab = new Label("Favorite properties : ");
        AccountPane.getChildren().add(FavBorLab);
        if(dataAccount.userFavoriteProps(loggedUser).isEmpty()){
            GridPane accountInfo = new GridPane();
            accountInfo.setMinSize(712, 205);
            Label FavProp = new Label("You have no favorite properties !");
            AccountPane.getChildren().add(FavProp);
        }
        else{
            List<AirbnbListing> favProps = dataAccount.userFavoriteProps(loggedUser);
            GridPane prop = new GridPane();
            ScrollPane scroll = new ScrollPane();
            scroll.setMinSize(720, 205);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setId("accountBtn");
            GridPane accountInfo = new GridPane();
            accountInfo.setMinSize(650, 205);
            scroll.setContent(accountInfo);
            prop.add(FavPropLab, 0, 0);
            for(int i = 0; i< favProps.size(); i++){
                AirbnbListing favlisting = favProps.get(i);
                AddEachProperty(favlisting, accountInfo, i, favProps);
            }
            prop.add(scroll, 0, 1);
            AccountPane.getChildren().add(prop);
        }
        }
    

    /**
     * Create the recommendations pane which will display
     * recommended properties based on what the user has previosly
     * viewed. 
     * @return    the sum of x and y
     */
    private void createRecommendPane()
    {
        RecommendPane.setMinSize(733, 250);
        RecommendPane.getStyleClass().add("pane");
        RecommendPane.setPadding(new Insets(10));
        Label titleLbl = new Label("Recommended Properties For You: ");
        titleLbl.setId("title");
        //getRecommendedProperties();
        RecommendPane.getChildren().add(titleLbl);

        GridPane prop = new GridPane();
        ScrollPane scroll = new ScrollPane();
        scroll.setMinSize(707, 205);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setId("accountBtn");
        GridPane recommendedInfo = new GridPane();
        recommendedInfo.setMinSize(712, 205);
        scroll.setContent(recommendedInfo);
        recommendedProp.clear();
        recommendedProp = recommendations.getRecommendations(); // store data in an arraylist
        AirbnbListing currentArray;
        if(recommendedProp.size() == 0){ //if not matching reults found
            Label results = new Label("No Recommended Properties Found");
            RecommendPane.getChildren().add(results);
        } 
        else{
            for(int i = 0; i< recommendedProp.size(); i++){
                AirbnbListing currentProp = recommendedProp.get(i);
                AddEachProperty(currentProp, recommendedInfo, i, recommendedProp);
            }
            prop.add(scroll, 0, 1);
            RecommendPane.getChildren().add(prop);
        }

    }

    /**
     * Add each property to the scroll pane. Display each
     * property's name, borough and price
     * @param  currentProp
     * @param infobox
     * @param i is the index of the property from the list
     * @param propertySet is the set of recommended properties
     * @param currentProp the current property to add
     */
    private void AddEachProperty(AirbnbListing currentProp, GridPane infoBox, int i, List<AirbnbListing> propertySet)
    {
        AnchorPane propPane = new AnchorPane();
        VBox priceBox = new VBox();
        VBox favPropBox = new VBox();

        Label FavProp = new Label(currentProp.getName());
        Label propBorough = new Label(currentProp.getNeighbourhood());
        Label price = new Label("£" + currentProp.getPrice());
        Label priceInfo = new Label(" p/night");
        price.setId("propPrice");

        priceBox.getChildren().addAll(price, priceInfo);
        favPropBox.getChildren().addAll(FavProp, propBorough);
        propPane.getChildren().addAll(favPropBox,priceBox);
        propPane.setLeftAnchor(favPropBox, 5.0);
        propPane.setRightAnchor(priceBox, 5.0);
        propPane.setId("propBox");
        propPane.setMinSize(733, 30);
        infoBox.add(propPane, 0, i);

        AirbnbListing airbnb = listing.get(0);
        for(int num = 0; num < listing.size(); num++){
            if(listing.get(num).getName().equals(propertySet.get(i).getName())){
                airbnb = listing.get(num);
            }
        }
        final AirbnbListing favAirbnb = airbnb;
        propPane.setOnMouseClicked(e-> viewIndivProperty(favAirbnb, propertySet));
    }

    /**
     * load individual properties if a property is clicked on
     * @param property is the property the user wants to see
     */
    private void viewIndivProperty(AirbnbListing property, List<AirbnbListing> list){
        try{
            StatData sd = new StatData();
            sd.updatePropertyCount(property.getId());
            IndivPropertyWindow.setInput(input);
            IndivPropertyWindow indiv = new IndivPropertyWindow();
            indiv.loadProperties(list, property);
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

    

}