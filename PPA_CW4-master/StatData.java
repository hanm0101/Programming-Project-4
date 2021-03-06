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
public class StatData
{
    // instance variables - replace the example below with your own
    private AirbnbDataLoader dataLoader;
    private ArrayList<AirbnbListing> listing ;
    private Connection conn ;
    /**
     * Constructor for objects of class test
     */
    public StatData()
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
     * Updates the viewing counter for the boroughs.
     */
    public void updateBoroughCount(String BName){
       try{
            Database.updateBCounter(conn,BName);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the viewing counter for the property.
     */
    public void updatePropertyCount(String PID){
       try{
            Database.updatePCounter(conn,PID);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Get the Borough with the most number of views.
     * @return The name of the borough with the most number of views
     */
    public String getMostViewedB(){
        try{
            ArrayList<String> mostBorough = Database.getMostViewB(conn);
            for(int i = 0; i <mostBorough.size(); i++) {
                return mostBorough.get(i);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the Borough with the least number of views.
     * @return The name of the borough with the most number of views
     */
    public String getLeastViewedB(){
        try{
            ArrayList<String> leastBorough = Database.getLessViewB(conn);
            for(int i = 0; i <leastBorough.size(); i++) {
                return leastBorough.get(i);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the Property with the most number of views.
     * @return The ID of the Property with the most number of views
     */
    public AirbnbListing getMostViewedP(){
        try{
            ArrayList<String> mostBorough = Database.getMostViewP(conn);
            for(int i = 0; i <mostBorough.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(mostBorough.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the Property with the least number of views.
     * @return The ID of the borough with the most number of views
     */
    public AirbnbListing getLeastViewedP(){
        try{
            ArrayList<String> leastBorough = Database.getLessViewP(conn);
            for(int i = 0; i <leastBorough.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(leastBorough.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Gets the property with the most favorites.
     * @return the ID of the property.
     */
    public AirbnbListing getBestP(){
        try{
            ArrayList<String> favoriteProperty = Database.getBestP(conn);
            for(int i = 0; i <favoriteProperty.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(favoriteProperty.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Gets the property with the least favorites.
     * @return the ID of the property.
     */
    public AirbnbListing getWorstP(){
        try{
            ArrayList<String> leastFavoriteProperty = Database.getWorstP(conn);
            for(int i = 0; i <leastFavoriteProperty.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(leastFavoriteProperty.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Gets the borough with the most favorites.
     * @return the name of the borough.
     */
    public String getBestB(){
        try{
            ArrayList<String> favoriteBorough = Database.getBestB(conn);
            for(int i = 0; i <favoriteBorough.size(); i++) {
                return favoriteBorough.get(i);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
     /**
     * Gets the borough with the least favorites.
     * @return the name of the borough.
     */
    public String getWorstB(){
        try{
            ArrayList<String> leastFavoriteBorough = Database.getWorstB(conn);
            for(int i = 0; i <leastFavoriteBorough.size(); i++) {
                if(leastFavoriteBorough.get(i) != null){
                    return leastFavoriteBorough.get(i);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
     /**
     * Gets the property with the most favorites.
     * @return the ID of the property.
     */
    public AirbnbListing getStatBestP(int from, int to){
        try{
            ArrayList<String> favoriteProperty = Database.getStatFavoriteP(conn, from, to);
            for(int i = 0; i <favoriteProperty.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(favoriteProperty.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Gets the property with the least favorites.
     * @return the ID of the property.
     */
    public AirbnbListing getStatWorstP(int from, int to){
        try{
            ArrayList<String> leastFavoriteProperty = Database.getStatLeastFavP(conn, from, to);
            for(int i = 0; i <leastFavoriteProperty.size(); i++) {
                for(AirbnbListing airbnb : listing){
                    if(airbnb.getId().equals(leastFavoriteProperty.get(i))){
                       return airbnb;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Gets the borough with the most favorites.
     * @return the name of the borough.
     */
    public String getStatBestB(int from, int to){
        try{
            ArrayList<String> favoriteBorough = Database.getStatFavoriteB(conn, from, to);
            for(int i = 0; i <favoriteBorough.size(); i++) {
                if(favoriteBorough.get(i) != null){
                    return favoriteBorough.get(i);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
     /**
     * Gets the borough with the least favorites.
     * @return the name of the borough.
     */
    public String getStatWorstB(int from, int to){
        try{
            ArrayList<String> leastFavoriteBorough = Database.getStatLeastFavB(conn, from, to);
            for(int i = 0; i <leastFavoriteBorough.size(); i++) {
                if(leastFavoriteBorough.get(i) != null){
                    return leastFavoriteBorough.get(i);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the names of the boroughs.
     */
    public  ArrayList<String> getBoroughs(){
        try{
            ArrayList<String> names = Database.getBNames(conn);
            return names;
        }catch(Exception e){
        }
        return null;
    }
   
}
