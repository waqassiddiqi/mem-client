package net.waqassiddiqi.mrt.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.waqassiddiqi.mrt.model.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebClient {
	public static Response invokeWebservice(String opCode, JSONObject params) {
		try {
			HttpParams httpParameters = new BasicHttpParams();			
			int timeoutConnection = 300000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 500000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(net.waqassiddiqi.mrt.Constants.WEB_SERVICE_URL);
						
			params.put("opcode", opCode);			
			
			
			/*
			for (Entry<String, String> entry : params.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}
			*/
			
			httpPost.setHeader("Content-Type","application/json");
			httpPost.setEntity(new StringEntity(params.toString(), "UTF8"));		
			
			HttpResponse response = httpClient.execute(httpPost);
	        
	        HttpEntity entity = response.getEntity();
	        InputStream inputStream = entity.getContent();
		    
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            inputStream.close();
            
            Log.d(net.waqassiddiqi.mrt.Constants.TAG, sb.toString());
            
            return parseResponseObject(sb.toString());
            
		} catch (Exception e) {
			Log.e(net.waqassiddiqi.mrt.Constants.TAG, e.getMessage(), e);
		}
		
		return null;
	}
	
	private static Response parseResponseObject(String jsonString) {
		JSONObject rawResponseObject;
		Response response = new Response();
		
		try {
			rawResponseObject = new JSONObject(jsonString.replace("Array", "").replace("(", "").replace(")", ""));
					
			
			if(rawResponseObject != null) {
				
				if(rawResponseObject.has("resultcode")) {
					response.setResultCode(rawResponseObject.getString("resultcode"));
					
					if(response.getResultCode().equals("0")) {
						
						response.setAttachedData(rawResponseObject);
						
						/*
						Iterator<String> keyIterator = rawResponseObject.keys();						
						while(keyIterator.hasNext()) {
							String paramKey = keyIterator.next();
							if(rawResponseObject.has(paramKey)) {
								if(!(rawResponseObject.get(paramKey) instanceof JSONArray)) {
									response.getAttachedData().put(paramKey, rawResponseObject.getString(paramKey));
								}
							}
						}
						*/
						
					}
				}
				
			}
		} catch (JSONException e) {
			response.setResultCode("99");
			e.printStackTrace();
		}
		
		return response;
	}
}
