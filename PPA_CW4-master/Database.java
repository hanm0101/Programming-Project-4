   
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Connects to an online MySQL database.
 * Adds and removes data from the database.
 *
 * @author Faith Ong, Hana Mizukami, Sadiyah Khanam, Zeineb Bouchamaoui
 * @version 28-03-2019
 */
public class Database
{
    /**
     * Constructor for objects of class Database
     */
    public Database()
    {
        
    }

    /**
     * Connects to the online database.
     */
    public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url ="jdbc:mysql://sql2.freemysqlhosting.net:3306/sql2283353";
            String username = "sql2283353";
            String password = "cF6!aK4%";
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        }
        catch(Exception e){
            System.out.println(e);
        };
        return null;
    }
    
    /**
     * Creates the 5 tables used in this application (Borough, Property, Login, FavoritesP, FavoritesB).
     */
    public static void createTable() throws Exception{
        try{
            Connection conn = getConnection();
            String BTable = "CREATE TABLE IF NOT EXISTS Borough(Name VARCHAR(40) UNIQUE NOT NULL, Counter INTEGER NOT NULL,PRIMARY KEY(Name));";
            PreparedStatement createBorough = conn.prepareStatement(BTable);
            String PTable= "CREATE TABLE IF NOT EXISTS Property(ID VARCHAR(40) UNIQUE NOT NULL, Counter INTEGER NOT NULL, Name VARCHAR(40) NOT NULL, Price INTEGER NOT NULL"
            +"PRIMARY KEY(ID),"
            +"FOREIGN KEY(Name) REFERENCES Borough(Name) ON DELETE CASCADE ON UPDATE CASCADE);";
            PreparedStatement createProperty = conn.prepareStatement(PTable);
            String LTable= "CREATE TABLE IF NOT EXISTS Login(Username VARCHAR(40) UNIQUE NOT NULL, Password VARCHAR(40) NOT NULL, Name VARCHAR(40) NOT NULL, "
            +"PRIMARY KEY(Username),"
            +"FOREIGN KEY(Name) REFERENCES Borough(Name) ON DELETE RESTRICT ON UPDATE CASCADE);";
            PreparedStatement createLogin= conn.prepareStatement(LTable);
            String FPTable= "CREATE TABLE IF NOT EXISTS FavoritesP(Username VARCHAR(40) NOT NULL, IDfav VARCHAR(40)  NOT NULL,"
            +"PRIMARY KEY(Username, IDfav),"
            +"FOREIGN KEY(Username) REFERENCES Login(Username) ON DELETE CASCADE ON UPDATE CASCADE,"
            +"FOREIGN KEY(IDfav) REFERENCES Property(ID) ON DELETE CASCADE ON UPDATE CASCADE);";
            PreparedStatement createFavoriteP= conn.prepareStatement(FPTable);
            String SearchTable= "CREATE TABLE IF NOT EXISTS Search(Username VARCHAR(40) NOT NULL, Searched VARCHAR(40)  NOT NULL, Counter INTEGER NOT NULL, "
            +"PRIMARY KEY(Username, Searched),"
            +"FOREIGN KEY(Username) REFERENCES Login(Username) ON DELETE CASCADE ON UPDATE CASCADE);";
            PreparedStatement createSearch= conn.prepareStatement(SearchTable);
            String PricesTable= "CREATE TABLE IF NOT EXISTS PriceRange(Username VARCHAR(40) NOT NULL, FromRange INTEGER NOT NULL, ToRange INTEGER NOT NULL, Counter INTEGER NOT NULL,"
            +"PRIMARY KEY(Username, FromRange, ToRange),"
            +"FOREIGN KEY(Username) REFERENCES Login(Username) ON DELETE CASCADE ON UPDATE CASCADE);";
            PreparedStatement createPrices= conn.prepareStatement(PricesTable);
            
            createBorough.executeUpdate();
            createProperty.executeUpdate();
            createLogin.executeUpdate();
            createFavoriteP.executeUpdate();
            createSearch.executeUpdate();
            createPrices.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Create complete.");
        };
    }
    
    /**
     * Removes a table from the database.
     * @param tableName the name of the table to be removed
     */
    public static void drop(String tableName) throws Exception{
        try{
            Connection conn = getConnection();
            PreparedStatement delete = conn.prepareStatement("DROP TABLE '"+ tableName+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    /**
     * Removes a Borough from the Borough table.
     * @param name borough to be removed
     */
    public static void deleteFromBorough(String name) throws Exception{
        try{
            Connection conn = getConnection();
            PreparedStatement delete = conn.prepareStatement("DELETE FROM Borough WHERE Name = '"+ name+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    /**
     * Removes a Property from the Property table.
     * @param id property to be removed
     */
    public static void deleteFromProperty(String id) throws Exception{
        try{
            Connection conn = getConnection();
            PreparedStatement delete = conn.prepareStatement("DELETE FROM Property WHERE ID = '"+ id+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    /**
     * Removes login informations from the Login table.
     * @param username login info to be removed
     */
    public static void deleteFromLogin(Connection conn,String username) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement delete = con.prepareStatement("DELETE FROM Login WHERE Username = '"+ username+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    /**
     * Removes a property from the users favorite properties table.
     * @param username users username.
     * @param IDfav  ID of the property to be removed
     */
    public static void deleteFromFavoritesP(Connection conn, String username, String IDfav) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement delete = con.prepareStatement("DELETE FROM FavoritesP WHERE Username = '"
            + username +"' AND IDfav = '"+IDfav+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    /**
     * Removes a search from the users search table.
     * @param username users username.
     * @param search  The search that needs to be removed
     */
    public static void deleteFromSearch(Connection conn, String username, String search) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement delete = con.prepareStatement("DELETE FROM Search WHERE Username = '"
            + username +"' AND Searched = '"+search+"';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
     /**
     * Removes a search from the users search table.
     * @param username users username.
     * @param search  The search that needs to be removed
     */
    public static void deleteFromPriceRange(Connection conn, String username, int from, int to) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement delete = con.prepareStatement("DELETE FROM PriceRange WHERE Username = '"
            + username +"' AND FromRange = '"+from+"' AND ToRange ='"+ to+ "';");
            
            delete.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Delete complete.");
        };
    }
    
    
    /**
     * Updates the users favorite borough.
     * @param username users username.
     * @param Namefav  name of the borough to be removed
     */
    public static void updateFavoriteB(Connection conn,String username, String Namefav) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement update = con.prepareStatement("UPDATE Login SET Name = '"+Namefav+"' WHERE Username ='"+ username+ "';");
            
            update.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Update complete.");
        };
    }
    
    /**
     * Deletes the users favorite borough.
     * @param username users username.
     */
    public static void deleteFavoriteB(Connection conn,String username) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement update = con.prepareStatement("UPDATE Login SET Name = NULL WHERE Username ='"+ username+ "';");
            
            update.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Update complete.");
        };
    }
    
    /**
     * Adds a borough to the borough table.
     * @param conn Connection to the database.
     * @param name  name of the borough to be added.
     * @param count borough viewing counter.
     */
    public static void postBorough(Connection conn, String name, Integer count) throws Exception{
        final String bName = name;
        final Integer bCounter = count;
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO Borough (Name, Counter) VALUES('"+bName+ "','"+bCounter +"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        }
    }
    
    /**
     * Adds a property to the property table.
     * @param conn Connection to the database.
     * @param id  id of the property to be added.
     * @param count property viewing counter.
     * @param name name of the properties' borough.  
     */
    public static void postProperty(Connection conn, String id, Integer count, String name) throws Exception{
        final String PID = id;
        final Integer PCounter = count;
        final String Bname = name;
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO Property (ID, Counter, Name) VALUES('"+PID+ "','"+PCounter +"','"+ name +"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        }
    }
    
    /**
     * Adds the login info to the Login table.
     * @param conn Connection to the database.
     * @param username  The user's username.
     * @param password The account's password.
     */
    public static void postLogin(Connection conn, String username, String password, String bname) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO Login (Username, Password) VALUES('"+username+ "','"+password+"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        }
    }
    
    /**
     * Adds a property to the users favorites property table.
     * @param conn Connection to the database.
     * @param username  The user's username. .
     * @param IDfav ID of the property to be added.
     */
    public static void postFavoriteP(Connection conn, String username, String IDfav) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO FavoritesP (Username, IDfav) VALUES('"+username+ "','"+IDfav+"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        }
    }
    
    /**
     * Adds a Search to the users searches table.
     * @param conn Connection to the database.
     * @param username  The user's username. .
     * @param Search The search.
     * @param Counter The number of time the user searched that.
     */
    public static void postSearch(Connection conn, String username, String Search, int Counter) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO Search (Username, Searched, Counter) VALUES('"+username+ "','"+Search+"','"+Counter+"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        } 
    }
    
    /**
     * Adds a price range to the users PriceRange table.
     * @param conn Connection to the database.
     * @param username  The user's username. .
     * @param from The from range.
     * @param to the to range.
     * @param Counter The number of time the user searched that.
     */
    public static void postPriceRange(Connection conn, String username, int from, int to, int Counter) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement posted = con.prepareStatement("INSERT INTO PriceRange(Username, FromRange, ToRange, Counter) VALUES('"+username+ "','"+from+"','"+to+"','"+Counter+"')");
            posted.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            //System.out.println("Insert Completed.");
        }
    }
    
    /**
     * Get the counter for a specific Borough.
     * @param name Name of the borough.
     * @return An ArrayList that contains the corresponding Borough.
     */
    public static ArrayList<String> getBCounter(String name) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT Counter FROM Borough Where Name ='"
            + name +"'");
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Counter"));
                array.add(result.getString("Counter"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the counter for a specific property.
     * @param id ID of the property.
     * @return An ArrayList that contains the corresponding Property.
     */
    public static ArrayList<String> getPCounter(String id) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT Counter FROM Property WHERE ID = '"+ id+"';");
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
               // System.out.println(result.getString("Counter"));
                array.add(result.getString("Counter"));
            }
         //   System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the password for a specific username.
     * @param username Account username.
     * @return An ArrayList that contains the corresponding Property.
     */
    public static ArrayList<String> getPassword(Connection conn,String username) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("SELECT Password FROM Login WHERE Username = '"+ username+"';");
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Password"));
                array.add(result.getString("Password"));
            }
          //  System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the favorite borough for a specific username.
     * @param username Account username.
     * @return An ArrayList that contains the corresponding Borough.
     */
    public static ArrayList<String> getFavoriteB(Connection conn,String username) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("SELECT Name FROM Login WHERE Username = '"+ username+"';");
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Name"));
                array.add(result.getString("Name"));
            }
           // System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Get the favorite properties for a specific username.
     * @param username Account username.
     * @return An ArrayList that contains the corresponding Borough.
     */
    public static ArrayList<String> getFavoritePs(Connection conn,String username) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("SELECT IDfav FROM FavoritesP WHERE Username = '"+ username+"';");
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
           // System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Updates the value of the property viewing counter by increasing by one.
     * @param conn Connection to the database.
     * @param id ID of the property.
     */
    public static void updatePCounter(Connection conn, String id) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Property SET Counter = Counter + 1 WHERE ID ='"+ id+"';");
            statement.executeUpdate();
            //System.out.println("Property updated");
            
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the value of the borough viewing counter by increasing by one.
     * @param conn Connection to the database.
     * @param name Name of the borough.
     */
    public static void updateBCounter(Connection conn, String name) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Borough SET Counter =  Counter+1 WHERE Name ='"+name+"';");
            statement.executeUpdate();
            
            //System.out.println("Borough updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the value of the price range counter by increasing by one.
     * @param conn Connection to the database.
     * @param username The username of the user.
     * @param From The from range.
     * @param To The to range.
     */
    public static void updatePRCounter(Connection conn, String username, Integer From, Integer To) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE PriceRange SET Counter =  Counter+1 WHERE Username ='"+username+"' AND FromRange = '"
            +From+"' AND '"+To+"';");
            statement.executeUpdate();
            
            //System.out.println("PriceRange updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the value of the search counter by increasing by one.
     * @param conn Connection to the database.
     * @param username The username of the user.
     * @param Search The word searched.
     */
    public static void updateSCounter(Connection conn, String username, String Search) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Search SET Counter =  Counter+1 WHERE Username ='"+username+"' AND Searched = '"
            +Search+"';");
            statement.executeUpdate();
            
            //System.out.println("Search updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the account's password.
     * @param conn Connection to the database.
     * @param username Account username.
     * @param newpassword new password
     */
    public static void updatePassword(Connection conn, String username, String newpassword) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Login SET password = '"
            + newpassword+"' WHERE Name ='"+username+"';");
            statement.executeUpdate();
            
            //System.out.println("Password updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the property's price.
     * @param conn Connection to the database.
     * @param id The id of the property.
     * @param price the new price of the property.
     */
    public static void updatePrice(Connection conn, String id, int price) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Property SET Price = '"
            + price+"' WHERE ID ='"+id+"';");
            statement.executeUpdate();
            
            //System.out.println("Password updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Updates the property's price.
     * @param conn Connection to the database.
     * @param id The id of the property.
     * @param price the new price of the property.
     */
    public static void updateFavorite(Connection conn, String username, String ID) throws Exception{
        try{
            Connection con = conn;
            PreparedStatement statement = con.prepareStatement("UPDATE Login SET IDfav = '"
            + ID+"' WHERE username ='"+username+"';");
            statement.executeUpdate();
            
            ////System.out.println("Password updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Gets the most viewed Property.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed property.
     */
    public static ArrayList<String> getMostViewP(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT ID FROM Property ORDER BY Counter DESC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("ID"));
                array.add(result.getString("ID"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getMostViewB(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Name FROM Borough ORDER BY Counter DESC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Name"));
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
        
    /**
     * Gets the least viewed Property.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed property.
     */
    public static ArrayList<String> getLessViewP(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT ID FROM Property ORDER BY Counter ASC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("ID"));
                array.add(result.getString("ID"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the least viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed borough.
     */
    public static ArrayList<String> getLessViewB(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Name FROM Borough ORDER BY Counter ASC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Name"));
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getBestP(Connection conn)throws Exception{
       try{
            Connection con = conn;
            String query = "SELECT IDfav FROM FavoritesP GROUP BY IDfav ORDER BY Count(IDfav) DESC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getBestB(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Name FROM Login GROUP BY Name ORDER BY Count(Name) DESC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Name"));
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
        
    /**
     * Gets the least viewed Property.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed property.
     */
    public static ArrayList<String> getWorstP(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT IDfav  FROM FavoritesP GROUP BY IDfav ORDER BY  Count(IDfav) ASC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the least viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed borough.
     */
    public static ArrayList<String> getWorstB(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Name FROM Login GROUP BY Name ORDER BY Count(Name) ASC ;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Name"));
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getStatFavoriteP( Connection conn, int from, int to)throws Exception{
       try{
            Connection con = conn;
            String query = "SELECT IDfav FROM FavoritesP JOIN Property ON FavoritesP.IDfav = Property.ID WHERE Property.Price > '"+ from +"'AND Property.Price < '"+ to +"' GROUP BY IDfav ORDER BY Count(IDfav) DESC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getStatFavoriteB(Connection conn, int from, int to)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Property.Name FROM Property JOIN Login ON Login.Name = Property.Name WHERE Property.Price >"+ from +" AND Property.Price < "+ to +" GROUP BY Property.Name ORDER BY Count(Property.Name) DESC ;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
        
    /**
     * Gets the least viewed Property.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed property.
     */
    public static ArrayList<String> getStatLeastFavP(Connection conn, int from, int to)throws Exception{
        try{
            Connection con = getConnection();
            String query = "SELECT IDfav FROM FavoritesP JOIN Property ON FavoritesP.IDfav = Property.ID WHERE Property.Price > '"+ from +"'AND Property.Price < '"+ to +"' GROUP BY IDfav ORDER BY Count(IDfav) ASC LIMIT 1;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the least viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed borough.
     */
    public static ArrayList<String> getStatLeastFavB(Connection conn, int from, int to)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Property.Name FROM Property JOIN Login ON Login.Name = Property.Name WHERE Property.Price >"+ from +" AND Property.Price < "+ to +" GROUP BY Property.Name ORDER BY Count(Property.Name) ASC ;;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the least viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the least viewed borough.
     */
    public static ArrayList<String> getUsernames(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Username FROM Login;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                //System.out.println(result.getString("Username"));
                array.add(result.getString("Username"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getUserFavoriteP( Connection conn, String username)throws Exception{
       try{
            Connection con = conn;
            String query = "SELECT IDfav FROM FavoritesP JOIN Property ON FavoritesP.IDfav = Property.ID  WHERE FavoritesP.Username ='"+username+"' GROUP BY IDfav;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
               // System.out.println(result.getString("IDfav"));
                array.add(result.getString("IDfav"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the most viewed Borough.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getUserFavoriteB(Connection conn, String username)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Login.Name FROM Login JOIN Property ON Property.Name = Login.Name  WHERE Login.Username = '"+ username +"';";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the user searches.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<String> getSearches(Connection conn, String username)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Searched FROM  `Search` WHERE Username = '"+username+"' ORDER BY Counter ASC;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("Searched"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
       
    /**
     * Gets the user searched price ranges.
     * @param conn Connection to the database.
     * @return An ArrayList containing the most viewed borough.
     */
    public static ArrayList<Integer[]> getPriceRanges(Connection conn, String username)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT FromRange, ToRange FROM `PriceRange` WHERE Username = '"+username+"' ORDER BY Counter ASC;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<Integer[]> array = new ArrayList<>();
            while(result.next()){
                Integer[] tempArray = new Integer[2];
                tempArray[0] = Integer.valueOf(result.getString("FromRange"));
                tempArray[1] = Integer.valueOf(result.getString("ToRange"));
                array.add(tempArray);
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Gets the names of the boroughs.
     * @param conn Connection to the database.
     * @return An ArrayList containing the borough names.
     */
    public static ArrayList<String> getBNames(Connection conn)throws Exception{
        try{
            Connection con = conn;
            String query = "SELECT Name FROM `Borough`;";
            PreparedStatement statement = con.prepareStatement(query);
            
            ResultSet result = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("Name"));
            }
            //System.out.println("All records have been selected");
            return array;
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
    }
    
}
