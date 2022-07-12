
/**
 * Write a description of class Input here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Input
{
    private String loggedUser;
    private boolean loggedIn;
    private boolean valid = false;
    private Integer from ;
    private Integer to ;
    private Integer numDays ;
    private Integer statFrom;
    private Integer statTo ;
    private DataAccount dataAccount;
    private StatData statData;
    /**
     * Constructor for objects of class Input
     */
    public Input()
    {
        
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setValid(boolean newValid)
    {
        valid = newValid;
    }

    public boolean getValid(){
        return valid;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setToValue(int newto)
    {
        to = newto;
    }
    
    public Integer getToValue(){
        return to;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setFromValue(int newfrom)
    {
        from = newfrom;
    }
    
    public Integer getFromValue(){
        return from;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setStatToValue(int newto)
    {
        statTo = newto;
    }
    
    public Integer getStatToValue(){
        return statTo;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setStatFromValue(int newfrom)
    {
        statFrom = newfrom;
    }
    
    public Integer getStatFromValue(){
        return statFrom;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setNumDays(int num)
    {
        numDays = num;
    }
    
    public Integer getNumDays(){
        return numDays;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setLoggedIn(boolean logged)
    {
        loggedIn = logged;
    }

    public boolean getLoggedIn(){
        return loggedIn;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setUsername(String newusername)
    {
        loggedUser = newusername;
    }
    
    public String getUsername(){
        return loggedUser;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setDataAccount(DataAccount dataAccount)
    {
        this.dataAccount = dataAccount;
    }
    
    public DataAccount getDataAccount(){
        return dataAccount;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setStatData(StatData statData)
    {
        this.statData = statData;
    }
    
    public StatData getStatData(){
        return statData;
    }
}
