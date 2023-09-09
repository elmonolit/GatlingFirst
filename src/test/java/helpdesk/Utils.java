package helpdesk;
import io.gatling.javaapi.core.Session;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    public static void filters(){
        String filter= Arrays.asList("owner", "queue", "status", "keywords", "date range", "kbitem")
                .get(new Random().nextInt(7));
//        switch (filter){
//            case "owner": return "a"
//        }

    }

    public static void writeUsersToCSV(Session session){
        try {
            FileWriter fw = new FileWriter("users.csv", true);
            fw.write(session.get("username")+ "," + session.get("password")+ "\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeUsersToCSVFromSession(Session session, String[] varsNames, String fileName){
        try {
//            FileWriter fw = new FileWriter("users.csv", true);
//            fw.write(session.get("username")+ "," + session.get("password")+ "\n");
//            fw.close();
            File f = new File(fileName);
            if (!f.exists()){
                FileWriter fw = new FileWriter(fileName);
                for (String var:varsNames) {
                    fw.write(var);
                    fw.write(",");
                }
                fw.write("\n");
                fw.close();
            }
            FileWriter fw = new FileWriter(fileName, true);
            for (String var: varsNames) {
                fw.write((char[]) session.get(var));

            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
