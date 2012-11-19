/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import gui.TradingServerFrame;
import util.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.JOptionPane; 
import java.net.InetAddress;



/**
 *
 * @author nadnerbgalea
 */
public class Controller {
    
    
    
 
    
    public static void main(String args[]) {
        /*
         * Sets the JTattoo look and feel
         */
        try {
            com.jtattoo.plaf.noire.NoireLookAndFeel.setTheme("Small-Font");
            javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        }
        catch (Exception ex) {
            System.out.println(ex); 
        }
        
        InetAddress host = null; 
        int pPort = 0;
        int tPort = 0; 
       
        // find -p
        int index = -1; 
        for (int i = 0; i < args.length; i++ )
            if (args[i].equals("-p"))
                index = i+1; 
        
        try {
                pPort = Integer.parseInt(args[index]); 
        }catch (Exception e){
                System.out.println("Input format: -h <host ip> -p <port for exchange> -t <port for trading>"); 
                System.exit(1); 
            }
                
        // find -t 
        
        index = -1; 
        for (int i = 0; i < args.length; i++ )
            if (args[i].equals("-t"))
                index = i+1; 
        
        try {
                tPort = Integer.parseInt(args[index]); 
        }catch (Exception e){
                System.out.println("Input format: -h <host ip> -p <port for exchange> -t <port for trading>"); 
                System.exit(1); 
            }
        
        
        // find -h
        index = -1; 
        for (int i = 0; i < args.length; i++ )
            if (args[i].equals("-h"))
                index = i+1; 
        
        try {
            if (index == -1){
                host = InetAddress.getByName(null); 
             }else {
        
                   host = InetAddress.getByName(args[index]); 
                }
        }catch (Exception e){
                System.out.println("Input format: -h <host ip> -p <port for exchange> -t <port for trading>"); 
                System.exit(1); 
            }
        
        

        GraphData graphData = new GraphData(); 
        final TradingServerFrame tsf = new TradingServerFrame(graphData); 
        final Client client = new Client(host, pPort, tPort, graphData);
        client.connect(); 
        
        tsf.getStartPriceFeedButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                tsf.getStartPriceFeedButton().setEnabled(false); 
                client.send("H");
            }
        });
        
        
        // Enable Reporting Button 
        tsf.getsendReportButton().setEnabled(false);
        client.addFinishListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // enable send report and load tables
                tsf.getsendReportButton().setEnabled(true);
                tsf.updateTables(new TableData(client.getJSON()));
                
            }
        });
        
        
        // set reporting button function 
        tsf.getsendReportButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
               // disable button 
                tsf.getsendReportButton().setEnabled(false);
                
                //send report 
                ESignWrapper.setJSON(client.getJSON());
                
                Thread getCID = new Thread() {
                        public void run() {
                               try {

                                     String cID = ESignWrapper.getCeremonyId(); 
                    
                                  JOptionPane.showMessageDialog(tsf, cID, "Ceremony ID", JOptionPane.INFORMATION_MESSAGE);
                    
                }catch (JSONException err){
                    System.out.println("Controller error, json, cereemony id");
                    System.out.println(err); 
                }
                         }
                    };
                    getCID.start();
                    
             
            }
        });
        
        
        tsf.setVisible(true);
    }
    
    
    
}
