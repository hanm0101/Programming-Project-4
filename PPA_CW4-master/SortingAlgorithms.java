import java.util.*;
/**
 * Write a description of class SortingAlgorithms here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SortingAlgorithms
{
    /**
     * taken from this website:
     * https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
     */
    public List<AirbnbListing> sortNumbersAsc(Set<Map.Entry<AirbnbListing,Integer>> set){
        //results stored in this list
        List<AirbnbListing> sortedList = new ArrayList<>();
        set.stream().sorted(Map.Entry.comparingByValue())
        .forEach(e -> sortedList.add(e.getKey()));

        return sortedList;
    }

    /**
     * taken from this website:
     * https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
     */
    public List<AirbnbListing> sortNumbersDesc(Set<Map.Entry<AirbnbListing,Integer>> set){
        List<AirbnbListing> sortedList = new ArrayList<>();
        set.stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .forEach(e -> sortedList.add(e.getKey()));

        return sortedList;
    }

    /**
     * taken from this website:
     * https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
     */
    public List<AirbnbListing> sortDoubleDesc(Set<Map.Entry<AirbnbListing,Double>> set){
        List<AirbnbListing> sortedList = new ArrayList<>();
        set.stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .forEach(e -> sortedList.add(e.getKey()));

        return sortedList;
    }
    
    /**
     * taken from this website:
     * https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
     */
    public List<AirbnbListing> sortAlphabetical(Set<Map.Entry<AirbnbListing,String>> set){
        List<AirbnbListing> sortedList = new ArrayList<>();
        set.stream().sorted(Map.Entry.comparingByValue())
        .forEach(e -> sortedList.add(e.getKey()));
        // for(AirbnbListing listing : sortedList){
        // System.out.println(listing.getHost_name());
        // }
        return sortedList;
    }
}
