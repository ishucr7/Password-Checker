package mypack;
import java.util.Date;
import java.util.ArrayList;
import models.PreviousPasswordModel;

public class previousPasswords {
    ArrayList<PreviousPasswordModel> pplist = new ArrayList<PreviousPasswordModel>();

    previousPasswords(ArrayList<PreviousPasswordModel> pplist){
        this.pplist = pplist;
    }

    public int get_length_of_list(){
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
    public Boolean is_this_password_old(String password){

        for(int i=0;i<this.pplist.size();i++){
            if(this.pplist.get(i).password == password){
                return true;
            }
        }
        return false;
    }
}