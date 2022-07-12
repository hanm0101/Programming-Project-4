

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class DataAccountTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DataAccountTest
{
    public DataAccount dataAccount = new DataAccount();
    /**
     * Default constructor for test class DataAccountTest
     */
    public DataAccountTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        
    }
    
    /**
     * Checks if the account is already in
     * the database with a username that's not
     * in the database.
     */
    @Test
    public void accountExistTest1()
    {
        final String username = "Anne";
        
        assertEquals(false, dataAccount.checkUserExist(username));
    }

    /**
     * Checks if the account is already in
     * the database with a username that's not
     * in the database.
     */
    @Test
    public void accountExistTest2()
    {
        final String username = "Ariane";
        
        assertEquals(true, dataAccount.checkUserExist(username));
    }
    
     /**
     * Checks if we can create an account with a
     * username that's not in the database.
     * 
     * After adding an account with that
     * username, we check if that user 
     * has been added.
     */
    @Test
    public void accountCreateTest1()
    {
        final String username = "Anne";
        
        assertEquals(true, dataAccount.createAnAccount(username, "AZERTY"));
        assertEquals(true, dataAccount.checkUserExist(username));
        
        dataAccount.deleteAnAccount(username);
    }

    /**
     * Checks if we can create an account with
     * a username that's already in the database.
     */
    @Test
    public void accountCreateTest2()
    {
        final String username = "Ariane";
        
        assertEquals(false, dataAccount.createAnAccount(username, "AZERTY"));
    }
    
    /**
     * Checks if we can delete an account.
     */
    @Test
    public void accountDeleteTest()
    {
        final String username = "Anne";
        dataAccount.createAnAccount(username, "AZERTY");
        
        assertEquals(true,dataAccount.checkUserExist(username));
        
        assertEquals(true,dataAccount.deleteAnAccount(username));
        
        assertEquals(false ,dataAccount.checkUserExist(username));
    }
    
    /**
     * Checks if a property is one of the users
     * favorite with a property that is not
     * one of their favorites.
     */ 
    @Test
    public void accountFavoritePropTest1()
    {
        final String username = "Ariane";
        final String propID = "15896822";
        
        assertEquals(false ,dataAccount.getFavoriteProperty(username,propID));
    }
    
    /**
     * Checks if a property is one of the users
     * favorite with a property that is
     * one of their favorites.
     */ 
    @Test
    public void accountFavoritePropTest2()
    {
        final String username = "Ariane";
        final String propID = "9923230";
        
        assertEquals(true,dataAccount.getFavoriteProperty(username,propID));
    }
    
    /**
     * Checks if we can add a property to the users
     * favorite.
     */ 
    @Test
    public void accountAddFavoritePropTest()
    {
        final String username = "Ariane";
        final String propID = "15896822";
        
        assertEquals(false ,dataAccount.getFavoriteProperty(username,propID));
        
        dataAccount.addFavoriteProp(username,propID);
        
        assertEquals(true,dataAccount.getFavoriteProperty(username,propID));
        
        dataAccount.removeFavoriteProp(username,propID);
    }
    
    /**
     * Checks if we can remove a property to the users
     * favorite.
     */ 
    @Test
    public void accountRemoveFavoritePropTest()
    {
        final String username = "Ariane";
        final String propID = "15896822";
        
        assertEquals(false ,dataAccount.getFavoriteProperty(username,propID));
        
        dataAccount.addFavoriteProp(username,propID);
        
        assertEquals(true,dataAccount.getFavoriteProperty(username,propID));
        
        dataAccount.removeFavoriteProp(username,propID);
        
        assertEquals(false ,dataAccount.getFavoriteProperty(username,propID));
    }
    
     /**
     * Checks if we can add a search.
     */ 
    @Test
    public void accountAddSearchTest()
    {
        final String username = "Ariane";
        final String search = "Station";
        
        assertEquals(false ,dataAccount.checkSearch(username, search));
        
        dataAccount.addSearch(username, search);
        
        assertEquals(true ,dataAccount.checkSearch(username, search));
        
        dataAccount.removeSearch(username, search);
    }
    
    /**
     * Checks if we can remove a search.
     */ 
    @Test
    public void accountRemoveSearchTest()
    {
        final String username = "Ariane";
        final String search = "Station";
        
        assertEquals(false ,dataAccount.checkSearch(username, search));
        
        dataAccount.addSearch(username, search);
        
        assertEquals(true ,dataAccount.checkSearch(username, search));
        
        dataAccount.removeSearch(username, search);
        
        assertEquals(false ,dataAccount.checkSearch(username, search));
        
    }
    
     /**
     * Checks if we can add a property from the users
     * favorite.
     */ 
    @Test
    public void accountAddPriceRangeTest()
    {
        final String username = "Ariane";
        final int from = 4000;
        final int to = 5000;
        
        assertEquals(false ,dataAccount.checkPriceRange(username,from,to));
        
        dataAccount.addPriceRange(username,from, to);
        
        assertEquals(true ,dataAccount.checkPriceRange(username,from,to));
        
        dataAccount.removePriceRange(username,from, to);
    }
    
    /**
     * Checks if we can remove a property from the users
     * favorite.
     */ 
    @Test
    public void accountRemovePriceRangeTest()
    {
        final String username = "Ariane";
        
        final int from = 4000;
        final int to = 5000;
        
        assertEquals(false ,dataAccount.checkPriceRange(username,from,to));
        
        dataAccount.addPriceRange(username,from, to);
        
        assertEquals(true ,dataAccount.checkPriceRange(username,from,to));
        
        dataAccount.removePriceRange(username,from, to);
        
        assertEquals(false ,dataAccount.checkPriceRange(username,from,to));
    }
    
    /**
     * Checks if we can add a borough to the users
     * favorite.
     */ 
    @Test
    public void accountAddFavoriteBorTest()
    {
        final String username = "Anne";
        final String borName = "CAMDEN";
        
        dataAccount.createAnAccount(username, "AZERTY");
        
        assertThat(dataAccount.userFavoriteBor(username), is(nullValue()));

        dataAccount.addFavoriteBor(username,borName);
        
        assertEquals(borName ,dataAccount.userFavoriteBor(username));
        
        dataAccount.deleteAnAccount(username);
    }
    
    /**
     * Checks if we can remove a borough from the users
     * favorite.
     */ 
    @Test
    public void accountRemoveFavoriteBorTest()
    {
        final String username = "Anne";
        final String borName = "CAMDEN";
        
        dataAccount.createAnAccount(username, "AZERTY");
        
        assertThat(dataAccount.userFavoriteBor(username), is(nullValue()));

        dataAccount.addFavoriteBor(username,borName);
        
        assertEquals(borName ,dataAccount.userFavoriteBor(username));
        
        dataAccount.removeFavoriteBor(username);
        
        assertThat(dataAccount.userFavoriteBor(username), is(nullValue()));
        
        dataAccount.deleteAnAccount(username);
    }
    
}
