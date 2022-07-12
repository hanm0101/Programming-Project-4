import java.util.*;
/**
 * This class is the backend for the
 * SearchBar class. It finds the matching data
 * based on the search. It also reorders the results for the
 * filters.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class SearchBarLoader
{
    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertySet = new ArrayList<>();
    private List<AirbnbListing> filteredProperties = new ArrayList<>();
    /**
     * Constructor for objects of class SearchBarLoader
     */
    public SearchBarLoader()
    {
    }

    /**
     * Load data from CSV file and store a total of how many properties
     * there are available for each borough.
     */
    public void getProperties(String keywords, String borough)
    {
        propertySet = dataLoader.load(); // store data in an arraylist
        AirbnbListing currentArray;
        filteredProperties.clear();
        for(int i=0;i<propertySet.size();i++){ //iterate through arraylist
            currentArray = propertySet.get(i);// get the array in the arraylist
            if(borough == null &&  keywords.length() >0){
                searchByKeyword(keywords, currentArray);
            }
            else if(borough != null &&  keywords.length() == 0){
                searchByBorough(borough, currentArray);
            }
            else{
                searchByBoth(keywords, borough, currentArray);
            }
        }
    }

    /**
     * Search through the CSV file to find properties
     * that contain the matching keyword. Store them 
     * in an arraylist
     * @param  keywords the keywords to search for
     * @param currentArray the current property to check
     */
    private void searchByKeyword(String keywords, AirbnbListing currentArray)
    {
        String propertyName = currentArray.getName();
        if(propertyName.toLowerCase().contains(keywords)){//find matching properties
            filteredProperties.add(currentArray);
        }
    }

    /**
     * Search through the CSV file to find properties
     * that contain the matching borough. Store them 
     * in an arraylist
     * @param  borough the borough to search for
     * @param currentArray the current property to check
     */
    private void searchByBorough(String borough, AirbnbListing currentArray)
    {
        String propertyBorough = currentArray.getNeighbourhood();
        if(propertyBorough.toLowerCase().contains(borough.toLowerCase())){//find matching boroughs
            filteredProperties.add(currentArray);
        }
    }

    /**
     * Search through the CSV file to find properties
     * that contain the matching keyword. Store them 
     * in an arraylist
     * @param  keywords the keywords to search for
     * @param currentArray the current property to check
     */
    private void searchByBoth(String keywords, String borough, AirbnbListing currentArray)
    {
        String propertyName = currentArray.getName();//get property name
        String propertyBorough = currentArray.getNeighbourhood();//get property borough
        if(propertyName.toLowerCase().contains(keywords) && propertyBorough.toLowerCase().contains(borough.toLowerCase())){
            filteredProperties.add(currentArray);//get properties with matching borough and keyword
        }
    }

    /**
     * return the list of matching properties
     * @return  filteredProperties the arraylist of matching properties
     */
    public List<AirbnbListing> getPropertyList()
    {
        return filteredProperties;
    }

    /**
     * Reorder the list of matching properties into descending order based on price.
     *Code help from:https://stackoverflow.com/questions/24943329/treemap-high-low-key-integer-sort - Jigar Joshi july 14 2014
     * @param  matchingProp the list of matching properties
     * @return orderedProp the ordered arraylist
     */
    public List<AirbnbListing> descendingOrder(List<AirbnbListing> matchingProp)
    {
        Map<Integer, AirbnbListing> propPricing = new TreeMap<Integer, AirbnbListing>(Collections.reverseOrder());//reoder into descending order
        return orderMap(propPricing, matchingProp);
    }

    /**
     * Reorder the list of matching properties into ascending order based on price.
     * @param  matchingProp the list of matching properties
     * @return orderedProp the ordered arraylist
     */
    public List<AirbnbListing> ascendingOrder(List<AirbnbListing> matchingProp)
    {
        Map<Integer, AirbnbListing> propPricing = new TreeMap<>();
        return orderMap(propPricing, matchingProp);
    }

    /**
     * Orders the properties by iterating through the matching 
     * property arraylist and storing the price with the property as
     * a hashmap. Then store the keys in a set and get the values which are
     * the properties from the hashmap.
     * @param  matchingProp the list of matching properties
     * @return orderedProp the ordered arraylist
     */
    private List<AirbnbListing> orderMap(Map<Integer, AirbnbListing> propPricing, List<AirbnbListing> matchingProp)
    {
        List<AirbnbListing> orderedProp = new ArrayList<>();
        for(int i=0; i<matchingProp.size();i++){//iterate through treemap
            AirbnbListing currentArray = matchingProp.get(i);//when matching propery found add it to the arraylist
            propPricing.put(currentArray.getPrice(), currentArray);//store price and airbnbListing property
        }
        Set<Integer> keys = propPricing.keySet();//store property prices into a set
        for (Iterator i = keys.iterator(); i.hasNext();) {//iterate through the prices 
            Integer key = (Integer) i.next();
            AirbnbListing value = propPricing.get(key);//get the value which is the property and add it to the orderedProp list
            orderedProp.add(value);
        }
        return orderedProp;
    }
}
