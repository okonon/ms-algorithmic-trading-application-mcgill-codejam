
package util; 

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thread which takes action methods sends information to the server and creates
 * JSON
 *
 * @author Team Gredona
 *
 *
 */
public class ActionThread extends Thread {
     
    
    
    private enum ConnectionState {
        
        LISTENING, CONNECTING, CONNECTED, CLOSED
    }
    
    
    private SuperQueue<ActionObject> q; 
    private JSONObject json;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ConnectionState state;
    private int port;
    private InetAddress host;
    private ActionListener finishedListener;
    private TableData tableData; 
    
    private LinkedList<ActionObject> actions; 
    
    private Object lock; 
    
    

    /**
     * Constructor for the thread, sets up socket connection and Data Streams,
     * also begins JSON
     *
     * @author TeamG
     *
     */
    public ActionThread(InetAddress host, int port) {
        
        this.host = host;
        this.port = port;
        
        lock = new Object(); 
        
        json = new JSONObject();
        q = new SuperQueue<ActionObject>(); 
        actions = new LinkedList<ActionObject>();  
        
        state = ConnectionState.CONNECTING;
        this.start();
        
        
    }
    
    
    public JSONObject getJSON(){
    	return json; 
    }
    
    public void addFinishListener(ActionListener l){
            finishedListener = l; 
    }
    
    
    synchronized private void connectionOpened() throws IOException {
        //set up input and state
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        
        
        state = ConnectionState.CONNECTED;
        
    }

    /**
     * This is called by the run() method when the connection is closed from the
     * other side. (This is detected when an end-of-stream is encountered on the
     * input stream.) It posts a message to the transcript and sets the
     * connection state to CLOSED.
     */
    synchronized private void connectionClosedFromOtherSide() {
        if (state == ConnectionState.CONNECTED) {
            state = ConnectionState.CLOSED;
        }
    }
    
   
    synchronized void close() {
        state = ConnectionState.CLOSED;
    }

    /**
     * Called from the finally clause of the run() method to clean up after the
     * network connection closes for any reason.
     *
     * @throws JSONException
     * @throws IOException
     */
    private void cleanUp() throws JSONException, IOException {
            
        state = ConnectionState.CLOSED;
        if (socket != null && !socket.isClosed()) {
            // Make sure that the socket, if any, is closed.
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        
        socket = null;
        in = null;
        out = null;
        
        json.put("team", "Team G");
        json.put("destination", "mcgillcodejam2012@gmail.com");
        
        ESignWrapper.setJSON(json);
        String jsonString = this.json.toString(2);
        Writer output = null;
        File file = new File("codejam.json");
        output = new BufferedWriter(new FileWriter(file));
        output.write(jsonString);
        output.close();
        
        if (finishedListener != null)
            finishedListener.actionPerformed(null);
    }
    
    
    synchronized private void buysell(ActionObject ao)  {
        out.println('B');
        out.flush();
        if (out.checkError()) {
            close();
        }
        actions.add(ao);
        
        
    }
    
    private String readNext() throws IOException{
        
          // Get input************
        char c;
        int size = 0;
        char cbuf[] = new char[7];
        int decimal = 0;
        boolean decimalFlag = false;
        while (decimal < 3) {
            c = (char) in.read();
            
            cbuf[size] = c;
            size++;
            
            if (decimalFlag) {
                decimal++;
            }
            
            if (c == '.') {
                decimalFlag = true;
            }
            
            if (c == 'E') {
                break;
            }
        }
        
        String message = "";
        for (int i = 0; i < size; i++) {
            message += cbuf[i];
        }
        
        
        if (message.charAt(0) == 'E')
            return null; 
        
        return message; 
        
    }
    
    private void buildJSON() throws JSONException{
        

        Iterator<ActionObject> iter = actions.iterator(); 
        
        while (iter.hasNext()){
            
            ActionObject ao = iter.next(); 
            
            String message = null; 
            try {
                 message = this.readNext(); 
            }catch (IOException e){
                continue; 
            }
            
            if (message == null)
                continue; 
            
            int time = ao.getTime();
            Strategy strategy = ao.getStrategy();

            int manager = ManagerSchedule.getManager(time, strategy.getTypeInt());
            
            JSONObject transaction = new JSONObject();
            transaction.put("time", time);
            transaction.put("type", ao.getType().toLowerCase());
            transaction.put("price", message);
            transaction.put("manager", "manager " + manager);
            transaction.put("strategy", strategy.getAcronym());
            json.accumulate("transactions", transaction);
        }
    
        
    }


    
    public  SuperQueue<ActionObject> getSuperQueue(){
        return q; 
    }
    
    public Object getLock(){
        return lock; 
    }

    /**
     * Sets up the runnable loop which checks whether the Queue is empty,
     * dequeues the next action if there is one
     *
     * @author TeamG
     *
     */
    public void run() {
        
        try {
            if (state == ConnectionState.CONNECTING) {
                socket = new Socket(host, port);
            }
            connectionOpened();
            
            while (state == ConnectionState.CONNECTED) {
                
                ActionObject currentAction = null;
                while (q.peek() != null) {
                    currentAction = q.pop();
                    buysell(currentAction); 
                }
 
                synchronized(lock){
                    lock.wait(); 
                }
                
            }   //end while connected
            
        
        buildJSON();
        if (socket != null) {
                socket.close();
            }
       
        } catch (Exception e) {

            // An error occurred.  Report it to the user, but not
            // if the connection has been closed (since the error
            // might be the expected error that is generated when
            // a socket is closed).
            if (state != ConnectionState.CLOSED) {
                System.out.println("\n\n Trading ERROR:  " + e);
            }
        } finally {
            
            try {
                cleanUp();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
