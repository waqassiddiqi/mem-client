package net.waqassiddiqi.mrt;

import android.app.AlarmManager;

public class Constants {
	public static final String TAG = "MRT-Module";
	public static final String WEB_SERVICE_URL = "http://lab.kalsym.com/mrt/api.php";
	
	public static final String OPCODE_LOGIN = "login";
	public static final String OPCODE_WORKER_INFO = "workerinfo";
	public static final String OPCODE_WORKER_MOVEMENT = "workermovement";
	public static final String OPCODE_WORKER_OFFENCE = "workeroffenses";
	public static final String OPCODE_CAMP_FINDER = "findcamp";
	public static final String OPCODE_ADD_OFFENCE = "addoffenses";
	
	//Location search radius
  	public static int DEFAULT_RADIUS = 1;
	
	//The maximum distance the user should travel between location updates
  	public static long MAX_DISTANCE = (DEFAULT_RADIUS * 1000) / 2; 
  	
  	//The maximum time that should pass before the user gets a location update.
  	public static long MAX_TIME = AlarmManager.ELAPSED_REALTIME;
}
