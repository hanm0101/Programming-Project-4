import java.util.*;

/**
 * Generates a list of recommended properties based on the following:
 * the user's favourite boroughs,
 * the user's most frequently searched price range/s
 * the user's most frequently viewed room type
 * the minimum nights of the most frequently viewed properties
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Recommendations
{
    private AirbnbDataLoader loader;
    private HashMap<AirbnbListing, Double> shortlist;
    private DataAccount dataAccount;
    private StatData statData; 
    private SortingAlgorithms sorter;
    //all the data required to sort the recommended properties 
    private List<AirbnbListing> favoriteProperties;
    private AirbnbListing mostViewedProperty;
    private String favoriteBorough;
    private String mostViewedBorough;
    private List<Integer[]> searchedPriceRanges;
    //stores the short listed properties and their scores
    private HashMap<AirbnbListing, Double> recommended;
    private static String username = "Ariane";
    private static boolean loggedIn = true;
    private final String PRIVATE_ROOM = "Private room"; 
    private final String ENTIRE_HOME = "Entire home/apt";
    private final String SHARED_ROOM = "Shared room";
     
    public Recommendations(){
        dataAccount = new DataAccount();
        statData = new StatData();
        shortlist = new HashMap<>();
        loader = new AirbnbDataLoader();
        sorter = new SortingAlgorithms();
        //retrieve data required to sort recommended properties
        mostViewedBorough = statData.getMostViewedB();
        favoriteBorough = dataAccount.userFavoriteBor(username);
        
        mostViewedProperty = statData.getMostViewedP();
        favoriteProperties = dataAccount.userFavoriteProps(username);
        
        searchedPriceRanges = dataAccount.getPriceRange(username);
        //WHAT IF THE FAVOURITE/ MOST VIEWED PROPERTIES/ BOROUGHS ARE 
        //UPDATED WHILE IN THIS WINDOW -> REFRESH BUTTON? OR CHANGE DYNAMICALLY?
        getRecommendations();
    }
    
    public List<AirbnbListing> getRecommendations(){
        List sortedShortlist = filterProperties();
        List<AirbnbListing> finalRecommendations = new ArrayList<>();
        //only display the recommendations with the top 10 highest scores
        for(int i = 0; i<10; i++){
            AirbnbListing chosen = (AirbnbListing) sortedShortlist.get(i);
            //add to list of final recommendations
            finalRecommendations.add(chosen);
            //System.out.println(chosen.getName() + ", "+chosen.getNeighbourhood() + ", "+chosen.getPrice() + " "+chosen.getRoom_type()
            //+chosen.getAvailability365());
        }
        
        
        return finalRecommendations;
    }
    
    /**
     * @param needMoreRecommendations if there are less than 10 recommendations after the first filtering process,
     * input true, else needMoreRecommendations = false
     */
    private List<AirbnbListing> filterProperties(){
        //get all properties in the airbnb database
        List<AirbnbListing> allListings = loader.load();
        for(AirbnbListing listing : allListings){
            shortlist.put(listing, 1.0);
           
        }
        //remove favourites and most viewed 
        for(AirbnbListing fave : favoriteProperties){
            shortlist.remove(fave);
        }
        shortlist.remove(mostViewedProperty);
        boroughFilter(); //working
        priceRangeFilter();
        roomTypeFilter();
        availabilityFilter();
        //sort the shortlist in ascending order (highest score at array end)
        
        for(int i=0; i<10; i++){
            AirbnbListing l =sorter.sortDoubleDesc(shortlist.entrySet()).get(i);
        }
        return sorter.sortDoubleDesc(shortlist.entrySet());
    }
    
    /**
     * If a property is not in one of the following, they will be shortlist out from the list of recommended properties:
     * 1. favourite borough 2.most viewed borough 3.searched borough 4. adjacent boroughs to the favourite/ most viewed 
     * if property is in favourite borough -> score multiplier = 1.0
     * " in most viewed borough or searched borough -> multiplier = 0.7
     * " in adjacent borough -> multiplier = 0.3
     */
    private void boroughFilter(){
        
        for(AirbnbListing property : shortlist.keySet()){
            String borough = property.getNeighbourhood();
            //if borough is the favourite borough, multiply score
            if(favoriteBorough !=null){
                if(borough.equals(favoriteBorough)){
                    shortlist.put(property, shortlist.get(property)*1.7);
                }
            }
            //if borough matches most viewed, multiply score
            if(borough.equals(mostViewedBorough)){
                shortlist.put(property, shortlist.get(property)*1.6);
            }
            //ADD SEARCH
             // List<String> adjacentBoroughs = findAdjacentBoroughs();
            // if(adjacentBoroughs !=null){
                // for(String adjacent : adjacentBoroughs){
                    // if(borough.equals(adjacent)){
                        // shortlist.put(property, shortlist.get(property)*0.3);
                    // }
                // }
            // }
        }
        
        //TESTING
        // for(AirbnbListing property : shortlist.keySet()){
            // if(property.getNeighbourhood().equals(favoriteBorough))
            // System.out.println(property.getNeighbourhood()+ shortlist.get(property));
            // if(property.getNeighbourhood().equals(mostViewedBorough))
            // System.out.println(property.getNeighbourhood()+ shortlist.get(property));
        // }
         //System.out.println("fave borough" +favoriteBorough);
        //System.out.println("most viewed borough" +mostViewedBorough);
    }
        
    // private List<String> findAdjacentBoroughs(){
        
    // }
    
    private void priceRangeFilter(){
        int[] range = getRecPriceRange();
        int lbAvg = range[0];
        int ubAvg = range[1];
        //if a property in shortlist has a price within this averaged price
        //range, multiply its score
        for(AirbnbListing property : shortlist.keySet()){
            int price = property.getPrice();
            if(lbAvg<= price && price <= ubAvg){
                shortlist.replace(property, shortlist.get(property)*1.6);
            }
        }
    }
        
    private int[] getRecPriceRange(){
         List<Integer[]> priceRanges = dataAccount.getPriceRange(username);
        //add up all the lower bounds and upper bounds respectively
        int lbAvg = 0;
        int ubAvg = 0;
        for(Integer[] range : priceRanges){
            lbAvg += range[0];
            ubAvg += range[1];
        }
        //now find the average lower bound and upper bound
        lbAvg = lbAvg/priceRanges.size();
        ubAvg = ubAvg/priceRanges.size();
        int[] range = {lbAvg, ubAvg};
        return range;
    }
        
        /**
         * each room type counter receives
         * 3 counts if present in favourite properties
         * 2 count if present in mostViewedProperties
         * 1 count if present in any other property in shortlist 
         * why? room types in favourites and in search results are more
         * likely to be prefered by the user
         */
        private void roomTypeFilter(){
        List<String> rtRanking = getRoomTypePopularityRanking();  
        double[] scoreMultipliers = new double[getRoomTypePopularityRanking().size()];
        //the most popular room type (rank1 / index 0) gets the highest multiplier score
        for(int i=0; i<scoreMultipliers.length; i++){
            scoreMultipliers[i] = (1+1/(i+2));
        }
        
            for(AirbnbListing listing : shortlist.keySet()){
            String roomtype = listing.getRoom_type();
            for(int i = 0; i<rtRanking.size(); i++){
                if(roomtype.equals(rtRanking.get(i))){
                    //multiply property's score according to the popularity ranking of its room type
                    shortlist.replace(listing, shortlist.get(listing)*scoreMultipliers[i]);
                }
            }
        }
    }
    
    private List<String> getRoomTypePopularityRanking(){
        int privateCounter = 0;
        int sharedCounter = 0;
        int entireCounter = 0;
        //check the room types of the favourite properties
        if(favoriteProperties != null){
            for(AirbnbListing listing : favoriteProperties){
                String roomType = listing.getRoom_type();
                switch(roomType){
                    case PRIVATE_ROOM : privateCounter+=3;
                    break;
                    case SHARED_ROOM : sharedCounter+=3;
                    break;
                    case ENTIRE_HOME : entireCounter +=3;
                    break;
                }
            }
        }
        //get the most viewed room type for most viewed property
        String mvRoomType = mostViewedProperty.getRoom_type();
        switch(mvRoomType){
            case PRIVATE_ROOM : privateCounter+=2;
            break;
            case SHARED_ROOM : sharedCounter+=2;
            break;
            case ENTIRE_HOME : entireCounter +=2;
            break;
        }
        
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("private", privateCounter);
        hm.put("shared", sharedCounter);
        hm.put("entire", entireCounter);
        //sorted in descending order
        List<String> sortedList = new ArrayList<>();
        hm.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .forEach(e -> sortedList.add(e.getKey()));
        
        return sortedList;
    }
        
    private void availabilityFilter(){
        int[] range = getDesiredAvailabilityRange();
        //increase scores of properties with availabilities in the middle range
        for(AirbnbListing listing : shortlist.keySet()){
            int availability = listing.getAvailability365();
            if(availability>= range[0] && availability<= range[1]){
                shortlist.replace(listing, shortlist.get(listing)*1.5);
            }
        }
    }
        
    private int[] getDesiredAvailabilityRange(){
        if(favoriteProperties==null){ //no particularly special range of availabilities required
            return null;
        }
        //get an arraylist of all the availibity data from favorite properties
        ArrayList<Integer> availabilities = new ArrayList<>();
        for(AirbnbListing fave : favoriteProperties){
            availabilities.add(fave.getAvailability365());
        }
        //get the middle 30%
        int[] av = new int[availabilities.size()];
        for(int i=0; i<availabilities.size(); i++){
            av[i] = availabilities.get(i);
        }
        Arrays.sort(av);
        double lowestAv = av[0];
        double highestAv = av[av.length -1];
        double range = highestAv - lowestAv;
        double median = (highestAv + lowestAv)/2;
        double hundredth = range/10;
        //size of range in which availability should lie
        double desiredRangePerc = 0.3;
        double lowerbound = median - (hundredth* desiredRangePerc/2);
        double upperbound = median + (hundredth* desiredRangePerc/2);
        int[] desiredRange = {(int)lowerbound, (int)upperbound};
        
        return desiredRange;
    }
    
    public static void setUserName(String newUsername){
        username = newUsername;
    }
    
    public static void setLoggedIn(boolean isLoggedIn){
        loggedIn = isLoggedIn;
    }
}

