/**
 * It is to create the connection with the sql server running.
 * We'll use this connection to query the database.
*/

package dbdriver;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {
    public static Connection createConnection()
    {


        // Added a comment
        String dbUsername = "root";
        String dbPassword = "Radhaswami1"; // Enter your root's password here.

        Connection con = null;
        // String url = "jdbc:mysql://" + dbHost + "/" + dbName;
        String url = "jdbc:mysql://localhost:3306/password_checker";
    
        try 
        {
            try 
            {
                Class.forName("com.mysql.cj.jdbc.Driver"); //loading mysql driver 
            } 
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            } 
            con = DriverManager.getConnection(url, dbUsername, dbPassword); //attempting to connect to MySQL database
            System.out.println("Printing connection object "+con);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return con; 
    }
}