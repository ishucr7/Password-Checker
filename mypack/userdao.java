package mypack;
import java.util.Date;
import mypack.previousPasswords;
import dbdriver.*;
import models.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class userdao {
    DBDriver db;
    
    public userdao(DBDriver db){
        this.db = db;
    }

    /**
     * Checks if a user is registered with the given username in the database.
     * 
     * @param username --- username of the user.
     * 
     * @return Boolean --- True/False
     */
    private boolean is_username_registered(String username){
        String query = "select * from users where username = '" + username +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }

    /**
     * Checks if the password given is the actual password for the username.
     * NOTE:-- There exists a user with the given username.
     * 
     * @param username --- username of the user. [User exists]
     * @param password --- password entered by the user.
     * 
     * @return Boolean --- True/False
     */
    private boolean are_valid_credentials(String username, String password){
        // generate hashed password
        String hashed_password = password;
        String query = "select * from users where username = '" + username + "' AND " + "password = '" + hashed_password +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }

    /**
     * Get the user corresponding to the given username.
     * 
     * @param username --- username for which we need to fetch the user.
     * 
     * @return --- UserModel object corresponding to the given user.
     */
    private UserModel get_user(String username){
        String query = "select * from users where username = '" + username +"'";
        UserModel User = this.db.select_user_query(query);
        return User;
    }

    /**
     * Gets the previous_password entry for the given password.
     * 
     * @param password --- password for which we need to get the previous_password entry.
     * 
     * @return  --- PreviousPasswordModel object of that given password.
     */
    public PreviousPasswordModel get_pp_given_password(String password){
        String query = "select * from previous_passwords where password = '" +
            password +  "'";
        ArrayList<PreviousPasswordModel> pp = this.db.select_previous_passwords_query(query);
        return pp.get(0);
    }

    /**
     * Update the invalid login count of the user. In case the invalid login count
     * exceeds 5, mark it as locked in the database.
     * 
     * @param User --- UserModel Object.
     */
    private void update_invalid_login_count(UserModel User){
        String query;
        User.invalid_login_count +=1;
        query = "update users set invalid_login_count = '"+User.invalid_login_count+"'";
                // Set the is locked as true;
        if(User.invalid_login_count == 5){
            query = "update users set is_locked = true, invalid_login_count = '"+User.invalid_login_count+"'";
        }
        this.db.update(query);
    }

    /**
     * Logins the user if the given credentials are valid. If not sends
     * back the corresponding message.
     * 
     * @param username --- username of the user.
     * @param password --- password entered by the user.
     * 
     * @return String --- message to be sent back for the user.
     */
    public String login(String username, String password){

        UserModel User = get_user(username);

        // Message to be sent back.
        String message;

        if(User.username == null){
            message = "There's no user with the  username given";
        }
        else if(User.is_locked){
            message = "Your account has been locked due to 5 invalid login attempts";
        }
        else if(!are_valid_credentials(username, password)){
            update_invalid_login_count(User);
            message = "Invalid password";
        }
        else{
            // Check if the user hasn't changed password from past 14 days.
            PreviousPasswordModel pp = get_pp_given_password(password);
            Date curr_date = new Date();
            long diff = curr_date.getTime() - pp.creation_date.getTime();
            long difference_in_days =  TimeUnit.DAYS.convert(diff,
                TimeUnit.MILLISECONDS);
            
            System.out.println("difference in days is ");
            System.out.println(difference_in_days);
            
            if(difference_in_days > 14)
                message = "You haven't changed your password from past 14 days. Reset your password";
            else
                message = "Logged in";
        }
        return message;
    }

    /**
     * Registers a user with the given username and password. Also makes checks
     * the conditions on the username and the password.
     * 
     * @param username --- username of the user.
     * @param password --- password of the user.
     * 
     * @return String --- Returns the message for the user that whether the user
     * was registered or not.
     */
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


    private Boolean are_credentials_valid(String username, String password){
        // generate hashed password
        String hashed_password = password;
        String query = "select * from users where username = '" + username + "' AND " + "password = '" + hashed_password +"'";
        UserModel User = this.db.select_user_query(query);
        if(User.username!=null){
            return true;
        }
        return false;
    }


    /**
     * Resets the user password. Checks on the following conditions:-
     * 1 The newpassword should not match the preivous 5 passwords.
     * 2 The newpassword should also satisfy the password criteria.
     * 3 If the old password list is of size 5 then delete the oldest password
     *   and add the newpassword there.
     * 
     * @param username --- username of the user.
     * @param oldpassword --- oldpassword to validate that the user is correct.
     * @param newpassword --- newpassword to be set.
     * 
     * @return --- returns the appropriate message.
     */
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