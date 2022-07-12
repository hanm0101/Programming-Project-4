import java.util.*;
import javafx.scene.control.Alert;
/**
 * Back end for the IndivPropertyWindow 
 * Gets the next and previous properties in the chosen borough
 * Updates a property's 'favourited' status for a user in the database
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19
 */
public class IndivPropertyEngine
{
    //list of all the properties in the chosen borough
    private List<AirbnbListing> boroughListings;
    //the current property being displayed
    private AirbnbListing current;
    //the user's username -> needed to retrieve and update favourite property 
    private static String username;
    private static boolean loggedIn;
    private StatData statData;
    private DataAccount dataAccount;
    private static Input input;
    
    
    /**
     * Constructor
     * Initialises variables
     * @param boroughListings list of properties in chosen borough
     * @param currentListing current property being displayed
     */
    public IndivPropertyEngine(List<AirbnbListing> boroughListings, AirbnbListing currentListing){
        this.boroughListings = boroughListings; 
        statData = input.getStatData();
        dataAccount = input.getDataAccount();
        current = currentListing;
        username = input.getUsername();
        loggedIn = input.getLoggedIn();
    }
    
    /**
     * gets the next property in the list to be displayed
     * @return AirbnbListing next property
     */
    public AirbnbListing getNextProperty(){
        int currentIndex = boroughListings.indexOf(current);
        try{
            //current index is now the next index in the array list. If current index was the last, wrap around to first index
            currentIndex = (currentIndex+1) % boroughListings.size();
            current = boroughListings.get(currentIndex);
            //update property counter
            statData.updatePropertyCount(current.getId());
            return current;
        }
        catch(Exception e){
        };
        return null;
    }
    
    /**
     * gets the previous property in the list to be displayed
     * @return AirbnbListing previous property
     */
    public AirbnbListing getPrevProperty(){
        int currentIndex = boroughListings.indexOf(current);
        try{
            if(currentIndex <= 0)
                currentIndex= boroughListings.size()-1;
            else
                currentIndex--;
            current = boroughListings.get(currentIndex);
            statData.updatePropertyCount(current.getId());
            return current;
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PropViewer");
            alert.setHeaderText("Well that flopped.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        };
        return null;
    }
    
    /**
     * if the property is not already a favourite, make it one 
     * if the property is already a favourite, remove its 'favourite' status
     * must update this in the database through DataAccount
     * @return boolea whether the property is a favourite or not 
     */
    public boolean toggleFavouriteProp(){
        //if borough is not already favourited, make it a favourite
        if(!dataAccount.getFavoriteProperty(username, current.getId())){
            dataAccount.addFavoriteProp(username, current.getId());
            //now a favourite
            return true;
        }
        else{
            dataAccount.removeFavoriteProp(username, current.getId());
            //now not a favourite 
            return false;
        }
    }
    
    /**
     * @return boolean is the property a favourite? true if yes, false if no
     */
    public boolean isFavoriteProperty(){
        return dataAccount.getFavoriteProperty(username, current.getId());
    }
    
    /**
     * intialises the user's username for this object window
     * @param Username user's username
     */
    public static void setInput(Input newInput){
        input = newInput;
    }
    
    /**
     * Returns whether or not the user is logged into the system
     * @return boolean true, if user is logged in, false if not logged in
     */
    public Input getInput(){
        return input;
    }
    
}