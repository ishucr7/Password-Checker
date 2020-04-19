package mypack;
import java.util.Date;
import mypack.previousPasswords;
import dbdriver.*;
import models.*;
import java.sql.*;
import java.util.*;
// import java.time.
import java.util.concurrent.*;
import java.text.SimpleDateFormat;
public class userdao {
    DBDriver db;
    
    public userdao(DBDriver db){
        this.db = db;
    }

    public boolean is_username_registered(String username){
        String query = "select * from users where username = '" + username +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }

    public boolean is_password_valid(String username, String password){
        // generate hashed password
        String hashed_password = password;
        String query = "select * from users where username = '" + username + "' AND " + "password = '" + hashed_password +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }

    public UserModel get_user(String username){
        String query = "select * from users where username = '" + username +"'";
        UserModel User = this.db.select_user_query(query);
        return User;
    }

    public PreviousPasswordModel get_pp(String password){
        String query = "select * from previous_passwords where password = '" +
            password +  "'";
        PreviousPasswordModel pp = this.db.select_pp(query);
        return pp;
    }

    // Login
    public String login(String username, String password){
        UserModel User = get_user(username);
        if(User.username == null){
            return "There's no user with the  username given";
        }
        else if(User.is_locked){
            return "Your account has been locked due to 5 invalid login attempts";
        }
        else if(!is_password_valid(username, password)){
            String query;
            User.invalid_login_count +=1;
            query = "update users set invalid_login_count = '"+User.invalid_login_count+"'";
            
            // Set the is locked as true;
            if(User.invalid_login_count == 5){
                query = "update users set is_locked = true, invalid_login_count = '"+User.invalid_login_count+"'";
            }
            this.db.update(query);
            return "Invalid password";
        }
        else{
            
            PreviousPasswordModel pp = get_pp(password);
            Date curr_date = new Date();
            long diff = curr_date.getTime()  - pp.creation_date.getTime();
            long difference_in_days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            
            System.out.println("difference in days is ");
            System.out.println(difference_in_days);
            
            if(difference_in_days > 14){
                return "You haven't changed your password from  past 14 days. Reset your password";
            }
            return "Logged in";
        }
    }


    public String register(String username, String password){
        UserModel User = new UserModel();
        User.username = username;
        User.password = password;
        User.invalid_login_count = 0;
        User.creation_date = new Date();
        User.generate_hashed_password();
        System.out.println("Print the hashed password");
        System.out.println(User.hashed_password);

        String message;

        if(is_username_registered(User.username)){
            message = "There exists an user with the given username";
        }
        else if(!User.does_password_satisy_criteria()){
            message = "The password doesn't satisfy the given conditions";
        }
        else{

            Timestamp ts = new Timestamp(User.creation_date.getTime());
            // Write into the user table.
            String query = "insert into users values " + "('" + User.username + "','"+ User.hashed_password + "','" + User.invalid_login_count + "','" +
                 ts + "', false)";
            this.db.update(query);

            // Write into the previous password tables;
            query = "insert into previous_passwords " + 
                "values ('" + User.username+"','"+User.hashed_password+"','" + ts+"')";
            this.db.update(query);
            message="User registration successfull";
        }
        System.out.println(message);
        return message;
    }


    public Boolean are_credentials_valid(String username, String password){
        // generate hashed password
        String hashed_password = password;
        String query = "select * from users where username = '" + username + "' AND " + "password = '" + hashed_password +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }

    public String password_reset(String username, String oldpassword, String newpassword){

        // See if the user credentials are valid or not.
        if(!are_credentials_valid(username, oldpassword)){
            return "The user credentials are invalid";
        }

        UserModel User = new UserModel();
        User.username = username;
        User.password = newpassword;
        User.invalid_login_count = 0;
        // Message to be sent.
        String message;

        String query = "select * from previous_passwords where  username = '" + User.username + "'";

        ArrayList<PreviousPasswordModel> pplist =  this.db.select_previous_passwords_query(query);

        previousPasswords pp = new previousPasswords(pplist);

        // Does the new password satisfy the conditions given.
        if(!User.does_password_satisy_criteria()){
            message = "The password doesn't satisfy the following conditions";
        }
        // Because we need the hashed password here.
        else if(pp.is_this_one_of_old_passwords(User.hashed_password)){
            message = "You need to enter a different password other than your last 5 passwords.";
        }
        else{
            // Update the password into the user tabel
            Timestamp ts = new Timestamp(User.creation_date.getTime());
            query = "update users set password = '" + User.hashed_password +"' where username = '"+ User.username + "'";
            this.db.update(query);

            if(pp.get_length_of_list() >=5){
                String oldest_password = pp.get_oldest_password();
                query = "delete from previous_passwords where password = '" + oldest_password + "'";
                this.db.update(query);
                System.out.println("As length was greater than 5, the oldest password was deleted");
            }

            // Write into the previous password tables;
            query = "insert into previous_passwords " + 
                "values ('" + User.username+"','"+User.hashed_password+"','" + ts+"')";
            this.db.update(query);

            message = "Password reset successful";
        }
        System.out.println(message);
        return message;
    }
}