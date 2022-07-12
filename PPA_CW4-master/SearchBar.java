import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import javafx.geometry.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Alert.AlertType;
/**
 * This the GUI for the search page. It will display the
 * filters and search options to the user and output the results
 * based on their search.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class SearchBar extends Panel
{
    private SearchBarLoader searchLoader = new SearchBarLoader();
    private List<AirbnbListing> matchingProperties = new ArrayList<>();
    private VBox middlePane = new VBox();
    private StatData statData;
    private String borough;
    private DataAccount dataAccount;
    private Scene scene;
    private Input input;
   
    public SearchBar( Input input){
        this.input = input;
        this.dataAccount =input.getDataAccount();
        this.statData =input.getStatData();
        initial();
    }
    
    public Scene getScene(){
        return scene;
    }
    
    
    public void initial()
    {
	// Create a new grid pane
        GridPane pane = new GridPane();
        pane.setMinSize(733,606);
        createSearchBar(pane);
        createMiddlePane(pane);
        createBottomPane(pane);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        scene = new Scene(pane, 733,606);
        scene.getStylesheets().add("Design.css");
    }

    /**
     * This creates the top pane which will
     * display the possible ways the user can search. These
     * include by borough and keyword. It also lets the user
     * filter their results in ascending or descending order
     *
     * @param  mainPane the pane to add the search bar to
     */
    private void createSearchBar(GridPane mainPane)
    {
        //create the pane
        GridPane topPane = new GridPane();
        topPane.setMinSize(733, 90);
        topPane.getStyleClass().add("pane");
        topPane.setPadding(new Insets(0,10,0,10));
        //create labels and texboxes
        Label boroughLbl = new Label("Borough: ");
        Label keywordLbl = new Label("Keyword(s): ");
        final TextField keywords = new TextField();
        keywords.setPromptText("Enter any keyword(s)");
        keywords.setPrefColumnCount(40);
        keywords.setId("texbox");
        keywords.getText();
        Button searchBtn = new Button("Search");
        searchBtn.setId("search");
        searchBtn.setAlignment(Pos.BOTTOM_RIGHT);
        //create the choice box for the boroughs
        ChoiceBox<String> boroughCB = new ChoiceBox();
        boroughCB.setId("filters");
        List<String> boroughNames = new ArrayList<>();
        boroughNames = statData.getBoroughs();
        boroughCB.getItems().addAll(boroughNames); //add borough names to the choice box
        Alert alert = new Alert(AlertType.ERROR, "Please enter a valid keyword(s) and/or borough name");
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    if(borough != null || keywords.getText().length() > 0){
                        matchingProperties.clear();
                        searchLoader.getProperties(keywords.getText(), borough);
                        matchingProperties = searchLoader.getPropertyList();//get list of matching properties
                        displayResults(mainPane);
                        String search = (keywords.getText()); //store search in database
                        if(input.getLoggedIn()){
                            dataAccount.addSearch(input.getUsername(), search);
                        }
                    }
                    else{
                        Optional<ButtonType> result = alert.showAndWait();//if no search data entered display a message
                    }
                }
            });

        //Create a choice boxe for filters
        ChoiceBox<String> filterCB = new ChoiceBox();
        filterCB.getItems().addAll("Ascending Price", "Descending Price");
        filterCB.setId("filters");
        Label filterLbl = new Label("Filters: ");
        //listener for the borough choice box
        boroughCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                // if the item of the list is changed
                public void changed(ObservableValue ov, Number value, Number new_value)
                {
                    borough = boroughCB.getItems().get((Integer) new_value);
                }
            });

        Alert filterAlert = new Alert(AlertType.ERROR, "Please search using a keyword or borough first");
        // add a listener for the filters choice box
        filterCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                // if the item of the list is changed
                public void changed(ObservableValue ov, Number value, Number new_value)
                {
                    String choice = filterCB.getItems().get((Integer) new_value);
                    if(choice.equals("Ascending Price") && !matchingProperties.isEmpty()){
                        List<AirbnbListing> tempListing = new ArrayList<>();
                        tempListing = searchLoader.ascendingOrder(matchingProperties); //order matching properties
                        matchingProperties.clear();//clear old results
                        matchingProperties = tempListing;
                        displayResults(mainPane);
                    }
                    else if(choice.equals("Descending Price") && !matchingProperties.isEmpty()){
                        List<AirbnbListing> tempListing = new ArrayList<>();
                        tempListing = searchLoader.descendingOrder(matchingProperties); //order matching properties
                        matchingProperties.clear();//clear old results
                        matchingProperties = tempListing;
                        displayResults(mainPane);
                    }
                    else{
                        Optional<ButtonType> result = filterAlert.showAndWait(); //if there is not any data to reoder then  display a message
                    }
                }
            });

        topPane.add(keywordLbl, 0, 0);
        topPane.add(keywords, 1, 0);
        topPane.add(boroughLbl, 0, 1);
        topPane.add(boroughCB, 1, 1);
        topPane.add(filterLbl, 0, 2);
        topPane.add(filterCB, 1, 2);
        topPane.add(searchBtn, 2,2);
        mainPane.add(topPane, 0, 0);
    }

    /**
     * This pane displays the results of the search
     * @param  pane the main pane to add the middle pane to
     */
    private void createMiddlePane(GridPane pane)
    {
        middlePane.setMinSize(728, 483);
        middlePane.getStyleClass().add("pane");
        middlePane.setPadding(new Insets(0,10,0,10));
        pane.add(middlePane, 0, 2);
    }

    /**
     * This pane displays the next and previos buttons
     *  @param  pane the main pane to add the bottom pane to
     */
    private void createBottomPane(GridPane pane){
        // Create the bottom pane
        GridPane bottomPane = new GridPane();
        bottomPane.setMinSize(733, 33);
        bottomPane.getStyleClass().add("pane");
        bottomPane.setPadding(new Insets(0,10,0,10));

        //Create a spacer pane
        Pane spacer = new Pane();
        spacer.setMinSize(565, 33);

        //Create the next and forward buttons
        Button nextButton = new Button(">>>");
        nextButton.setId("forwardButton");
        Button backButton = new Button("<<<");
        backButton.setId("backButton");
        backButton.setMinSize(70, 10);
        nextButton.setMinSize(70, 10);

        nextButton.setOnAction(this::nextPanel);
        backButton.setOnAction(this::previousPanel);

        //Add the buttons to the bottom pane.
        bottomPane.add(backButton, 0, 0);
        bottomPane.add(spacer, 1, 0);
        bottomPane.add(nextButton, 2, 0);

        pane.add(bottomPane, 0, 3); // add it to the root pane
    }

    /**
     * Display the results of the search by storing each as a vbox
     *
     *  @param  pane the main pane to add the anchor pane to
     */
    private void displayResults(GridPane pane)
    {
        AirbnbListing currentArray;
        middlePane.getChildren().clear();
        if(matchingProperties.size() == 0){ //if not matching results found
            Label results = new Label("No Matching Results Found");
            middlePane.getChildren().add(results);
        }
        else{
            for(int i=0; i<matchingProperties.size(); i++){ //for each matching item display its details
                currentArray = matchingProperties.get(i);// get the array in the arraylist
                addEachProperty(currentArray);
            }
            addScroll(pane);
        }
    }

    /**
     * Display the name, borough, price, number of reviews and the
     * type of accommodation in a vbox and add that to the middle pane
     * @param currentArray the current property to display
     */
    private void addEachProperty(AirbnbListing currentArray)
    {
        AnchorPane  propBox = new AnchorPane ();
        VBox infoBox = new VBox();
        VBox priceBox = new VBox();
        //add data about each property
        Label name = new Label(currentArray.getName());
        name.setId("propName");
        Label borough = new Label("Borough: " + currentArray.getNeighbourhood());
        Label roomType = new Label("Room Type: " + currentArray.getRoom_type());
        Label numReviews = new Label("Number Of Reviews: " + currentArray.getNumberOfReviews());
        Label price = new Label("£" + currentArray.getPrice());
        Label priceInfo = new Label(" p/night");
        price.setId("propPrice");
        priceBox.getChildren().addAll(price, priceInfo);
        infoBox.getChildren().addAll(name, borough,roomType,numReviews,priceBox);
        propBox.getChildren().addAll(infoBox,priceBox);
        propBox.setLeftAnchor(infoBox, 5.0);
        propBox.setRightAnchor(priceBox, 5.0);
        propBox.setId("propBox");
        propBox.setOnMouseClicked(e-> viewIndivProperty(currentArray));
        middlePane.getChildren().add(propBox);
    }

    /**
     * Add a scroll pane so the user can view all
     * the results.
     *  @param  pane the main pane to add the scroll pane to
     */
    private void addScroll(GridPane pane)
    {
        ScrollPane sp = new ScrollPane(middlePane);
        sp.setId("scrollPane");
        sp.setVvalue(0.5);
        sp.setPrefSize(733, 483);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER); // no horizontal bar will ever display
        pane.add(sp, 0, 1);
    }

    /**
     * load individual properties if a property is clicked on
     * @param property is the property the user wants to see
     */
    private void viewIndivProperty(AirbnbListing property){
        try{
            StatData sd = new StatData();
            sd.updatePropertyCount(property.getId());
            IndivPropertyWindow.setInput(input);
            IndivPropertyWindow indiv = new IndivPropertyWindow();
            indiv.loadProperties(matchingProperties, property);
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



