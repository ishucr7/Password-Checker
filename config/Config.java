package config;
import java.util.*;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// public class Config {
//     public static Properties prop;
//     public Config(){
//         prop = new Properties();
//         String fileName = "app.config";
//         InputStream is = null;
//         try {
//             System.out.println("path ");
            
//             is = new FileInputStream(fileName);
//         } catch (FileNotFoundException ex) {
//             System.out.println("error bitches");
//         }
//         try {
//             prop.load(is);
//         } catch (IOException ex) {
//             //...
//         }
//         System.out.println(prop.getProperty("dbUsername"));
//         // System.out.println(prop.getProperty("app.version"));
//     }
//     public String getProperty(String key){
//         return prop.getProperty(key);
//     }
// }


public class Config{
    public static File configFile = new File("app.config");
    public static Properties props;
    public static FileReader reader;

    public Config(){
        try {
            reader = new FileReader(configFile);
            props = new Properties();
            props.load(reader);
            String host = props.getProperty("dbHost");
            
            System.out.print("Host name is: " + host);
            // reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error bitches 1");
            // file does not exist
        } catch (IOException ex) {
            System.out.println("Error bitches");
            // I/O error
        }
    }

    public String getProperty(String key){
        return props.getProperty(key);
    }
    public void closeReader(){
        try{
            reader.close();
        }catch(IOException ex){

        }
    }
}
// // public class Config
// // {
// //     Properties configFile;
// //     public Config()
// //     {
// //         configFile = new java.util.Properties();
// //         try {
// //             configFile.load(this.getClass().getClassLoader().
// //                 getResourceAsStream("./config.cfg"));
// //         }catch(Exception eta){
// //             eta.printStackTrace();
// //         }
// //     }
        
// //     public String getProperty(String key)
// //     {
// //         String value = this.configFile.getProperty(key);
// //         return value;
// //     }
// // }