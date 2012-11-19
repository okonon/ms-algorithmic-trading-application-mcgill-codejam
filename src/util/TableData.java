package util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableData {

	ArrayList<String[]> ema, sma, tma, lwma; 
	String[][] emat, smat, tmat, lwmat; 
	
 
	public TableData(JSONObject json){
		
		// set up Arrays
		ema = new ArrayList<String[]>(); 
		sma = new ArrayList<String[]>();
		tma = new ArrayList<String[]>();
		lwma = new ArrayList<String[]>();
                
                loadWith(json); 
	}
	
	// Load Data
	public void loadWith(JSONObject json){
		JSONArray trans = null; 
		try {
			trans = (JSONArray) json.get("transactions");
		} catch (JSONException e) {
			System.out.println("Error<TableData>: not JSON object "); 
			e.printStackTrace();
			System.exit(1); 
		} 
		
		
		for (int i = 0; i < trans.length(); i++){
			try {
				
				JSONObject obj = trans.getJSONObject(i); 
				
				String price = obj.getString("price"); 
				String time =  "" + obj.getInt("time");
				String type =  obj.getString("type");
				
				String row[] = new String[3]; 
				row[0] = time;
				row[1] = type;
				row[2] = price; 
				
				// add row to proper table 
				String strat = obj.getString("strategy"); 
				if (strat.equals("EMA"))
					ema.add(row); 
				else if (strat.equals("TMA"))
					tma.add(row);
				else if (strat.equals("SMA"))
					sma.add(row);
				else if (strat.equals("LWMA"))
					lwma.add(row);
				else 
					System.out.println("Error: Strategy could not be found"); 
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public String[][] getEMATable(){
		
		if (emat == null)
			emat = toArray(ema);  
		return emat; 
	}
	
	public String[][] getSMATable(){
		if (smat == null)
			smat = toArray(sma);  
		return smat;   
	}
	public String[][] getTMATable(){
		if (tmat == null)
			tmat = toArray(tma);  
		return tmat; 
	}
	public String[][] getLWMATable(){
		if (lwmat == null)
			lwmat = toArray(lwma);  
		return lwmat;    
	}
	
	public String[][] toArray(ArrayList<String[]> a){
		
		String[][] tb = new String[a.size()][];
		for (int i = 0; i < a.size(); i++)
			tb[i] = a.get(i); 
		
		return tb; 
	}
	 
}
