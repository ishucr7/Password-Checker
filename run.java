import mypack.*;
import dbdriver.*;
class run{
    public static userdao jbh;

    public static void pr(String message){
        System.out.println(message);
    }

    public static void register(){
        pr("Welcome to Signup \n");
        pr("username: ");
        String username = System.console().readLine(); 
        pr("password: ");
        String password = System.console().readLine(); 
        String mess = jbh.register(username, password);
        pr(mess+"\n");
    }

    public static void login(){
        pr("Welcome to login ");
        pr("username: ");
        String username = System.console().readLine(); 
        pr("password: ");
        String password = System.console().readLine(); 
        String mess = jbh.login(username, password);
        pr(mess+"\n");
    }

    public static void reset_password(){
        pr("Welcome to reset password ");
        pr("username: ");
        String username = System.console().readLine(); 
        pr("old password: ");
        String oldpassword = System.console().readLine(); 
        pr("new password: ");
        String newpassword = System.console().readLine(); 
        String mess = jbh.password_reset(username, oldpassword, newpassword);
        pr(mess);
    }

    public static void main(String[] arg){
        DBDriver db = new DBDriver();
        jbh = new userdao(db);

        pr("To register user press 0");
        pr("To login user press 1");
        pr("To reset user password press 2");
        pr("To quit 3");

        while(true){
            int inp = Integer.parseInt(System.console().readLine());
            if(inp==0)
                register();
            else if(inp == 1)
                login();
            else if(inp == 2)
                reset_password();
            else if(inp == 3)
                break;
            else{
                pr("No problem, enter it again");
            }
        }
    }
}
