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

    public ArrayList<PreviousPasswordModel> pplist = new ArrayList<PreviousPasswordModel>();

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

    public int get_length_of_pplist(){
        return this.pplist.size();
    }

    public String get_oldest_password(){
        Date oldest_date  = new Date();
        String oldest_password = null;
        for(int i=0;i<this.pplist.size();i++){
            Date d = this.pplist.get(i).creation_date;
            if(oldest_date.compareTo(d) > 0){
                oldest_date = d;
                oldest_password = this.pplist.get(i).password;
            }
        }
        return oldest_password;
    }

    public Boolean is_this_one_of_old_passwords(String password){
        for(int i=0;i<this.pplist.size();i++){
            if(this.pplist.get(i).password == password){
                return true;
            }
        }
        return false;
    }
}