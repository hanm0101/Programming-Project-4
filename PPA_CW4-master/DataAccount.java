import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DataAccount
{
    // instance variables - replace the example below with your own
    private AirbnbDataLoader dataLoader;
    private ArrayList<AirbnbListing> listing ;
    private HashMap<String , String> InitialAccounts;
    private Connection conn ;
    /**
     * Constructor for objects of class test
     */
    public DataAccount()
    {
        dataLoader = new AirbnbDataLoader();
        listing = dataLoader.load();
        try{
            conn = Database.getConnection();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Checks if the username doesn't already exist.
     * @param newusername The username to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean checkUserExist(String newusername){
        try{
            ArrayList<String> existingUsernames =  Database.getUsernames(conn);
            Iterator<String> usernamesIterator = existingUsernames.iterator();
            while (usernamesIterator.hasNext()) {
                String username = usernamesIterator.next();
                if(username.toLowerCase().equals(newusername.toLowerCase())){
                    return true;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    
     /**
     * Creates an account with the username and password enterred.
     * @param newusername the new accounts username.
     * @param newpassword the new accounts password.
     * @return true if the account has been created.
     */
    public boolean createAnAccount(String newusername, String  newpassword){
        try{
            if(checkUserExist(newusername)){
                return false;
            }
            if(newusername!= null && newpassword!= null){
                if(!newusername.trim().equals("") && !newpassword.trim().equals("")){
                    Database.postLogin(conn, newusername.toLowerCase(), newpassword, null);
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    
    /**
     * Creates an account with the username and password enterred.
     * @param newusername the new accounts username.
     * @param newpassword the new accounts password.
     * @return true if the account has been created.
     */
    public boolean deleteAnAccount(String newusername){
        try{
            if(!checkUserExist(newusername)){
                return true;
            }
            if(newusername!= null ){
                if(!newusername.trim().equals("")){
                    Database.deleteFromLogin(conn, newusername.toLowerCase());
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    
    /**
     * Creates an account if the username doesn't already exist.
     */
    public boolean checkUserPassword(String newusername, String  newpassword){
        try{
            boolean check = checkUserExist(newusername);
            if(!check) return false;
            ArrayList<String> accpass = Database.getPassword(conn, newusername.toLowerCase());
            for(int i = 0; i <accpass.size(); i++) {
                if(newpassword.equals(accpass.get(i))){
                       return true;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    
     /**
     * Checks if this borough is the users favorite.
     * @param username The logged in account.
     * @param boroughName the name of the borough to check.
     * @return true if the borough is the users favorite.
     */
    public boolean getFavoriteBorough(String username, String boroughName){
        try{
            ArrayList<String> favoriteBor = Database.getFavoriteB(conn, username.toLowerCase());
            for(int i = 0; i <favoriteBor.size(); i++) {
                if(boroughName.toUpperCase().equals(favoriteBor.get(i))){
                       return true;
                }
            }
        }catch(Exception e){
        }
        return false;
    }
    
    /**
     * Checks if this property is one of the users' favorite.
     * @param username The logged in account.
     * @param propertyID the ID of the property to check.
     * @return true if the property is one of the users' favorite.
     */
    public boolean getFavoriteProperty(String username, String propertyID){
        try{
            ArrayList<String> favoriteProp = Database.getFavoritePs(conn, username.toLowerCase());
            for(int i = 0; i <favoriteProp.size(); i++) {
                if(propertyID.equals(favoriteProp.get(i))){
                       return true;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    
    /**
     * Adds a property to the users favorite.
     * @param username The logged in account.
     * @param IDfav The ID of the property.
     */
    public void addFavoriteProp(String username, String IDfav){
        try{
            
            Database.postFavoriteP(conn, username.toLowerCase(),  IDfav );
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Removes a property from the users favorite.
     * @param username The logged in account.
     * @param IDfav The ID of the property.
     */
    public void removeFavoriteProp(String username, String IDfav){
        try{
            Database.deleteFromFavoritesP(conn, username.toLowerCase(), IDfav);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Adds a borough to the users favorite.
     * @param username The logged in account.
     * @param name Name of the borough to add.
     */
    public void addFavoriteBor(String username, String name){
        try{
            Database.updateFavoriteB(conn,  username.toLowerCase(),  name.toUpperCase());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Removes a borough from the users favorite.
     * @param username The logged in account.
     */
    public void removeFavoriteBor(String username){
        try{
            Database.deleteFavoriteB(conn, username.toLowerCase());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Get the users favorite borough.
     * @param username The logged in account.
     * @return the name of the borough if the user has a favorite.
     */
    public String userFavoriteBor(String username){
        try{
            ArrayList<String> favB = Database.getUserFavoriteB(conn,  username.toLowerCase() );
            for(int i = 0; i < favB.size(); i++){
                if(favB.get(i) != null){
                    return favB.get(i);
                }
                
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the users favorite property.
     * @param username The logged in account.
     * @return the users favorite properties.
     */
    public ArrayList<AirbnbListing> userFavoriteProps(String username){
        try{
            ArrayList<String> favPID = Database.getUserFavoriteP(conn, username.toLowerCase());
            ArrayList<AirbnbListing> favP = new ArrayList<AirbnbListing>();
            for(int i = 0; i <favPID.size(); i++){
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(favPID.get(i))){
                        favP.add(airbnb);
                    }
                }
            }
            return favP;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Checks if the price range has already been searched by the user.
     * @param username The users username
     */
    public boolean checkPriceRange(String username, Integer From, Integer To){
        if(!checkUserExist(username)) {
            return false;
        }
        ArrayList<Integer[]> array = getPriceRange(username.toLowerCase());
        for(int i = 0; i < array.size(); i++){
            if(array.get(i)[0].equals(From) && array.get(i)[1].equals(To)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds the price range searched by the user if it hasn't already been
     * searcheds. If it has it just increases the counter.
     * @param username The users username
     */
    public void addPriceRange(String username, Integer From, Integer To){
        try{
            boolean exists = checkPriceRange(username.toLowerCase(), From, To);
            if(exists){
                Database.updatePRCounter(conn,username.toLowerCase(), From, To);
            }
            else{
                Database.postPriceRange(conn, username.toLowerCase(), From, To, 1);
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Removes the price range searched by the user if it has already been
     * searcheds. 
     * @param username The users username
     */
    public void removePriceRange(String username, Integer From, Integer To){
        try{
            boolean exists = checkPriceRange(username.toLowerCase(), From, To);
            if(exists){
                Database.deleteFromPriceRange(conn,username.toLowerCase(), From, To);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Checks if the search has already been searched by the user.
     * @param username The users username
     */
    public boolean checkSearch(String username, String Search){
        if(!checkUserExist(username)) {
            return false;
        }
        ArrayList<String> array = getSearches(username);
        for(int i = 0; i < array.size(); i++){
            String searched = array.get(i).toLowerCase().trim();
            if(searched.equals(Search.toLowerCase().trim())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds the price range searched by the user if it hasn't already been
     * searcheds. If it has it just increases the counter.
     * @param username The users username
     */
    public void addSearch(String username, String Search){
        try{
            boolean exists = checkSearch(username.toLowerCase(), Search);
            if(exists){
                Database.updateSCounter(conn,username.toLowerCase(), Search);
            }
            else{
                Database.postSearch(conn, username.toLowerCase(),Search, 1);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Removes the search searched by the user if it has already been
     * searcheds. 
     * @param username The users username
     */
    public void removeSearch(String username, String Search){
        try{
            boolean exists = checkSearch(username.toLowerCase(), Search);
            if(exists){
                Database.deleteFromSearch(conn,username.toLowerCase(), Search);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Returns an ArrayList containing the price ranges searched by the user.
     * @param username The username of the user.
     * @return The price ranges searched by the user.
     */
     public ArrayList<Integer[]> getPriceRange(String username){
        try{
            ArrayList<Integer[]> array = Database.getPriceRanges(conn, username.toLowerCase());
            
            return array;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Returns an ArrayList containing the words searched by the user.
     * @param username The username of the user.
     * @return The searches.
     */
     public ArrayList<String> getSearches(String username){
        try{
            ArrayList<String> array = Database.getSearches(conn, username.toLowerCase());
            return array;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}
    