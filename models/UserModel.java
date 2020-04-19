package models;
import java.util.Date;
import java.util.regex.*;
import java.util.*; 
import models.UserModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        this.hashed_password = this.password;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(this.password.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            this.hashed_password = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        // Use some hashing technique to generate a hashed password.
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
            if(this.pplist.get(i).password.equals(password)){
                return true;
            }
        }
        return false;
    }
}