
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author Breakend
// */
//public class ESignTest {
//    public static void main(String[] args) throws JSONException, IOException{
//        
//            JSONObject json = new JSONObject();
//            JSONObject transaction = new JSONObject();
//            json.put("team", "Team Gredona");
//            json.put("destination", "cgredona@gmail.com");
//            transaction.put("time", 20);
//            transaction.put("type", "sell");
//            transaction.put("price", "29.294");
//            transaction.put("manager", "manager " + 2);
//            transaction.put("strategy", "YTM");
//            json.accumulate("transactions", transaction);
//            JSONObject transaction2 = new JSONObject();
//            transaction2.put("time", 25);
//            transaction2.put("type", "buy");
//            transaction2.put("price", "29.294");
//            transaction2.put("manager", "manager " + 1);
//            transaction2.put("strategy", "TVM");
//            json.accumulate("transactions", transaction2);
//            
//            String jsonStringForFile = json.toString(2);
//            Writer output = null;
//            File file = new File("codejam.json");
//            output = new BufferedWriter(new FileWriter(file));
//            output.write(jsonStringForFile);
//            output.close();
//            
//            ESignWrapper.setJSON(json);
//            String response = ESignWrapper.getCeremonyId();
//            System.out.println(response);
//    }
//}
