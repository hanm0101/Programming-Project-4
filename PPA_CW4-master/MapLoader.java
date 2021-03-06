import java.util.*;

/**
 * The MapLoader does all the backend of the MapWindow
 * class. It gets the data from the Airbnb class and returns ot
 * back to the MapWindow to display.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class MapLoader
{
    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> set = new ArrayList<>();
    private HashMap<String, Integer> propertiesCount = new HashMap<>();
    private HashMap<String, String> boroughNames = new HashMap<>();
    /**
     *  
     */
    public MapLoader()
    {
        set = dataLoader.load(); // store data in an arraylist ??????????????
    }

    /**
     * Load data from CSV file and initally set how many properties
     * there are available for each borough to zero.
     */
    public void createProperties()
    {
        AirbnbListing currentArray;
        for(int i=0;i<set.size();i++){ //iterate through arraylist
            currentArray = set.get(i);// get the array in the arraylist
            propertiesCount.put(currentArray.getNeighbourhood(), 0); // store borough and count=0 in hashmap
        }
    }

    /**
     * Check if the map will be displayed by checking
     * if the date range and price range are valid
     *
     * @param  min stores the min price
     * @param max stores the max price
     * @param noOfDays no of days selected
     * @return search true if the map can be displayed
     */
    public boolean canSearch(int min, int max, long noOfDays)
    {
        boolean search;
        if(noOfDays == 0 ||  max == 0){
            search = false;
        }
        else{
            search = true;
        }
        return search;
    }

    /**
     * Load data from CSV file and store a total of how many properties
     * there are available for each borough which meet the date range and price
     * requirements.
     * @param  min stores the min price
     * @param max stores the max price
     * @param noOfDays no of days selected
     */
    public void countProperties(int min, int max, long noOfDays)
    {
        AirbnbListing currentArray;
        createProperties();
        for(int i=0;i<set.size();i++){ //iterate through arraylist
            currentArray = set.get(i);// get the array in the arraylist
            String neighbourhood = currentArray.getNeighbourhood(); // get the neighbourood of the current property from the array
            for(Map.Entry<String, Integer> entry: propertiesCount.entrySet()){
                if(entry.getKey().toString().contains(neighbourhood) && currentArray.getPrice()>=min && currentArray.getPrice()<=max
                && currentArray.getAvailability365()>= noOfDays && currentArray.getMinimumNights() <= noOfDays){ //find boroughs in the price and date range
                    Integer value = entry.getValue();
                    value ++;//increment the index of the property
                    propertiesCount.put(entry.getKey(), value);
                }
            }
        }
    }

    /**
     * Find and return the borough's full name
     * from being given the shortened name.
     * @param btnName the shorted name of the borough
     * @return the boroughs's full name
     */
    public String findBorough(String btnName)
    {
        String borough = "";
        for(Map.Entry<String, String> entry: boroughNames.entrySet()){ //iterate through treemap
            if(entry.getValue().equals(btnName)){
                borough = entry.getKey();//get the full name of the borough
            }
        }
        return borough;
    }

    /**
     * Find and return the borough's count from being given the shortened name.
     * @param btnName the shorted name of the borough
     * @return the boroughs's count
     */
    public int getCount(String btnName)
    {
        int count = 0;
        String location = findBorough(btnName);
        for(Map.Entry<String, Integer> entry: propertiesCount.entrySet()){  //iterate through hashmap
            if (entry.getKey().equals(location)){ //get count using the borough string value
                count = entry.getValue();
            }
        }
        return count;
    }

    /**
     * Store every borough's name and its corresponding shortened name in a hashmap
     *
     */
    public void createBoroughsMap()
    {
        boroughNames.put("Greenwich","GWCH" );
        boroughNames.put("Hackney", "HACK");
        boroughNames.put("Hammersmith and Fulham","HAMM") ;
        boroughNames.put("Islington", "ISLI");
        boroughNames.put("Kensington and Chelsea","KENS");
        boroughNames.put("Lambeth","LAMB");
        boroughNames.put("Lewisham", "LEWS");
        boroughNames.put("Southwark", "STHW");
        boroughNames.put("Tower Hamlets", "TOWH");
        boroughNames.put("Wandsworth","WAND");
        boroughNames.put("Westminster", "WSTM");
        boroughNames.put("Barking and Dagenham", "BARK");
        boroughNames.put("Barnet","BARN");
        boroughNames.put("Bexley","BEXL");
        boroughNames.put("Brent", "BREN");
        boroughNames.put("Bromley", "BROM");
        boroughNames.put("Croydon", "CROY");
        boroughNames.put("Ealing", "EALI");
        boroughNames.put("Enfield", "ENFI");
        boroughNames.put("Haringey", "HRGY");
        boroughNames.put("Harrow", "HRRW");
        boroughNames.put("Havering", "HAVE");
        boroughNames.put("Hillingdon", "HILL");
        boroughNames.put("Hounslow", "HOUN");
        boroughNames.put("Kingston upon Thames", "KING");
        boroughNames.put("Merton","MERT");
        boroughNames.put("Newham", "NEWH");
        boroughNames.put("Redbridge", "REDB");
        boroughNames.put("Richmond upon Thames", "RICH");
        boroughNames.put("Sutton","SUTT");
        boroughNames.put("Camden", "CAMD");
        boroughNames.put("Waltham Forest", "WALT");
        boroughNames.put("City of London", "CITY");
    }
}
