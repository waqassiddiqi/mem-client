package net.waqassiddiqi.mrt.model;

import org.json.JSONObject;

public class Response {
	private String resultCode;	
	private JSONObject attachedData;
	
	public Response() { }
	public Response(String resultCode, JSONObject attachedData) {
		this.resultCode = resultCode;
		this.attachedData = attachedData;
	}
	
	public String getResultCode() {
		return this.resultCode;		
	}
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public JSONObject getAttachedData() {
		return this.attachedData;
	}
	
	public void setAttachedData(JSONObject attachedData) {
		this.attachedData = attachedData;
	}
}
