package net.waqassiddiqi.mrt.ui;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import net.waqassiddiqi.mrt.App;
import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.R;
import net.waqassiddiqi.mrt.model.Response;
import net.waqassiddiqi.mrt.util.LastLocationFinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends BaseActivity {

	private LastLocationFinder mLastBestLocFinder;
	private boolean bShowMessage = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		
		prepareLocationFinder();
		checkGpsStatus();
		new LocationTask(this).execute();
		
		try {
			((Button) findViewById(R.id.btnScanQRCode)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentIntegrator integrator = new IntentIntegrator(DashboardActivity.this);
					integrator.initiateScan();										
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		((Button) findViewById(R.id.btnViewCampInfo)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				
				Location currentLoc = mLastBestLocFinder.getLastBestLocation(Constants.MAX_DISTANCE, Constants.MAX_TIME);
				if(currentLoc != null) {
					bShowMessage = true;
					invokeWebservice(Constants.OPCODE_CAMP_FINDER, "latitude", 
							Double.toString(currentLoc.getLatitude()), "longitude", 
							Double.toString(currentLoc.getLongitude()));
				} else {
					Toast.makeText(DashboardActivity.this, 
							"Unable to locate Camp", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void checkGpsStatus() {
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Yout GPS seems to be disabled, do you want to enable it?")
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, final int id) {
		                   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
	    }
	}
	
	private class LocationTask extends AsyncTask<Void, Void, Location> {
		private Context mContext;
		
		public LocationTask(Context context) {
			this.mContext = context;
		}
		@Override
		protected Location doInBackground(Void... params) {			
			return mLastBestLocFinder.getLastBestLocation(Constants.MAX_DISTANCE, Constants.MAX_TIME);			
		}
		
		@Override
		protected void onPostExecute(Location loc) {
			if(loc != null) {
				((DashboardActivity) mContext).invokeWebservice(false, Constants.OPCODE_CAMP_FINDER, "latitude", 
					Double.toString(loc.getLatitude()), "longitude", 
					Double.toString(loc.getLongitude()));
			}
		}
	}
	
	/**
	 * Create new LastLocationFinder class object
	 * get the current where possible or last best known location information
	 */
	private void prepareLocationFinder() {
		mLastBestLocFinder = new LastLocationFinder(getApplicationContext());		
	}
	
	@Override
	protected void bindData(Response response) {
		try {
			boolean bFound = false;
			
			if(response != null && response.getAttachedData() != null) {
				
				App app = (App) getApplication();
				
				if(response.getAttachedData().has("campname")) {
					
					if(app != null) {
						app.setCurrentCampName(response.getAttachedData().getString("campname"));
						app.setCurrentCampId(Integer.parseInt(response.getAttachedData().getString("campid")));
					}
					
					bFound = true;
					
					if(bShowMessage) {
						Toast.makeText(this, "You are in camp: " + response.getAttachedData().getString("campname")
								, Toast.LENGTH_LONG).show();
						bShowMessage = false;
					}
				}
			}
			
			if(!bFound && bShowMessage) 
				Toast.makeText(this, "Unable to locate Camp", Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null && scanResult.getContents() != null && scanResult.getContents().trim().length() > 0) {
			
			Intent labourIntent = new Intent(this, LabourInfoActivity.class);
			labourIntent.putExtra("jsonData", scanResult.getContents().trim());
			startActivity(labourIntent);
			
		}
	}
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	DashboardActivity.this.finish();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
		builder.setMessage("Are you sure to exit?")
			.setPositiveButton("Yes", dialogClickListener)
	    	.setNegativeButton("No", dialogClickListener)
	    	.show();
	}
}
