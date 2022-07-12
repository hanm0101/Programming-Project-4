import java.util.*;
/**
 *  Backend for the PropertyTypesBarChart graph
 *  For all properties within the price range, sorts into their different boroughs
 *  and room types.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 29.3.19
 */
public class PropertyTypesBarChartLoader
{
    //for each borough, stores the number of properties of private rooms there are
    HashMap<String, Integer> privateRoomCount;
    //for each borough, stores the # of properties of shared room type
    HashMap<String, Integer> sharedRoomCount;
    //for each borough, stores the # of properties of entire home/apt type
    HashMap<String, Integer> entireHomeCount;
    //price range
    private static int max =1000;//default values
    private static int min = 100;//default values
    /**
     * constructor. initialises all the hashmaps to stores counters
     * also calculates the series data to be used in the graph
     */
    public PropertyTypesBarChartLoader(){
        //get ready to count the different room types for each borough
        privateRoomCount = new HashMap<>();
        sharedRoomCount = new HashMap<>();
        entireHomeCount = new HashMap<>();
        calculateSeriesData();
    }

    /**
     * Counts how many of each room type for each borough
     * Stores the results in hashmaps mapping borough to count of properties
     */
    public void calculateSeriesData(){
        //put all boroughs in each counter hashmap and initialise all counters to 0
        for(String borough : Resources.ALL_BOROUGHS){
            privateRoomCount.put(borough, 0);
            sharedRoomCount.put(borough, 0);
            entireHomeCount.put(borough,0);
        }
        //for each property, increment counter for correct borough and room type
        for(AirbnbListing listing : Resources.ALL_LISTINGS){
            String borough = listing.getNeighbourhood();
            int price = listing.getPrice();
            if(price >=min && price<= max){
                switch(listing.getRoom_type()){
                    //increment the int in the hashmaps -> store the number of each room type in each borough
                    case Resources.PRIVATE_ROOM :
                    privateRoomCount.replace(borough, privateRoomCount.get(borough)+1);
                    break;
                    case Resources.SHARED_ROOM :
                    sharedRoomCount.replace(borough, sharedRoomCount.get(borough)+1);
                    break;
                    case Resources.ENTIRE_HOME :
                    entireHomeCount.replace(borough, entireHomeCount.get(borough)+1);
                    break;
                }
            }
        }
        //TEST
        // for(String borough : Resources.ALL_BOROUGHS){
            // System.out.println(borough + "private: "+privateRoomCount.get(borough)+ "shared :"+sharedRoomCount.get(borough) +
            // "entire: "+entireHomeCount.get(borough));
        // }
    }

    /**
     * to pass in the upper bound of the price range
     * @param to the upper bound
     */
    public static void setToValue(int to){
        max = to;
    }

    /**
     * to pass in the lower bound of the price range
     * @param from the lower bound
     */
    public static void setFromValue(int from){
        min = from;
    }

    /**
     * @return privateRoomCount for each borough, the # of properties of room type private room
     */
    public HashMap<String, Integer> getPrivateRoomCount(){
        return privateRoomCount;
    }

    /**
     * @return sharedRoomCount for each borough, the # of properties of room type shared room
     */
    public HashMap<String, Integer> getSharedRoomCount(){
        return sharedRoomCount;
    }

    /**
     * @return entireHomeCount for each borough, the # of properties of room type entire home/apt
     */
    public HashMap<String, Integer> getEntireHomeCount(){
        return entireHomeCount;
    }

}