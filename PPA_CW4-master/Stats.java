import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Stack;
import java.lang.*;

/**
 * Stats stores the backend information and calculations on statistics for Stat Window.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29-03-2019
 */
public class Stats
{
    // instance variables - replace the example below with your own
    private AirbnbDataLoader dataLoader;
    private ArrayList<AirbnbListing> listing ;
    private ArrayList<String> stat; //array list of the titles
    private ArrayList<String> allStats;
    private ArrayList<String> shownStats;
    private ArrayList<String> availableStats;
    private HashMap<String, String> stats; //hashmap of titles and values
    private StatData statData;
    /**
     * Constructor for objects of class Stats
     */
    public Stats(StatData statData)
    {
        dataLoader = new AirbnbDataLoader(); //loads Airbnb Loader
        stats = new HashMap<String, String>(); //hashmap for the titles and the values of statistics
        allStats = new ArrayList<>(); //arraylist of all the values of statistics
        shownStats = new ArrayList<>(); //arraylist of the values of statistics shown in the grid
        availableStats = new ArrayList<>(); //arraylist of the values not shown
        this.statData = statData; //initialize stat data class
        listing = dataLoader.load();
    }

    /**
     * Return hashmap stats which contains the added values
     * @return hashmap of the statictics with titles and values.
     */
    public HashMap<String, String> getStats(){
        return stats;
    }

    /**
     * Returns every statistic with a title and value concatenated
     * @return concatenated statistics with titles and values
     */
    public String getStatsEntry(){
        Set<Map.Entry<String,String>> entries = getStats().entrySet();
        String entryValue = " ";
        for (Map.Entry<String,String> entry : entries){
            entryValue += entry.getKey() + entry.getValue();
        }
        return entryValue;
    }

    /**
     * Returns array list availableStats which has stats not shown
     * @return array list availableStats
     */
    public ArrayList<String> getAvailable(){
        return availableStats;
    }

    /**
     * Add values to stat array list for all the titles of stats
     * Used for access on hashmap stats
     */
    public void addStat(){
        stat = new ArrayList<>();
        stat.add("Average number of reviews");
        stat.add("Number of available properties");
        stat.add("Number of homes and appartments");
        stat.add("Most expensive borough");
        stat.add("Shortest distance to the closest property");
        stat.add("Most favorited property");
        stat.add("Most favorited borough");
        stat.add("Least favourited borough");
    }

    /**
     * Return a string for the title of the stats from stat array
     * @return statistic title
     * @param index index of the array list stat
     */
    public String statName(int index){
        return stat.get(index);
    }

    /**
     * Return a string for the value of the stats from the hashmap stats
     * @return stat value from the hashmap stats
     * @param indexNumber index of the array list stat
     */
    public String returnStatName(int indexNumber){
        return stats.get(statName(indexNumber));
    }

    /**
     * Return both the title and the statistic
     * @return concatenated title and value
     * @param ind index of the array list stat
     */
    public String returnStatLabel(int ind){
        String concat = statName(ind) + "  " + returnStatName(ind);
        return concat;
    }

    /**
     * Add statistics to allStats array list to store all stats
     */
    public void addValuesArray(){
        String concatValues;
        for (int i=0; i < 8; i++){
            concatValues = returnStatLabel(i);
            allStats.add(concatValues);
        }
    }

    /**
     * Initialize shownStats array list
     */
    public void addFirstFour(){
        shownStats.add(returnStatLabel(0));
        shownStats.add(returnStatLabel(1));
        shownStats.add(returnStatLabel(2));
        shownStats.add(returnStatLabel(3));
    }

    /**
     * Join two array lists allStats and shownStats by subtracting values shownStats from allStats
     * Add values to availableStats which stores the stats that aren't shown
     */
    public void disjoinAL(){
        allStats.removeAll(shownStats); //remove the values in shownStats from allStats

        for (String value: allStats){
            availableStats.add(value); //add values to availableStats (all - shown)
        }
        for (String stat : shownStats){
            allStats.add(stat); //put the removed values back to allStats to keep all the stats
        }
    }

    /**
     * Return a value from availableStats
     * @return a value from availableStats
     * @param first index of availableStats
     */
    public String getAvailable(int first){
        return availableStats.get(first);
    }

    /**
     * Remove a string from the array list availableStats
     * @param shown String of stat title
     */
    public void removeAvailable(String shown){
        availableStats.remove(shown);
    }

    /**
     * Add a string value to availableStats
     * @param hideValue removed value of stats to add to available list
     */
    public void addAvailable(String hideValue){
        availableStats.add(hideValue);
    }

