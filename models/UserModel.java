package models;
import java.util.Date;
import java.util.regex.*;
import java.util.*; 
import models.UserModel;


public class UserModel {
    public String username;
    public String password;
    public String hashed_password;
    public int invalid_login_count;
    public Date creation_date;
    public Boolean is_locked;


    public String default_password="a";

    public Pattern p = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$\\@\\#\\%\\&\\*\\/\\\\])(?=.*\\d).+$");
    
    public Matcher m;

    public UserModel(){
    }
    
    public boolean does_password_satisy_criteria(){
        this.m = this.p.matcher(this.password);

        if(this.password.length() < 8 || !m.matches() ||
            this.password == this.default_password )
            return false;
        return true;
    }

    public void generate_hashed_password(){
        // Use some hashing technique to generate a hashed password.
        this.hashed_password = this.password;
    }

}