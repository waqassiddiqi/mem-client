package net.waqassiddiqi.mrt.util;

import java.util.List;

import net.waqassiddiqi.mrt.Constants;

import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LastLocationFinder {
	protected LocationManager mLocationManager;
	protected Context mContext;
	protected Criteria mCriteria;
	protected LocationListener mLocationListener;
	
	public LastLocationFinder(Context context) {
		this.mContext = context;
		mLocationManager = (LocationManager) mContext.getSystemService(Service.LOCATION_SERVICE);
		mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);		
	}
	
	public Location getLastBestLocation(long minDistance, long minTime) {
		Location lastBestLocation = null;
		long bestTime = Long.MIN_VALUE;
		float bestAccuracy = Float.MAX_VALUE;
		
		List<String> matchingProviders = mLocationManager.getAllProviders();
		for (String provider : matchingProviders) {
			Location location = mLocationManager.getLastKnownLocation(provider);
			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();
				
				if ((time > minTime && accuracy < bestAccuracy)) {
					lastBestLocation = location;
					bestAccuracy = accuracy;
					bestTime = time;
				} else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
					lastBestLocation = location;
					bestTime = time;
				}
			}
		}
		
		if (bestTime < minTime || bestAccuracy > minDistance) { 
			String provider = mLocationManager.getBestProvider(mCriteria, true);
			if(provider != null) {
				mLocationManager.requestLocationUpdates(provider, 0, 0, singeUpdateListener, mContext.getMainLooper());
			}			
		}
		
		return lastBestLocation;								
	}
	
	protected LocationListener singeUpdateListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			
			Log.d(Constants.TAG, "Single Location Update Received: " + location.getLatitude() + "," + location.getLongitude());
			
			if (mLocationListener != null && location != null)
				mLocationListener.onLocationChanged(location);
			
			mLocationManager.removeUpdates(singeUpdateListener);
	    }
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	    public void onProviderEnabled(String provider) {}    
	    public void onProviderDisabled(String provider) {}
    };
    
    public void setChangedLocationListener(LocationListener l) {    
    	mLocationListener = l;
    }
    
    public void cancel() {
    	mLocationManager.removeUpdates(singeUpdateListener);
    }
}
