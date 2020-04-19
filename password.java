import java.util.Date;
import java.util.regex.*;
public class password {
    public String hashed_password;
    public String user_id;
    public int invalid_login_count;
    public Date expiration_data;
    public String default_pass = "";

    public Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$\\@\\#\\%\\&\\*\\/\\\\])(?=.*\\d).+$");

    public boolean pattern_checker(String textToCheck) {
        Matcher m = p.matcher(textToCheck);
        return m.matches();
    }
    public boolean validate_password(String password){
        boolean is_valid = true;
        if(password.length() < 8)
            is_valid = false;
        if(!pattern_checker(password))
            is_valid = false;
        if(password == default_pass)
            is_valid = false;
        return is_valid;
    }
}