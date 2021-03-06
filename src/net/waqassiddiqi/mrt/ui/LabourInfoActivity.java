package net.waqassiddiqi.mrt.ui;

import java.util.List;

import com.fedorvlasov.lazylist.ImageLoader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.waqassiddiqi.mrt.App;
import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.R;
import net.waqassiddiqi.mrt.model.Camp;
import net.waqassiddiqi.mrt.model.Response;
import net.waqassiddiqi.mrt.model.Worker;

public class LabourInfoActivity extends BaseActivity {

	private String mWorkerId = null;
	private String mWorkerName = null;
	private String mJsonData = null;
	private ImageLoader mImgLoader = null;
	private boolean mIsValidJsonData = false;	
	private App mApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labour_info_layout);
		
		setIsSubActivity(true);
		setDisplayHomeAsUpEnabled(true);
		setTitle("Labour Information");					
		
		mImgLoader = new ImageLoader(getApplicationContext());
		mApp = (App) getApplication();
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.getString("jsonData") != null) {
			Log.d(Constants.TAG, "Decoded json: " + extras.getString("jsonData"));
			
			mJsonData = extras.getString("jsonData");
		} else {
			finish();
		}		
		
		
		((Button) findViewById(R.id.btnViewOffences)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mWorkerId != null) {
					Intent labourIntent = new Intent(LabourInfoActivity.this, LabourOffenceActivity.class);
					labourIntent.putExtra("workerId", mWorkerId);
					startActivity(labourIntent);
				}
			}
		});
		
		((Button) findViewById(R.id.btnViewMovement)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mWorkerId != null) {
					Intent labourIntent = new Intent(LabourInfoActivity.this, LabourMovementActivity.class);
					labourIntent.putExtra("workerId", mWorkerId);
					startActivity(labourIntent);
				}
			}
		});
		
		if(mJsonData != null && mJsonData.trim().length() > 0) {
			Worker objWorker = Worker.parseJson(mJsonData);
			if(objWorker != null) {
				
				mWorkerId = objWorker.Id;
				mWorkerName = objWorker.name;
				
				bindControls(objWorker);
				
				mIsValidJsonData = true;
				
				displayCamps(objWorker.camps);
				checkCampOffence(objWorker.camps);
			}
		}		
		
		invokeWebservice(Constants.OPCODE_WORKER_INFO, "workerid", mWorkerId, 
				"username", mApp.getCurrentUsername(), "password", mApp.getCurrentPassword());
	}
	
	private void checkCampOffence(List<Camp> workerCamps) {
		
		boolean bFound = false;
		
		if(workerCamps != null) {
			for(Camp c : workerCamps) {
				if(c.campId.equals(Integer.toString(mApp.getCurrentCampId()))) {
					bFound = true;
					break;
				}
			}
		}
			
		if(!bFound && mApp.getCurrentCampId() > 0) {						
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	Intent intent = new Intent(LabourInfoActivity.this, AddOffenceActivity.class);
				        	intent.putExtra("workerId", mWorkerId);
				        	intent.putExtra("workerName", mWorkerName);
				        	startActivity(intent);
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            break;
			        }
			    }
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Laborer is not authorised to access this camp. Raise an offence?")
				.setPositiveButton("Yes", dialogClickListener)
		    	.setNegativeButton("No", dialogClickListener)
		    	.show();
			
		}		
	}
	
	private void bindControls(Worker objWorker) {
		
		if(objWorker == null)
			return;
		
		((TextView) findViewById(R.id.txtName)).setText(objWorker.name);
		((TextView) findViewById(R.id.txtGender)).setText("Gender: " + objWorker.gender);
		
		((TextView) findViewById(R.id.txtNationality)).setText("Nationality: " + objWorker.nationality);
		
		
		((TextView) findViewById(R.id.txtICNumber)).setText("IC Number: " + objWorker.ic);
		
		((TextView) findViewById(R.id.txtPassport)).setText(objWorker.passportNumber);
		((TextView) findViewById(R.id.txtPassportExpiry)).setText(objWorker.passportExpiry);
		
		((TextView) findViewById(R.id.txtPermit)).setText(objWorker.permit);
		((TextView) findViewById(R.id.txtPermitExpiry)).setText(objWorker.permitExpiry);
		
		((TextView) findViewById(R.id.txtPackage)).setText(objWorker.workerPackage);
		((TextView) findViewById(R.id.txtSubcontractor)).setText(objWorker.subContractor);
		
		((TextView) findViewById(R.id.txtCIDBNumber)).setText(objWorker.cidb);
		((TextView) findViewById(R.id.txtCIDBExpiry)).setText(objWorker.cidbExpiry);
		
		((TextView) findViewById(R.id.txtCamp)).setSingleLine(false);
		((TextView) findViewById(R.id.txtCamp)).setText(objWorker.campName);
		
		if(objWorker.camps != null && objWorker.camps.size() > 0) {
			displayCamps(objWorker.camps);
		}
		
		mImgLoader.DisplayImage(objWorker.photoUrl, (ImageView) findViewById(R.id.imgDisplayPic));
	}
	
	@Override
	protected void bindData(Response response) {
		
		try {
			if(response != null && response.getAttachedData() != null) {
				Worker objWorker = Worker.parseJson(response.getAttachedData().toString());
				bindControls(objWorker);
				
				mWorkerName = response.getAttachedData().getString("name");
				
				/*
				if(response.getAttachedData().has("photo") && response.getAttachedData().getString("photo") != null 
						&& response.getAttachedData().getString("photo").trim().length() > 0) {									
					
					try {
						URL u = new URL(response.getAttachedData().getString("photo"));
						
						mImgLoader.DisplayImage(u.toURI().toString(), (ImageView) findViewById(R.id.imgDisplayPic));
						
					} catch (URISyntaxException e) { }
					
				}					
				
				if(response.getAttachedData().has("camps") && response.getAttachedData().get("camps") instanceof JSONArray) {
					
					((TextView) findViewById(R.id.txtCamp)).setSingleLine(false);
					
					JSONArray campsArray = response.getAttachedData().getJSONArray("camps");
					List<Camp> lst = new ArrayList<Camp>();
					
					for(int i=0; i<campsArray.length(); i++) {
						Camp c = new Camp();
						c.campName = campsArray.getJSONObject(i).getString("name");
						c.campId = campsArray.getJSONObject(i).getString("id");
						
						lst.add(c);												
					}
									
					displayCamps(lst);
					
					checkCampOffence(lst);
				}
				*/
				
			} else {
				if(mIsValidJsonData == false) {
					Toast.makeText(this, "Invalid worker code", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void displayCamps(List<Camp> campList) {
		
		if(campList == null)
			return;
		
		StringBuilder sb = new StringBuilder();
		for(Camp c : campList) {
			sb.append(c.campName);
			sb.append(System.getProperty ("line.separator"));			
		}
		
		((TextView) findViewById(R.id.txtCamp)).setText(sb.toString());
	}
}