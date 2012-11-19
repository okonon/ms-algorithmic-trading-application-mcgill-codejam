package util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a wrapper to run the eSignThread so that it gets the ceremony ID for modularity.
 * @author Breakend
 */
public class ESignWrapper {
    private static JSONObject json;
    private static String ceremonyID;
    
    public static void setJSON(JSONObject jsonob){
        json=jsonob;
    }
    
    public static String getCeremonyId() throws JSONException{
        if(json != null){
            ESignThread eSignThread = new ESignThread(); 
            eSignThread.setJSONString(json);
            eSignThread.start();
            String response;
            while(eSignThread.getResponseMessage()==null){} //wait until a response is gotten from the esign
            response = eSignThread.getResponseMessage();
            ceremonyID = response;
        } else{
            System.out.print("JSON is null, try getting your data again.");
        }
        return ceremonyID;
    }
}
