/**
 * Driver to that'll handle the queries to the mysql server by the user.
 */

package dbdriver;
import java.sql.*;
import models.*;
import java.util.*;
import java.util.Date;
import javax.naming.spi.DirStateFactory.Result;

public class DBDriver {

    // Will contain the connection.
    public Connection con = null;
    public Statement statement = null;

    // Constructor to get the connection running.
    public DBDriver(){
        this.con = DBConnection.createConnection();
        try{
            this.statement = con.createStatement();
            System.out.println("You have the connection ready bitches");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This function is specifically written to perform select query for the user.
     * It returns the user stored in the db in the form of UserModel Object.
     * 
     * @param query -- The sql query to be run on the sql server.
     * 
     * @return UserModel --- User obtained from db in the form of UserModel Object.
     */
    public UserModel select_user_query(String query){
        UserModel User = new UserModel();

        try{
            ResultSet res = this.statement.executeQuery(query);
            while(res.next()){
                String username = res.getString("username");
                String password = res.getString("password");
                int invalid_login_count = res.getInt("invalid_login_count");
                boolean is_locked = res.getBoolean("is_locked");

                User.username = username;
                User.hashed_password = password;
                User.invalid_login_count = invalid_login_count;
                User.is_locked = is_locked;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return User;
    }

    /**
     * It gets the previous_password entry from the previous_passwords table.
     * 
     * @param query --- The query that gets the previous_password entry from
     *                  the sql server.
     * 
     * @return PreviousPasswordModel  --- previous_password entry in the form of
     *                                    PreviousPasswordModel Object.
     */
    public PreviousPasswordModel select_pp(String query){

        PreviousPasswordModel pp = new PreviousPasswordModel();
        try{
            ResultSet res = this.statement.executeQuery(query);
            while(res.next()){
                String username = res.getString("username");
                String password = res.getString("password");
                Date date = new Date(res.getTimestamp("creation_date").getTime());
                pp.username = username;
                pp.creation_date = date;
                pp.password = password;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return pp;
    }

    /**
     * Gets the list of the previous_passwords associated with a particular username.
     * 
     * @param query -- The sql query that fetches all the entries in the previous_passwords
     *                 table from the sql server.
     * 
     * @return  ArrayList<PreviousPasswordModel>  --- ArrayList of previousPasswordModel
     *                                                objects.
     */
    public ArrayList<PreviousPasswordModel> select_previous_passwords_query(String query){
        ArrayList<PreviousPasswordModel> pplist = new ArrayList<PreviousPasswordModel>();
        try{
            ResultSet res = this.statement.executeQuery(query);
            while(res.next()){
                String username = res.getString("username");
                String password = res.getString("password");
                Date date = new Date(res.getTimestamp("creation_date").getTime());

                PreviousPasswordModel pp = new PreviousPasswordModel();
                pp.username = username;
                pp.creation_date = date;
                pp.password = password;
                // append it to the list.
                pplist.add(pp);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return pplist;
    }

    /**
     * It runs the update/delete/insert.
     * @param query -- The sql query to be run on the server.
     * 
     * @return integer -- 1 if the update/insert/delete was successful, 0 
     *                      otherwise.
     */
    public int update(String query){
        try{
            return this.statement.executeUpdate(query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("There was some problem with the update query");
        return 0;
    }

}