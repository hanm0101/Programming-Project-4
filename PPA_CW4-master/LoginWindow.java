import javafx.application.Application;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.value.*;
import javafx.scene.control.Alert.*;
import java.util.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.io.FileInputStream;

/**
 * LoginWindow is the first panel the user sees
 * It enables them to chose the inital price range
 * and allows them to either create an account or 
 * login.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 28-03-2019
 */
public class LoginWindow extends Panel
{
    private Stage stage;
    private GridPane pane = new GridPane();
    private BorderPane WelcomePane = new BorderPane();
    private GridPane LoginPane = new GridPane();
    private GridPane CreatePane = new GridPane();
    private GridPane LoggedPane = new GridPane();
    private TextField usernameLog = new TextField();
    private PasswordField passwordLog = new PasswordField();
    private TextField usernameCrea = new TextField();
    private PasswordField passwordCrea = new PasswordField();
    private String accUsername;
    private String accPassword;
    private static Integer to;
    private static Integer from;
    private DataAccount dataAccount ;
    private Label generalInst;
    private Scene scene;
    private Input input;
    
    public LoginWindow(Input input){
        this.input = input;
        this.dataAccount = input.getDataAccount();
        initial();
    } 
    
    public LoginWindow(DataAccount dataAccount){
        this.dataAccount = dataAccount;
        initial();
    }
    
    /**
     * Returns the login scene.
     */
    public Scene getScene(){
        return scene;
    }
    
    /**
     * Initialises the login scene.
     */
    public void initial()
    {
       
        // Create the root pane 
        pane.setMinSize(733,606);
        
        //Initialises the top and bottom panes
        createTopPane(pane);
        createBottomPane(pane);

        // Sets the content of the middle pane. 
        setWelcome();
        setLogin();
        setCreate();
        setLogged();
        
        if(!input.getLoggedIn()) pane.add(WelcomePane, 0, 1);
        if(input.getLoggedIn()) pane.add(LoggedPane, 0, 1);
        // JavaFX must have a Scene (window content) inside a Stage (window)
        scene = new Scene(pane, 733,606);
        scene.getStylesheets().add("Design.css");
    }
    
