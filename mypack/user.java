package mypack;
import java.util.Date;
import java.util.regex.*;
import java.util.*; 
import models.UserModel;
public class user {

    UserModel User;
    public String default_password="a";

    public Pattern p = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$\\@\\#\\%\\&\\*\\/\\\\])(?=.*\\d).+$");
    
    public Matcher m;

    user(UserModel User){
        this.User = User;
        this.User.invalid_login_count = 0;
        this.User.creation_date = new Date();
        this.generate_hashed_password();
    }
    
    public boolean does_password_satisy_criteria(){
        this.m = this.p.matcher(this.User.password);

        if(this.User.password.length() < 8 || !m.matches() ||
            this.User.password == this.default_password )
            return false;
        return true;
    }

    public void generate_hashed_password(){
        // Use some hashing technique to generate a hashed password.
        this.User.hashed_password = this.User.password;
    }

}