package net.waqassiddiqi.mrt.ui;

import net.waqassiddiqi.mrt.App;
import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.R;
import net.waqassiddiqi.mrt.model.Response;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddOffenceActivity extends BaseActivity {
	
	private EditText txtOffence;
	private String mWorkerId;
	private String mWorkerName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_offence_layout);
		
		setTitle("Add New Offence");
		
		txtOffence = (EditText) findViewById(R.id.txtOffence);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.getString("workerId") != null) {
			mWorkerName = extras.getString("workerName", "");
			mWorkerId = extras.getString("workerId", "");
			
			((EditText) findViewById(R.id.txtWorkerId)).setText(mWorkerId);
			((EditText) findViewById(R.id.txtWorkerName)).setText(mWorkerName);
						
			App app = (App) getApplication();
			if(app != null && app.getCurrentCampName() != null)
				txtOffence.setText("Unauthorized access to camp: " + app.getCurrentCampName());
			else
				txtOffence.setText("Unauthorized access to camp");
			
		} else {
			Toast.makeText(this, "Invalid worker code", Toast.LENGTH_LONG).show();
			finish();
		}
		
		((Button) findViewById(R.id.btnAdd)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				invokeWebservice(Constants.OPCODE_ADD_OFFENCE, "workerid", 
						mWorkerId, "offense", txtOffence.getText().toString());
			}
		});
		
		((Button) findViewById(R.id.btnCancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void bindData(Response response) {
		if(response != null && response.getAttachedData() != null) {
			
			Toast.makeText(this, "New offence has been added", Toast.LENGTH_LONG).show();
			finish();
			
		} else {
			Toast.makeText(this, "Unable to complete your request, please try again", Toast.LENGTH_LONG).show();
		}
	}
}
