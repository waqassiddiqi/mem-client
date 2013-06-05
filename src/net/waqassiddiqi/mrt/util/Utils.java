package net.waqassiddiqi.mrt.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static boolean isOnline(Context contetx) {		
		ConnectivityManager cm = 
				(ConnectivityManager) contetx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if (netInfo != null && netInfo.isConnected()) {		
	        return true;
	    }
		
		return false;
	}
}
