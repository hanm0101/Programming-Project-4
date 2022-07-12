import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import java.time.temporal.ChronoUnit;
import javafx.util.Callback;
import java.time.*;
import javafx.geometry.HPos;
import javafx.scene.shape.Polygon;
import java.util.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.*;
import java.io.IOException;
import javafx.beans.value.*;
import javafx.animation.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
/**
 * The is the GUI for the Map window. It displays the
 * map of the boroughs and visually displays the number of
 * properties each borough has that meets the requirements of the
 * price and date range.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class MapWindow extends Panel
{
    private MapLoader mapLoader = new MapLoader();
    private Button goBtn;
    private int noOfDays =0;
    private static Integer min;
    private static Integer max;
    private Stage stage;
    private boolean valid = false;
    private StatData statData;
    private BoroughPropertiesWindow propertyWindow = new BoroughPropertiesWindow();
    private DataAccount dataAccount;
    private Scene scene;
    private Input input;
    
    public MapWindow(Input input){
        this.input = input;
        this.dataAccount= input.getDataAccount();
        this.statData = input.getStatData();
        initial();
    }
    
    /**
     * Return the map scene.
     */
    public Scene getScene(){
        return scene;
    }
    
    /**
     * Initialises the scene;
     */
    public void initial()
    {
        this.stage = stage;
        GridPane pane = new GridPane();// Create the root pane
        pane.setMinSize(733,606);

        min = input.getFromValue();
        max = input.getToValue();
        
        mapLoader.createProperties();
        mapLoader.createBoroughsMap();

        createTopPane(pane);
        createMiddlePane(pane);
        createKeyPane(pane);
        createBottomPane(pane);
        // JavaFX must have a Scene (window content) inside a Stage (window)
        scene = new Scene(pane, 733,606);
        scene.getStylesheets().add("Design.css");
    }

    /**
     * Create the top pane and add all its component
     * @param  pane the root pane to add the top pane to
     */
    private void createTopPane(GridPane pane)
    {
        // Create the top pane
        GridPane topPane = new GridPane();
        topPane.setMinSize(733,80);
        topPane.getStyleClass().add("pane");
        topPane.setPadding(new Insets(0,10,0,10));

        //create labels and buttons
        Label fromLabel = new Label("From: ");
        Label toLabel = new Label("To: ");
        toLabel.setId("forwardBtn");
        fromLabel.setId("backBtn");
        goBtn = new Button("Search");
        goBtn.setId("searchBtn");
        
        
        //Create the two choice boxes
        ChoiceBox<Integer> toCB = new ChoiceBox();
        toCB.getSelectionModel().select(input.getToValue());
        toCB.getItems().addAll(0, 50, 100, 200, 300,  400, 500, 600, 700, 800, 900, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 5000, 6000);
        ChoiceBox<Integer> fromCB = new ChoiceBox();
        fromCB.getSelectionModel().select(input.getFromValue());
        toCB.setId("maxBox");
        fromCB.setId("minBox");
        fromCB.getItems().addAll(0, 50, 100, 200, 300,  400, 500, 600, 700, 800, 900, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 5000, 6000);
        //Create the alert box that appears when the to and from values are invalid.
        Alert alert = new Alert(AlertType.ERROR, "The values selected are invalid !");
        
        //Listens to changes to the "to" dropBox selected value.
        toCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    max = toCB.getItems().get((Integer) new_value);
                    if(max !=null && min !=null && min > max){
                        Optional<ButtonType> result = alert.showAndWait();
                        valid = false;
                        max = input.getToValue();
                    }
                    else if(max != null && min != null){ 
                        valid = true;
                        input.setToValue(max);
                    }}
            }  );
            
        //Listens to changes to the "from" dropBox selected value.
        fromCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    min = fromCB.getItems().get((Integer) new_value);
                    if(max !=null && min !=null && min > max){
                        Optional<ButtonType> result = alert.showAndWait();
                        valid = false;
                        min = input.getFromValue();
                    }
                    else if(max !=null && min !=null) {
                        valid = true;
                        input.setFromValue(min);
                    }
                }
            }  );

        //Add the labels and choice boxes to the top pane.
        VBox box = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(fromLabel, 1, 0);
        gridPane.add(fromCB, 3, 0);
        gridPane.add(toLabel, 21, 0);
        gridPane.add(toCB, 23, 0);
        gridPane.add(goBtn,42 ,0);
        box.getChildren().add(gridPane);
        topPane.add(box, 0, 1);

        pane.add(topPane, 0, 0); //add it to the root pane
        timeUI(topPane);
    }

    /** 
     * Create the middle pane and add all its components
     * @param  pane the root pane to add the middle pane to
     */
    private void createMiddlePane(GridPane pane){
        // Create the middle pane
        Pane middlePane = new Pane();
        middlePane.setMinSize(733, 430);
        middlePane.getStyleClass().add("pane");
        Tooltip tooltip = new Tooltip("Click on a borough to view the properties available."); //display instructions for the user
        Tooltip.install(pane, tooltip);

        pane.add(middlePane, 0, 1);
        goBtn.setOnAction(event -> {//if the buttom is clicked check the minimum no. of days and price range has been inserted
                if(mapLoader.canSearch(input.getFromValue(), input.getToValue(), input.getNumDays())){
                    mapLoader.countProperties(input.getFromValue(), input.getToValue(),  input.getNumDays());
                    displayButtons(middlePane);
                    if(input.getLoggedIn()){
                        dataAccount.addPriceRange(input.getUsername(), min,max);
                    }
                    try{
                        new StatGUI().start(new Stage());
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }else{//else inform the user to change their date range or price range
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a valid date range");
                    alert.showAndWait();}});
    }

    /** Create the bottom pane and add all its components
     *
     * @param  pane the root pane to add the top pane to
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

    /** Create the third pane which holds the map key and add all its components
     *
     * @param  pane the root pane to add the top pane to
     */
    private void createKeyPane(GridPane pane){
        // Create the pane for the key box
        HBox keyPane = new HBox();
        keyPane.setSpacing(20);
        keyPane.setAlignment(Pos.CENTER);
        keyPane.setMinSize(650, 60);
        keyPane.getStyleClass().add("pane");

        // create labels for the key and their colours
        Label keyLabel = new Label(" KEY:  ");
        keyLabel.setId("keyLbl");
        Label keyZero = new Label("<=25");
        Label zeroColour = new Label("         ");
        zeroColour.setId("label0");
        VBox box0 = new VBox();
        box0.setAlignment(Pos.CENTER);
        box0.getChildren().addAll(keyZero, zeroColour);
        keyLabel.setId("keyLbl");
        Label keyOne = new Label("25 to 50");
        Label oneColour = new Label("         ");
        oneColour.setId("label1");
        VBox box1 = new VBox();
        box1.setAlignment(Pos.CENTER);
        box1.getChildren().addAll(keyOne, oneColour);
        keyLabel.setId("keyLbl");
        Label keyTwo = new Label("50 to 100");
        Label twoColour = new Label("         ");
        twoColour.setId("label2");
        VBox box2 = new VBox();
        box2.setAlignment(Pos.CENTER);
        box2.getChildren().addAll(keyTwo, twoColour);
        Label keyThree = new Label("100 to 300");
        Label threeColour = new Label("         ");
        threeColour.setId("label3");
        VBox box3 = new VBox();
        box3.setAlignment(Pos.CENTER);
        box3.getChildren().addAll(keyThree, threeColour);
        Label keyFour = new Label("300 to 500");
        Label fourColour = new Label("         ");
        fourColour.setId("label4");
        VBox box4 = new VBox();
        box4.setAlignment(Pos.CENTER);
        box4.getChildren().addAll(keyFour, fourColour);
        Label keyFive = new Label("500 to 700");
        Label fiveColour = new Label("         ");
        fiveColour.setId("label5");
        VBox box5 = new VBox();
        box5.setAlignment(Pos.CENTER);
        box5.getChildren().addAll(keyFive, fiveColour);
        Label keySix = new Label("700 to 900");
        Label sixColour = new Label("         ");
        sixColour.setId("label6");
        VBox box6 = new VBox();
        box6.setAlignment(Pos.CENTER);
        box6.getChildren().addAll(keySix, sixColour);
        Label keySeven = new Label(">=900");
        Label sevenColour = new Label("         ");
        sevenColour.setId("label7");
        VBox box7 = new VBox();
        box7.setAlignment(Pos.CENTER);
        box7.getChildren().addAll(keySeven, sevenColour);

        keyPane.getChildren().addAll(keyLabel, box0, box1, box2, box3, box4, box5, box6, box7);//add the boxes to the key pane

        pane.add(keyPane, 0, 2); //add the key pane to the root pane
    }

    /**
     * Display the date picker for the check in and check
     * out dates. Calculate the total number of days the customer
     * is staying.
     * Code help from: https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/date-picker.htm
     * @param  TOPpnae add the date pickers to the GUI
     */
    private void timeUI(GridPane TOPpane)
    {
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        DatePicker checkInDatePicker = new DatePicker();
        DatePicker checkOutDatePicker = new DatePicker(); //create date pickers to store the check in and check out dates
        checkInDatePicker.setValue(LocalDate.now());
        input.setNumDays(noOfDays);
        final Callback<DatePicker, DateCell> dayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item.isBefore(checkInDatePicker.getValue().plusDays(1))) {
                                setDisable(true); //stop user from picking a check out date that is before the check in date
                                setStyle("-fx-background-color: #fe8d14;");
                            }
                            noOfDays =(int) ChronoUnit.DAYS.between(checkInDatePicker.getValue(), checkOutDatePicker.getValue());// calculate total number of days
                            input.setNumDays(noOfDays);
                        }
                    };
                }
            };
        checkOutDatePicker.setId("outPicker");
        checkInDatePicker.setId("inPicker");
        checkOutDatePicker.setDayCellFactory(dayCellFactory);
        checkOutDatePicker.setValue(checkInDatePicker.getValue().plusDays(1));//initially set the checkout day to the check in date+1
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label checkInlabel = new Label("Check-In Date: ");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 1, 0);
        Label checkOutlabel = new Label("Check-Out Date: ");
        gridPane.add(checkOutlabel, 2, 0);
        GridPane.setHalignment(checkOutlabel, HPos.LEFT);
        gridPane.add(checkOutDatePicker, 3, 0); // store the date pickers and labels in a gridPane
        vbox.getChildren().add(gridPane);
        TOPpane.add(vbox, 0, 0);  //add the gridPane to the main GUI
    }

    /**
     * Create a button for each borough and and display it. The map
     * of buttons will fade into the screen
     *
     * @param  middlePane the pane the buttons are added to to be displayed
     */
    private void displayButtons(Pane middlePane)
    {
        List<String> btnNames = new ArrayList<>();
        //btnNames.clear();
        Collections.addAll(btnNames, "ENFI", "BARN", "HRGY", "WALT", "HRRW", "BREN", "CAMD", "ISLI",
            "HACK", "REDB", "HAVE", "HILL", "EALI", "KENS", "WSTM", "TOWH","NEWH", "BARK", "HOUN",
            "HAMM", "WAND", "CITY", "GWCH", "BEXL", "RICH", "MERT", "LAMB", "STHW", "LEWS", "KING", "SUTT",
            "CROY", "BROM");
        for(int i=0; i<btnNames.size(); i++){  //iterate through hashmap to get buttons
            String btnName = btnNames.get(i);
            Button btn = new Button(btnNames.get(i));
            btnIDs(btnNames.get(i), btn);
            btnStructure(btn, i);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        try {
                            BoroughPropertiesWindow.setInput(input);
                            statData.updateBoroughCount(mapLoader.findBorough(btnName));
                            propertyWindow.loadCriteria(mapLoader.findBorough(btnName), input.getFromValue(), input.getToValue(), noOfDays);
                            propertyWindow.start(new Stage());
                        }
                        catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });
            middlePane.getChildren().add(btn); //add the button the pane to be displayed
            FadeTransition ft = new FadeTransition(Duration.millis(1000000000), middlePane);//fade the map in
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.play();
        }
    }

    /**
     * Give each borough an ID corresponding to the number
     * of properties it has available. This is used to change its
     * tile's colour.
     * @param btnName the name of the buttom
     * @param btn the button itself
     */
    private void btnIDs(String btnName, Button btn)
    {
        int value = mapLoader.getCount(btnName);
        
        if(value >=25 && value <50){
            btn.setId("typeOne");
        }
        else if(value >=50 && value <100){
            btn.setId("typeTwo");
        }
        else if(value >=100 && value <300){
            btn.setId("typeThree");
        }
        else if(value>=300 && value <500){
            btn.setId("typeFour");
        }
        else if(value>=500 && value<700){
            btn.setId("typeFive");
        }
        else if(value >=700 && value<900){
            btn.setId("typeSix");
        }
        else if(value>=900){
            btn.setId("typeSeven");
        }
        else{
            btn.setId("typeZero");
        }
    }

    /**
     * This method sets up the geographically correct
     * representaion of the map by giving each borough
     * specific X and Y coordinates.
     * @param  btn the button that will have its xna dy values changed
     * @param i the index of the button to maintain order
     */
    private void btnStructure(Button btn, int i)
    {
        Polygon hexagon = new Polygon(100.0, 0.0,130.0, 20.0,130.0,
                50.0,100.0, 70.0,70.0,
                50.0,70.0, 20.0);
        btn.setShape(hexagon);//each button as the hexagon shape
        btn.setMinSize(70, 70);
        if(i==0){
            btn.setLayoutX(353);
            btn.setLayoutY(5.0);
        }
        if (i>0 && i<4){
            double initialXCoordinate = 244;
            for(int index = 2; index <=i ; index++){
                initialXCoordinate += 72;//move each button along 72 points
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(58.0);
        }
        if (i>=4 && i<11){
            double initialXCoordinate= 136;//new row of buttons
            for(int index = 5; index <=i ; index++){
                initialXCoordinate += 72;
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(111.0);
            initialXCoordinate+= 72;
        }
        if (i>=11 && i<18){
            double initialXCoordinate= 100;
            for(int index = 12; index <=i ; index++){
                initialXCoordinate += 72;
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(164.0);
            initialXCoordinate += 72;
        }
        if (i>=18 && i<24){
            double initialXCoordinate= 136;
            for(int index = 19; index <=i ; index++){
                initialXCoordinate += 72;
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(217.0);
            initialXCoordinate+= 72;
        }
        if (i>=24 && i<29){
            double initialXCoordinate= 172;
            for(int index = 25; index <=i ; index++){
                initialXCoordinate += 72;
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(270.0);
            initialXCoordinate+= 72;
        }
        if (i>=29 && i<33){
            double initialXCoordinate= 208;
            for(int index = 30; index <=i ; index++){
                initialXCoordinate += 72;
            }
            btn.setLayoutX(initialXCoordinate);
            btn.setLayoutY(323.0);
            initialXCoordinate+= 72;
        }
    }
}