    /**
     * Create the top pane and add all its component
     * @param  pane the root pane to add the top pane to
     */
    private void createTopPane(GridPane pane){
        // Create the top pane
        GridPane TOPpane = new GridPane();
        TOPpane.setMinSize(650,40);
        TOPpane.getStyleClass().add("pane");
        TOPpane.setPadding(new Insets(5));

        //create labels
        Label fromLabel = new Label("From: ");
        fromLabel.setMinWidth(50);
        Label toLabel = new Label("To: ");
        toLabel.setMinWidth(30);
        toLabel.setId("forwardBtn");
        fromLabel.setId("backBtn");
        Button goBtn = new Button("Let's go !!");
        goBtn.setId("searchBtn");

        goBtn.setOnAction(this::search);
        //Create the two choice boxes
        ChoiceBox<Integer> toCB = new ChoiceBox();
        toCB.getItems().addAll(0, 50, 100, 200, 300,  400, 500, 600, 700, 800, 900, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 5000, 6000);
        
        ChoiceBox<Integer> fromCB = new ChoiceBox();
        toCB.setId("toBox");
        fromCB.setId("fromBox");
        fromCB.getItems().addAll(0, 50, 100, 200, 300,  400, 500, 600, 700, 800, 900, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 5000, 6000);
        
        if(input.getFromValue() != null && input.getToValue() != null){
            toCB.getSelectionModel().select(input.getToValue());
            fromCB.getSelectionModel().select(input.getFromValue());
        }
        
        //Create the alert box that appears when the to and from values are invalid.
        Alert alert = new Alert(AlertType.ERROR, "The values selected are invalid !");
        //Listens to changes to the "to" dropBox selected value.
        toCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    to = toCB.getItems().get((Integer) new_value);
                    if(to !=null && from !=null && from > to){
                        Optional<ButtonType> result = alert.showAndWait();
                        input.setValid(false);
                        to = input.getToValue();
                        fromCB.setValue(to);
                    }
                    else if(to !=null && from != null){ 
                        input.setValid(true);
                        input.setToValue(to);
                    }}
            }  );

        //Listens to changes to the "from" dropBox selected value.
        fromCB.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>(){
                public void changed(ObservableValue ov, Number value, Number new_value){
                    from = fromCB.getItems().get((Integer) new_value);
                    if(to !=null && from !=null && from > to){
                        Optional<ButtonType> result = alert.showAndWait();
                        input.setValid(false);
                        from = input.getFromValue();
                        fromCB.setValue(from);
                    }
                    else if(to !=null && from != null) {
                        input.setValid(true);
                        input.setFromValue(from);
                    ;}
                }
            }  );

        //Add the labels and choice boxes to the top pane.
        VBox box = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(fromLabel, 1, 0);
        gridPane.add(fromCB, 10, 0);
        gridPane.add(toLabel, 20, 0);
        gridPane.add(toCB, 30, 0);
        gridPane.add(goBtn,38 ,0);
        box.getChildren().add(gridPane);
        TOPpane.add(box, 0, 1);
        pane.add(TOPpane, 0, 0);
        
    }
    
    /**
     * Create the bottom pane and add all its component
     * @param  pane the root pane to add the top pane to
     */
    private void createBottomPane(GridPane pane){
        // Create the bottom pane
        GridPane BOTTOMpane = new GridPane();
        BOTTOMpane.setMinSize(733, 33);
        BOTTOMpane.getStyleClass().add("pane");
        BOTTOMpane.setPadding(new Insets(0,10,0,10));

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
        BOTTOMpane.add(backButton, 0, 0);
        BOTTOMpane.add(spacer, 1, 0);
        BOTTOMpane.add(nextButton, 2, 0);

        pane.add(BOTTOMpane, 0, 3); // add it to the root pane
    }
    
    /**
     * Create the welcome pane and add all its component
     */
    private void setWelcome(){
        //Create the welcome pane.
        WelcomePane.setMinSize(733, 533);
        WelcomePane.getStyleClass().add("pane");
        
        VBox top = new VBox();
        top.setMinSize(733, 30);
        top.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome ! "); 
        welcome.setId("welcome");
        
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);
        separator1.setMaxWidth(710);
        separator1.setId("sep");
        
        //Create a pane containing instructions and general informations.
        GridPane infoPane = new GridPane();
        infoPane.setMinSize(520, 450);
        infoPane.setAlignment(Pos.CENTER);
        infoPane.setHgap(10);
        infoPane.setPadding(new Insets(10));
        
        try{
            Image image1 = new Image(new FileInputStream("london-drawing-1.png"));
            ImageView logo = new ImageView(image1);
            logo.setFitHeight(200);
            logo.setPreserveRatio(true);
            logo.setSmooth(true);
            logo.setCache(true);
            infoPane.add(logo, 1,0);
        }catch(Exception e){
            System.out.println(e);
        }
        //Create pane containing buttons to login and create an account
        GridPane accPane = new GridPane();
        accPane.setMinSize(205, 450);
        accPane.setAlignment(Pos.CENTER);
        accPane.setVgap(10);
        GridPane accPane1 = new GridPane();
        accPane1.setId("accPane");
        accPane1.setMinSize(175, 435);
        accPane1.setAlignment(Pos.CENTER);
        accPane1.setHgap(20);
        accPane1.setVgap(20);
        accPane1.setPadding(new Insets(20));
        
        
        generalInst = new Label("Select a price range to get started !");
        generalInst.setTextAlignment(TextAlignment.JUSTIFY);
        generalInst.setWrapText(true);
        Label instructionAcc = new Label("Create an account or login to save your favorites ! \nYou will also be able to get more personalised \nrecommendations.");
        instructionAcc.setTextAlignment(TextAlignment.JUSTIFY);
        instructionAcc.setWrapText(true);
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.setMaxHeight(455);
        separator2.setId("sep");
        Button CreateAccount = new Button("New account");
        CreateAccount.setId("accountBtn");
        Button ExistingAccount = new Button("Existing account");
        ExistingAccount.setId("accountBtn");
        
        CreateAccount.setOnAction(this::createAccount);
        ExistingAccount.setOnAction(this::existingAccount);
        
        top.getChildren().add(welcome);
        top.getChildren().add(separator1);
        infoPane.add(generalInst, 1,1);
        infoPane.add(instructionAcc, 1,2);
        accPane1.add(CreateAccount, 0, 0);
        accPane1.add(ExistingAccount, 0,1);
        accPane.add(accPane1, 0, 0);
        WelcomePane.setTop(top);
        WelcomePane.setLeft(infoPane);
        WelcomePane.setCenter(separator2);
        WelcomePane.setRight(accPane);
        
        
    }
    
    /**
     * Create the login pane and add all its component
     */
    private void setLogin(){
        LoginPane.setMinSize(650, 533);
        LoginPane.setId("page");
        LoginPane.setAlignment(Pos.CENTER);
        
        GridPane out = new GridPane();
        out.setMinSize(370, 213);
        out.getStyleClass().add("pane");
        out.setAlignment(Pos.CENTER);
        GridPane midLogin = new GridPane();
        midLogin.setMinSize(350, 193);
        midLogin.setId("logPane");
        midLogin.setAlignment(Pos.CENTER);
        Button accback = new Button("go back");
        accback.setId("accountBtn");
        Button Login = new Button("Login");
        Login.setId("accountBtn");
        Label ULabel = new Label("Username :");
        Label PLabel = new Label("Password :");
        
        Login.setOnAction(this::login);
        accback.setOnAction(this::goBackMainLog);
        usernameLog.setOnKeyReleased(this::getusernameLog);
        passwordLog.setOnKeyReleased(this::getpasswordLog);
        
        midLogin.add(ULabel, 0,0);
        midLogin.add(usernameLog, 1,0);
        midLogin.add(PLabel, 0,1);
        midLogin.add(passwordLog, 1,1);
        midLogin.add(Login, 2,3);
        midLogin.add(accback, 0,4);
        out.add(midLogin, 0,0);
        LoginPane.add(out, 0,0);
    }
    
    /**
     * Create the create pane and add all its component
     */
    private void setCreate(){
        CreatePane.setMinSize(650, 533);
        CreatePane.setId("page");
        CreatePane.setAlignment(Pos.CENTER);
        
        GridPane out = new GridPane();
        out.setMinSize(370, 213);
        out.getStyleClass().add("pane");
        out.setAlignment(Pos.CENTER);
        GridPane midCreate = new GridPane();
        midCreate.setMinSize(350, 193);
        midCreate.setId("logPane");
        midCreate.setAlignment(Pos.CENTER);
        Button accback = new Button("go back");
        accback.setId("accountBtn");
        Button Create = new Button("Create");
        Create.setId("accountBtn");
        Label ULabel = new Label("Username :");
        Label PLabel = new Label("Password :");
        
        Create.setOnAction(this::create);
        accback.setOnAction(this::goBackMainCrea);
        usernameCrea.setOnKeyReleased(this::getusernameCrea);
        passwordCrea.setOnKeyReleased(this::getpasswordCrea);
        
        midCreate.add(ULabel, 0,0);
        midCreate.add(usernameCrea, 1,0);
        midCreate.add(PLabel, 0,1);
        midCreate.add(passwordCrea, 1,1);
        midCreate.add(Create, 2,3);
        midCreate.add(accback, 0,4);
        out.add(midCreate, 0,0);
        CreatePane.add(out, 0,0);
    }
    
    /**
     * Create the logged in pane and add all its component
     */
    private void setLogged(){
        LoggedPane.setMinSize(650, 533);
        LoggedPane.getStyleClass().add("pane");
        LoggedPane.setAlignment(Pos.CENTER);
        Label logged = new Label("Logged in");  
        Button logout = new Button("Log out");
        logout.setId("accountBtn");
        logout.setOnAction(this::accountLogout);
        LoggedPane.add(logged, 0, 0);
        LoggedPane.add(logout, 1,1);
    }
    
    /**
     * Saves the value of the username based on the login;
     */
    private void getusernameLog(KeyEvent event){
        accUsername = usernameLog.getText();
    }
    
    /**
     * Saves the value of the password based on the login ;
     */
    private void getpasswordLog(KeyEvent event){
        accPassword = passwordLog.getText();
    }
    
    /**
     * Saves the value of the username based on the create page;
     */
    private void getusernameCrea(KeyEvent event){
        accUsername = usernameCrea.getText();
    }
    
    /**
     * Saves the value of the password based on the create page;
     */
    private void getpasswordCrea(KeyEvent event){
        accPassword = passwordCrea.getText();
    }
 
    /**
     * Changes the middle pane from welcome to create.
     */
    private void createAccount(ActionEvent event){
        pane.getChildren().remove(WelcomePane);
        pane.add(CreatePane, 0,1);
    }
    
    /**
     * Changes the middle pane from welcome to login.
     */
    private void existingAccount(ActionEvent event){
        pane.getChildren().remove(WelcomePane);
        pane.add(LoginPane, 0,1);
    }
    
    /**
     * Changes the middle pane from login to welcome.
     */
    private void goBackMainLog(ActionEvent event){
        pane.getChildren().remove(LoginPane);
        pane.add(WelcomePane, 0,1);
        accUsername = "";
        accPassword = "";
        usernameLog.setText("");
        passwordLog.setText("");
    }
    
    /**
     * Changes the middle pane from create to welcome.
     */
    private void goBackMainCrea(ActionEvent event){
        pane.getChildren().remove(CreatePane);
        pane.add(WelcomePane, 0,1);
        accUsername = "";
        accPassword = "";
        usernameCrea.setText("");
        passwordCrea.setText("");
    }
    
    /**
     * Checks if the user can login.
     * Logins in if they can.
     */
    private void login(ActionEvent event){
        boolean validaccount = dataAccount.checkUserPassword(accUsername, accPassword);
        if(!validaccount) {
            Alert invalert = new Alert(AlertType.ERROR, "The username or the password are invalid !");
            Optional<ButtonType> result = invalert.showAndWait();
        }
        else{
            pane.getChildren().remove(LoginPane);
            pane.add(LoggedPane, 0,1);
            input.setLoggedIn(true);
            input.setUsername(accUsername);
            accUsername = "";
            accPassword = "";
        }
    }
    
    /**
     * Checks if the user can create.
     * Create if they can.
     */
    private void create(ActionEvent event){
        boolean accountexist = dataAccount.checkUserExist(accUsername);
        if(accountexist) {
            Alert crealert = new Alert(AlertType.ERROR, "An account with that username already exists !");
            Optional<ButtonType> result = crealert.showAndWait();  
        }   
        else{
            boolean createAccount = dataAccount.createAnAccount(accUsername, accPassword);
            if(! createAccount) {
                Alert invalert = new Alert(AlertType.ERROR, "The username or the password are invalid !");
                Optional<ButtonType> result = invalert.showAndWait();  
            }
            else{
                pane.getChildren().remove(CreatePane);
                pane.add(LoginPane, 0,1);
                accUsername = "";
                accPassword = "";
            }
        }
    }
    
    /**
     * Log out when pressed.
     */
    private void accountLogout(ActionEvent event){
        input.setLoggedIn(false);
        input.setUsername("");
        pane.getChildren().remove(LoggedPane);
        pane.add(WelcomePane, 0,1);
    }
    
    /**
     * Adds price range to database if logged in.
     * Changes the text in welcome if the price range chosen is valid.
     */
    private void search(ActionEvent event){
        if(input.getLoggedIn() && input.getValid()){
            dataAccount.addPriceRange(input.getUsername(), from,to);
            }
        else if(input.getValid()){
            
            generalInst.setText("Click on next to access a map of London."
                +" You will \nbe able to get more informations on the properties per \nborough."
                +" You will also get access to some statistics \nbased on the price range you selected."
                +" If you want more \nspecific searches, just click next after the map."
                +" \nYou can also access some general informations about \nthe listings and more if you are logged in!");
                
            input.setFromValue(from);
            input.setToValue(to);
        }
    }
    
    /**
     * Goes to the next panel if the price range chosen is valid.
     */
    @Override
    public void nextPanel(ActionEvent event){
        if(input.getValid()){
            super.nextPanel(event);
        }
    }
    
    /**
     * Goes to the previous panel if the price range chosen is valid.
     */
    @Override
    public void previousPanel(ActionEvent event){
        if(input.getValid()){
            super.previousPanel(event);
        }
    }
}
