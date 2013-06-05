package net.waqassiddiqi.mrt.ui;

import org.json.JSONObject;

import net.waqassiddiqi.mrt.model.Response;
import net.waqassiddiqi.mrt.service.WebClient;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockActivity {
	
	private boolean isSubActivity = false;;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
		switch(item.getItemId()) {
			case android.R.id.home:
				if(isSubActivity)
					finish();
				break;
		}
		
		return true;
	}
	
	protected void setIsSubActivity(boolean isSubActivity) {
		this.isSubActivity = isSubActivity;
	}
	
	protected abstract void bindData(Response response);
	
	protected final void invokeWebservice(boolean bShowLoading, String opCode, String... params) {
		
		JSONObject jsonParams = new JSONObject();
		try {
			for(int i=0; i<params.length; i+=2) {
				jsonParams.put(params[i], params[i+1]);
			}
			
			new RequestWebService(this, opCode, jsonParams, bShowLoading).execute();
			
		} catch (Exception e) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
			builder.setMessage("Request to server failed - " + e.getMessage())
	        .setCancelable(false)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {		            	  		            	   			            	   			            	   			            	   			            	   
	         	   
	         	   dialog.dismiss();
	            }
	        });
			
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	protected final void invokeWebservice(String opCode, String... params) {
		invokeWebservice(true, opCode, params);
	}
	
	protected final void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
	}
	
	private static class RequestWebService extends AsyncTask<Void, Void, Response> {
		private Context mContext;
		private ProgressDialog mProgressDialog = null;
		private String mOpCode;
		private JSONObject mParams;
		private boolean bShowLoading = true;
		
		public RequestWebService(Context context, String opCode, JSONObject params, boolean showLoading) {
			this.mContext = context;
			this.mOpCode = opCode;
			this.mParams = params;
			this.bShowLoading = showLoading;
		}
		
		@Override
		protected void onPreExecute() {
			if(bShowLoading)
				mProgressDialog = ProgressDialog.show(mContext, "Fetching data", "Please wait...", true);
		}
		
		@Override
		protected Response doInBackground(Void... v) {
			return WebClient.invokeWebservice(mOpCode, mParams);
		}
		
		@Override
		protected void onPostExecute(Response response) {
			if(mProgressDialog != null)
				mProgressDialog.dismiss();
			
			BaseActivity thisActivity = (BaseActivity) mContext;
			thisActivity.bindData(response);
		}
	}
}
