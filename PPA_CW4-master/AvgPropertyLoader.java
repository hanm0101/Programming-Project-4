import java.util.*;
/**
 * This class calculates all the data for the average property
 * price per borough chart.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 04-03-2019
 */
public class AvgPropertyLoader
{
    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> dataSet = new ArrayList<>();
    private HashMap<String, Double> avgPropPrice= new HashMap<>();
    private int to;
    private int from;
    /**
     * Construct the hashMap
     */
    public AvgPropertyLoader()
    {
    }

    /**
     * set the maximum to the passed in value
     * @param  to the value the max is set to
     */
    public void setToValue(int max)
    {
        to = max;
    }

    /**
     * set the minimum to the passed in value
     * @param  from the value the min is set to
     */
    public void setFromValue(int min)
    {
        from = min;
    }

    /**
     * Load data from CSV file and store the borough name and
     * the average price per property in a hashmap
     * @param from the minimum prcie
     * @param to the maximum price
     */
    public void createMap(int from, int to)
    {
        dataSet = dataLoader.load(); // store data in an arraylist
        AirbnbListing currentArray;
        for(int i=0;i<dataSet.size();i++){ //iterate through arraylist
            currentArray = dataSet.get(i);// get the array in the arraylist
            if(avgPropPrice.get(currentArray.getNeighbourhood()) == null){
                double value = calculateAvg(currentArray.getNeighbourhood(), from, to); 
                avgPropPrice.put(currentArray.getNeighbourhood(), value); //put average and borough name into hashmap
            }
        }
    }

    /**
     * Caculate the average price to stay the minimum number of nights per borough
     * @param  borough the borough name used to get the the price per night
     * and minimum number of nights
     * @param from the minimum prcie
     * @param to the maximum price
     * @return roundedPrice the average to 2 decimal places
     */
    private double calculateAvg(String borough, int from, int to)
    {
        double avg = 0.0;
        int count = 0;
        AirbnbListing currentArray;
        for(int i=0;i<dataSet.size();i++){ // get the array in the arraylist
            currentArray = dataSet.get(i);// get the array in the arraylist
            if(borough.equals(currentArray.getNeighbourhood()) && currentArray.getPrice()>=from && currentArray.getPrice()<=to){ //matching borough found
                int minNightsPrice = currentArray.getMinimumNights()*currentArray.getPrice(); //the price for the minimum no. of nights
                count ++; //keep track of the number of properties added
                avg += minNightsPrice;
            }
        }
        avg = avg /count; //get average
        double roundedPrice = Math.round(avg * 100.0) / 100.0; //round to 2dp
        return roundedPrice;
    }

    /**
     * Iterate through hashmap and return the average price for the borough searched for
     * @param  boroughName used to find the correct average
     * @return avg the average price
     * @param from the minimum prcie
     * @param to the maximum price
     */
    public double getAvg(String boroughName, int from, int to)
    {
        double avg = 0.0;
        for(Map.Entry<String, Double> entry: avgPropPrice.entrySet()){//iterate through hashmap
            if(entry.getKey().equals(boroughName)){
                avg = entry.getValue();//get the average
            }
        }
        return avg;
    }
}