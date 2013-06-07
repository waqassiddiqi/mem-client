package net.waqassiddiqi.mrt.model;

import java.util.ArrayList;
import java.util.List;

import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Worker {
	public String Id;
	public String name;
	public String gender;
	public String passportNumber;
	public String passportExpiry;
	public String nationality;
	public String photoUrl;
	public String campName;
	public List<Camp> camps;
	public String workerPackage;
	public String subContractor;
	public String permit;
	public String permitExpiry;
	public String cidb;
	public String cidbExpiry;
	public String ic;
	
	public static Worker parseJson(String jsonString) {
		JSONObject rawResponseObject;
		Worker workerObject = null;
		
		try {
			rawResponseObject = new JSONObject(jsonString);
			workerObject = new Worker();
			
			if(rawResponseObject.has("id")) {
				workerObject.Id = rawResponseObject.getString("id");
				workerObject.Id = Utils.replace(workerObject.Id, "null", "-");
			}
			
			if(rawResponseObject.has("name")) {
				workerObject.name = rawResponseObject.getString("name");
				workerObject.name = Utils.replace(workerObject.name, "null", "-");
			}
			
			if(rawResponseObject.has("gender")) {
				workerObject.gender = rawResponseObject.getString("gender");
				workerObject.gender = Utils.replace(workerObject.gender, "null", "-");
			}
			
			if(rawResponseObject.has("passport")) {
				workerObject.passportNumber = rawResponseObject.getString("passport");
				workerObject.passportNumber = Utils.replace(workerObject.passportNumber, "null", "-");
			}
			
			if(rawResponseObject.has("passportexpiry")) {
				workerObject.passportExpiry = rawResponseObject.getString("passportexpiry");
				workerObject.passportExpiry = Utils.replace(workerObject.passportExpiry, "null", "-");
			}
			
			if(rawResponseObject.has("nationality")) {
				workerObject.nationality = rawResponseObject.getString("nationality");
				workerObject.nationality = Utils.replace(workerObject.nationality, "null", "-");
			}
			
			if(rawResponseObject.has("photo"))
				workerObject.photoUrl = rawResponseObject.getString("photo");
			
			if(rawResponseObject.has("permit")) {
				workerObject.permit = rawResponseObject.getString("permit");
				workerObject.permit = Utils.replace(workerObject.permit, "null", "-");
			}
			
			if(rawResponseObject.has("permitexpiry")) {
				workerObject.permitExpiry = rawResponseObject.getString("permitexpiry");
				workerObject.permitExpiry = Utils.replace(workerObject.permitExpiry, "null", "-");
			}
			
			if(rawResponseObject.has("cidb")) {
				workerObject.cidb = rawResponseObject.getString("cidb");
				workerObject.cidb = Utils.replace(workerObject.cidb, "null", "-");
			}
			
			if(rawResponseObject.has("cidbexpiry")) {
				workerObject.cidbExpiry = rawResponseObject.getString("cidbexpiry");
				workerObject.cidbExpiry = Utils.replace(workerObject.cidbExpiry, "null", "-");
			}
			
			if(rawResponseObject.has("package")) {
				workerObject.workerPackage = rawResponseObject.getString("package");
				workerObject.workerPackage = Utils.replace(workerObject.workerPackage, "null", "-");
			}
			
			if(rawResponseObject.has("subcontractor")) {
				workerObject.subContractor = rawResponseObject.getString("subcontractor");
				workerObject.subContractor = Utils.replace(workerObject.subContractor, "null", "-");
			}
			
			if(rawResponseObject.has("ic")) {
				workerObject.ic = rawResponseObject.getString("ic");
				workerObject.ic = Utils.replace(workerObject.ic, "null", "-");
			}
			
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
