
import java.util.*;
/**
 * Back end for the BoroughPropertiesWindow
 * Retrieves all the properties in the chosen borough so they can be passed to the BoroughPropertiesWindow to be displayed.
 * Sorts the properties by the chosen field and returns the sorted lists. 
 * Sets and unsets the borough as the favourite
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19
 */
public class BoroughPropertiesEngine
{
     //the criteria the properties should match in order to be displayed
    private String neighbourhood;
    //price range
    private int minPrice;
    private int maxPrice;
    //availability 
    private int numDays;

    private AirbnbDataLoader loader;
    private SortingAlgorithms sorter; 
    //all the properties in the chosen borough
    private List<AirbnbListing> listings;
    private DataAccount dataAccount;
    //user's details
    private static String username; //HOW TO CONNECT
    private static boolean loggedIn; //HOW TO GET THESE VALUES
    private static Input input;
   
     /**
     * Constructor. Loads in the chosen borough
     * @param neighbourhood the chosen borough for which you want to display properties 
     */
    public BoroughPropertiesEngine(String neighbourhood){
        dataAccount = input.getDataAccount();
        username = input.getUsername();
        loggedIn = input.getLoggedIn();
        
        this.neighbourhood = neighbourhood;
        //loader = new AirbnbDataLoader();
        sorter = new SortingAlgorithms();
        listings = new ArrayList<>();
    }
    
    /**
     * Loads all the listings from the Database and filters them.
     * Fills listings with all the properties in the chosen neighbourhood with the chosen price range and avialibity 
     */
    public void matchCriteriaAndLoad(){
        Resources.ALL_LISTINGS.stream()
        .filter(l -> (neighbourhood.equals(l.getNeighbourhood()) && l.getPrice()<= maxPrice && l.getPrice()>= minPrice && l.getAvailability365()>= numDays))
        .forEach(l-> listings.add(l));
    }
    
    /**
     * @return List<AirbnbListing> of properties in the chosen borough
     */
    public List<AirbnbListing> getListings(){
        return listings; 
    }
    
    /**
     * sorts the list of properties by a chosen field
     * @param what field you want to sort the list by 
     * @return List<AirbnbListing> ordered list 
     */
    public List<AirbnbListing> sortBy(String what){
        boolean sortNumbers = true; //default to sort numbers
        boolean sortNumbersAsc = true; //default to sort numbers in ascending order
        HashMap<AirbnbListing, Integer> numbers = new HashMap<>();
        HashMap<AirbnbListing, String> names = new HashMap<>();
        switch(what){
            case Resources.NUMBER_OF_REVIEWS: numbers = byNumberOfReviews(numbers);
            sortNumbersAsc = false; //sort in descending order
            break;
            case Resources.HOST_NAME:
            sortNumbers = false; //we are sorting characters not numbers
            names = byHostName(names);
            break;
            case Resources.PRICE: numbers = byPrice(numbers);
            break;
            case Resources.MIN_NIGHTS: numbers = byMinNights(numbers);
            break;
        }
    
        if(sortNumbers)
            if(sortNumbersAsc){
                return sorter.sortNumbersAsc(numbers.entrySet());
            }
            else{
                return sorter.sortNumbersDesc(numbers.entrySet());
            }
        else
            return sorter.sortAlphabetical(names.entrySet());
    
    }

    /**
     * produces a hashmap of the prices and the property they come from
     * @return HashMap<AirbnbListing, Integer> of properties mapped to their number of reviews
     * @param map an empty HashMap for the method to populate
     */
    private HashMap<AirbnbListing, Integer> byPrice(HashMap<AirbnbListing, Integer> map){
        listings.stream().forEach(l-> map.put(l,l.getPrice()));
        return map;
    }
    
    /**
     * produces a hashmap of the number of reviews and the property apply to
     * @return HashMap<AirbnbListing, Integer> of properties mapped to their number of reviews
     * @param map an empty HashMap for the method to populate
     */
    private HashMap<AirbnbListing, Integer> byNumberOfReviews(HashMap<AirbnbListing, Integer> map){
        listings.stream().forEach(l-> map.put(l,l.getNumberOfReviews()));
        return map;
    }
    
    /**
     * produces a hashmap of the properties and the names of their hosts
     * @return HashMap<AirbnbListing, Integer> of properties mapped to their host's name
     * @param map an empty HashMap for the method to populate
     */
    private HashMap<AirbnbListing, String> byHostName(HashMap<AirbnbListing, String> map){
        listings.stream().forEach(l-> map.put(l, l.getHost_name()));
        return map;
    }

    /**
     * produces a hashmap of the minimum nights and the property apply to
     * @return HashMap<AirbnbListing, Integer> of properties mapped to the minimum number of nights people can stay
     * @param map an empty HashMap for the method to populate
     */
    private HashMap<AirbnbListing, Integer> byMinNights(HashMap<AirbnbListing, Integer> map){
        listings.stream().forEach(l-> map.put(l,l.getMinimumNights()));
        return map;
    }
    
    /**
     * if the borough is not a favourite, update it as a favourite in the database
     * if the borough is already a favourite, update it not as a favourite in the database
     * @return boolean whether borough has been set or unset as a favourite 
     */
    public boolean toggleFavouriteBorough(){
        //if borough is not already favourited, make it a favourite
        if(!isFavoriteBorough()){
            dataAccount.addFavoriteBor(username, neighbourhood);
            //now a favourite
            return true;
        }
        else{ //if it is a favourite, remove it from list of favourites
            dataAccount.removeFavoriteBor(username);
            //now not a favourite
            return false;
        }
        // System.out.println("fave borough");
        // System.out.println(dataAccount.userFavoriteBor(username));
    }
    
    /**
     * @return whether the chosen borough is a favourite or not 
     */
    public boolean isFavoriteBorough(){
        return dataAccount.getFavoriteBorough(username, neighbourhood);
    }
    
    /**
     * sets the chosen borough, price range and availability range so you can extract + display the correct properties
     * @param borough the chosen one 
     */
    public void loadCriteria(String borough, int minPrice, int maxPrice, int numDays){
        //CHANGED THE NAME OF THIS METHOD FROM LOADBOROUGH TO LOADCRITERIA
        neighbourhood = borough;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.numDays = numDays;
    }
    
    
    /**
     * set's the Input class, which contains all of the 
     * data the user chose.
     * @param input an instance of input
     */
    public static void setInput(Input newInput){
        input = newInput;
    }
    
    /**
     * set's the Input class, which contains all of the 
     * data the user chose.
     * @param input an instance of input
     */
    public static Input getInput(){
        return input ;
    }
    
}
