package net.waqassiddiqi.mrt.model;

import java.util.ArrayList;
import java.util.List;

import net.waqassiddiqi.mrt.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Worker {
	public String Id;
	public String name;
	public String passportNumber;
	public String nationality;
	public String photoUrl;
	public String workPermit;
	public String campName;
	public List<Camp> camps;
	
	public static Worker parseJson(String jsonString) {
		JSONObject rawResponseObject;
		Worker workerObject = null;
		
		try {
			rawResponseObject = new JSONObject(jsonString);
			workerObject = new Worker();
			
			if(rawResponseObject.has("id"))
				workerObject.Id = rawResponseObject.getString("id");
			
			if(rawResponseObject.has("name"))
				workerObject.name = rawResponseObject.getString("name");
			
			if(rawResponseObject.has("passport"))
				workerObject.passportNumber = rawResponseObject.getString("passport");
			
			if(rawResponseObject.has("nationality"))
				workerObject.nationality = rawResponseObject.getString("nationality");
			
			if(rawResponseObject.has("photo"))
				workerObject.photoUrl = rawResponseObject.getString("photo");
			
			if(rawResponseObject.has("work_permit"))
				workerObject.workPermit = rawResponseObject.getString("work_permit");
			
			if(rawResponseObject.has("camp"))
				workerObject.campName = rawResponseObject.getString("camp");
			
			if(rawResponseObject.has("camps")) {
				if(rawResponseObject.get("camps") instanceof JSONArray) {
					
					workerObject.camps = new ArrayList<Camp>();
					
					JSONArray campsArray = rawResponseObject.getJSONArray("camps");
							
					for(int i=0; i<campsArray.length(); i++) {
						Camp c = new Camp();
						c.campName = campsArray.getJSONObject(i).getString("name");
						c.campId = campsArray.getJSONObject(i).getString("id");
						
						workerObject.camps.add(c);
					}										
				}
			}
			
		} catch (Exception e) {
			Log.e(Constants.TAG, e.getMessage());
		}
		
		return workerObject;
	}
}
