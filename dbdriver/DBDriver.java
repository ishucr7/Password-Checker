package dbdriver;
import java.sql.*;
import models.*;
import java.util.*;
import java.util.Date;
import javax.naming.spi.DirStateFactory.Result;

public class DBDriver {
    public Connection con = null;
    public Statement statement = null;

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

    public UserModel select_user_query(String query){
        UserModel User = new UserModel();
        try{
            ResultSet res = this.statement.executeQuery(query);
            while(res.next()){                
                String username = res.getString("username");
                String password = res.getString("password");
                // See how date is stored.
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

    public PreviousPasswordModel select_pp(String query){
        PreviousPasswordModel pp = new PreviousPasswordModel();
        try{
            ResultSet  res = this.statement.executeQuery(query);
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

    //  It can work for both insert and update query.
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