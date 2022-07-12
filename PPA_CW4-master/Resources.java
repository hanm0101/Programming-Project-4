import java.util.*;
/**
 * Write a description of class StringConstants here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Resources
{
    public static final String[] ALL_BOROUGHS= {"Kingston upon Thames","Croydon","Bromley","Hounslow","Ealing","Havering","Hillingdon","Harrow",
    "Brent","Barnet","Enfield","Waltham Forest","Redbridge","Sutton","Lambeth","Southwark","Lewisham", "Greenwich","Bexley",
    "Richmond upon Thames", "Merton","Wandsworth", "Hammersmith and Fulham", "Kensington and Chelsea", "City of London",
    "Westminster", "Camden", "Tower Hamlets", "Islington", "Hackney", "Haringey","Newham", "Barking and Dagenham"};
    
    public static final String PRIVATE_ROOM = "Private room";
    public static final String ENTIRE_HOME = "Entire home/apt";
    public static final String SHARED_ROOM = "Shared room";
    
    public static final String NAME = "Name";
    public static final String HOST_NAME = "Host name";
    public static final String NUMBER_OF_REVIEWS = "Number of reviews";
    public static final String PRICE = "Price of property";
    public static final String MIN_NIGHTS = "Minimum number of nights";
    
    private static AirbnbDataLoader loader = new AirbnbDataLoader();
    public static final List<AirbnbListing> ALL_LISTINGS = loader.load();
    
    public Resources(){
        
    }
    
    
}
