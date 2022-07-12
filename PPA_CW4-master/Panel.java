import javafx.event.*;
import javafx.scene.Scene;
/**
 * Write a description of class Panel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public  abstract class Panel
{
    
    /**
     * Constructor for objects of class Panel
     */
    public Panel()
    {
    }
    protected void nextPanel(ActionEvent event){
        try{
            Main.next();
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    protected void previousPanel(ActionEvent event){
        try{
            Main.previous();
        }
        catch(Exception e){
            System.out.println(e);
        };
    }
    
    
}