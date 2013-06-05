package net.waqassiddiqi.mrt;

import android.app.Application;

public class App extends Application {
	private int currentCampId;
	private String currentCampName;	
	
	public int getCurrentCampId() {
		return this.currentCampId;
	}
	
	public void setCurrentCampId(int currentCampId) {
		this.currentCampId = currentCampId;
	}
	
	public String getCurrentCampName() {
		return this.currentCampName;
	}
	
	public void setCurrentCampName(String currentCampName) {
		this.currentCampName = currentCampName;
	}
}
