


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.*;

/**
 * Main window to be opened. This controls the order
 * of the next and previous panels to show.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class Main extends Application
{
    // We keep track of the count, and label displaying the count:
    private static int current = 0;
    private static int from = 0;
    private static int to = 0;
    private static String loggedUser;
    private static boolean loggedin;
    private Label myLabel = new Label("0");
    private static Stage stage;
    private static ArrayList<Scene> scenes;
    private static DataAccount accountData = new DataAccount();
    private static StatData statData = new StatData();
    private static Input input = new Input();
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        
        StatGUI.setInput(input);
        input.setDataAccount(accountData);
        input.setStatData(statData);
        LoginWindow mainInitial = new LoginWindow(input);
        stage.setTitle("Airbnb");
        stage.setScene(mainInitial.getScene());
        stage.setResizable(false);
        //Show the Stage (window)
        stage.show();
    }
    

     /**
     * Sets the scene to show.
     */
    public static void switchScenes()
    {
        switch(current){
            case 0:
            LoginWindow main = new LoginWindow(input);
            Scene tempMain = main.getScene();
            stage.setScene(tempMain);
            break;
            case 1:
            MapWindow map = new MapWindow(input);
            Scene tempMap = map.getScene();
            stage.setScene(tempMap);
            try{
               new StatGUI().start(new Stage());
            }catch(Exception e){
               System.out.println(e);
            }
            break;
            case 2:
            SearchBar search = new SearchBar(input);
            Scene tempSearch = search.getScene();
            stage.setScene(tempSearch);
            break;
            case 3:
            if(input.getLoggedIn()){
                AccountWindow account = new AccountWindow(input);
                Scene tempAccount = account.getScene();
                stage.setScene(tempAccount);
            }
            else{
                NotLoggedWindow notLogged = new NotLoggedWindow(input);
                Scene tempNotLogged = notLogged.getScene();
                stage.setScene(tempNotLogged);
            }
            break;
            default: 
            System.out.println("ERROR");
            break;
        } 
   }
        
   /**
    * Increments the values of current and then switch the scenes.
    * 
    */
   public  static void next(){
            current++;
            if(current > 3) current = 0;
            switchScenes();
   }
      
   /**
    * Decrements the values of current and then switch the scenes.
    */
   public static void previous(){
            current--;
            if(current <0 ) current = 3;
            switchScenes();
   }
}
