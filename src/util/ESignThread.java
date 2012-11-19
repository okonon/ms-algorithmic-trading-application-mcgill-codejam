
package util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creates new thread for esigning the data
 *
 * @author Team Gredona
 */
public class ESignThread extends Thread {

    private String JSONString;
    private volatile String responseMessage;

    public ESignThread() {
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public String setJSONString(JSONObject object) throws JSONException {
        JSONString = object.toString(0);
        return JSONString;
    }

    public void run() {
        String type = "application/json";
        URL u;
        try {
            u = new URL("https://stage-api.e-signlive.com/aws/rest/services/codejam");
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) u.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Basic Y29kZWphbTpBRkxpdGw0TEEyQWQx");
                conn.setRequestProperty("Content-Type", type);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(JSONString);
                writer.close();
                InputStream input = conn.getInputStream();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuilder jb = new StringBuilder();
                    String line = null;
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        line = reader.readLine();
                        this.responseMessage = line;
                    } catch (Exception e) {
                        System.out.println("Theres something wrong with the reader.");
                    }

                } else {
                    // Server returned HTTP error code.
                    System.out.println("There's a problem with the response");
                }
            } catch (IOException ex) {
                Logger.getLogger(ESignThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ESignThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}