    /**
     * Add a string value to the front of the availableStat array list
     * @param hideValue string of the shown value
     */
    public void addToFirstPositionAvailable(String hideValue){
        availableStats.add(0, hideValue);
    }

    /**
     * Calculates the average number of reviews per properties.
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void averageReviews(int from, int to){
        Double propertiesReview = 0.0;
        Iterator<AirbnbListing> listingIterator = listing.iterator();
        while (listingIterator.hasNext()) {
            if (listingIterator.next().getPrice() >= from && listingIterator.next().getPrice() <= to){
                propertiesReview += listingIterator.next().getNumberOfReviews();
            }
        }

        Double averageReviews = propertiesReview /listing.size();

        DecimalFormat df = new DecimalFormat("#.##");
        stats.put("Average number of reviews", df.format(averageReviews));
    }

    /**
     * Calculates the number of available properties.
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void availableProperties(int from, int to){
        int availableProp = 0;
        Iterator<AirbnbListing> listingIterator = listing.iterator();
        while (listingIterator.hasNext()) {
            AirbnbListing nextProperty = listingIterator.next();
            if(nextProperty.getAvailability365() != 0 &&  nextProperty.getPrice() >= from && nextProperty.getPrice() <= to){
                availableProp++;
            }
        }

        stats.put("Number of available properties", Integer.toString(availableProp));
    }

    /**
     * Calculates the number of home and apartments.
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void homesAndAppartments(int from, int to){
        int homesAndAppartments = 0;
        Iterator<AirbnbListing> listingIterator = listing.iterator();
        while (listingIterator.hasNext()) {
            if(!listingIterator.next().getRoom_type().equals("Private room") && listingIterator.next().getPrice() >= from && listingIterator.next().getPrice() <= to){
                homesAndAppartments++;
            }
        }

        stats.put("Number of homes and appartments", Integer.toString(homesAndAppartments));
    }

    /**
     * Find the most expensive borough.
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void mostExpensiveBorough(int from, int to){
        AirbnbListing airbnb = listing.get(0);
        Double price = airbnb.getPrice()*(double)airbnb.getMinimumNights();
        for(int i = 1; i < listing.size(); i++){
            AirbnbListing tempAirbnb = listing.get(i);
            if (tempAirbnb.getPrice() >= from && tempAirbnb.getPrice() <= to){
                Double tempPrice = tempAirbnb.getPrice()*(double)tempAirbnb.getMinimumNights();
                if(tempPrice > price){
                    airbnb = tempAirbnb;
                    price = tempPrice;
                }
            }
        }

        String borough = airbnb.getNeighbourhood();
        stats.put("Most expensive borough", borough);
    }

    /**
     * Find the shortest distance to a property from Bush House
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void findDistance(int from, int to){
         double curLatitude = 51.512592;
         double curLongitude = -0.116500;
         int radius = 6371;
         AirbnbListing airbnb = listing.get(0);
         double dlon =  (curLongitude - airbnb.getLongitude());
         double dlat =  (curLatitude - airbnb.getLatitude());
         double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(airbnb.getLongitude())* Math.cos(curLongitude)*Math.pow(Math.sin(dlon/2),2);
         double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a));
         double distance = radius * c;for(int i = 1; i < listing.size(); i++){
             AirbnbListing tempAirbnb = listing.get(i);
              if (tempAirbnb.getPrice() >= from && tempAirbnb.getPrice() <= to){
                  dlon =  (curLongitude - tempAirbnb.getLongitude());
                  dlat =  (curLatitude - tempAirbnb.getLatitude());
                  a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(tempAirbnb.getLongitude())* Math.cos(curLongitude)*Math.pow(Math.sin(dlon/2),2);
                  c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a));
                  double tempDistance = radius * c;
                  if(distance > tempDistance){
                      airbnb =  tempAirbnb;
                      distance = tempDistance;
                  }
              }
         }
         DecimalFormat df = new DecimalFormat("#.##");
         stats.put("Shortest distance to the closest property", df.format(distance));
    }

    /**
     * Stores the most favourited property in stats hashmap
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void mostFavouritedProperty(int from, int to){
        stats.put("Most favorited property", statData.getStatBestP(from, to).getName());
    }

    /**
     * Stores the most favourited borough in stats hashmap
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void bestBorough(int from, int to){
        stats.put("Most favorited borough", statData.getStatBestB(from, to));
    }

    /**
     * Stores the least favourited borough in stats hashmap
     * @param from minimum price user input
     * @param to maximum price user input
     */
    public void worstBorough(int from, int to){
        stats.put("Least favourited borough", statData.getStatWorstB(from, to));
    }
}
