package models;
import java.util.Date;

public class UserModel {
    public String username;
    public String password;
    public String hashed_password;
    public int invalid_login_count;
    public Date creation_date;
    public Boolean is_locked;
}