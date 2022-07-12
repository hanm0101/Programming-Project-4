
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ChoiceBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * StatGUI starts the application of the statistics GUI. 
 * StatGUI initiates stat controller class and stats.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29-03-2019
 */
public class StatGUI extends Application
{
    private static StatGUIController statController; //calls the stat controller class
    private static Input input;
    
    @Override
    public void start(Stage statStage) throws Exception
    {
        URL url = getClass().getResource("StatViewerFXML.fxml");
        Parent root = FXMLLoader.load(url);
        statController = new StatGUIController();
        // initiates scene in a stage
        Scene statScene = new Scene(root);
        statScene.getStylesheets().add("Design.css");
        statStage.setTitle("Statistics");
        statStage.setScene(statScene);
        // Show the Stage (window)
        statStage.show();
        statController.initialize();
    }
    
    public static void setInput(Input newInput){
        statController.setInput(newInput);
    }
}