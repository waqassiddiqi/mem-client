package net.waqassiddiqi.mrt;

import android.app.Application;

public class App extends Application {
	private int currentCampId;
	private String currentCampName;	
	private String currentUsername;
	private String currentPassword;
	
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

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
}
