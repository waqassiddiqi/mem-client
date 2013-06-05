package net.waqassiddiqi.mrt.ui;

import java.text.SimpleDateFormat;

import org.json.JSONArray;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.R;
import net.waqassiddiqi.mrt.model.Response;

public class LabourMovementActivity extends BaseActivity {

	private String mWorkerId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labour_movement_layout);
		
		setIsSubActivity(true);
		setDisplayHomeAsUpEnabled(true);
		setTitle("Labour Movement");
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.getString("workerId") != null) {
			mWorkerId = extras.getString("workerId");
		} else {
			finish();
		}
		
		invokeWebservice(Constants.OPCODE_WORKER_MOVEMENT, "workerid", mWorkerId);
	}
	
	@Override
	protected void bindData(Response response) {

		try {
			if(response != null && response.getAttachedData() != null) {
				
				if(response.getAttachedData().has("movements") && response.getAttachedData().get("movements") instanceof JSONArray) {
					
					((TextView) findViewById(R.id.txtMovements)).setSingleLine(false);
					
					SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
					
					JSONArray moveArray = response.getAttachedData().getJSONArray("movements");
					StringBuilder sb = new StringBuilder();					
					for(int i=0; i<moveArray.length(); i++) {
						
						sb.append(sdfOut.format(sdfIn.parse(moveArray.getJSONObject(i).getString("date"))));
						sb.append("    ");
						sb.append(moveArray.getJSONObject(i).getString("campname"));		
						sb.append(System.getProperty ("line.separator"));
						sb.append(System.getProperty ("line.separator"));
					}
					
					((TextView) findViewById(R.id.txtMovements)).setText(sb.toString());
				}
			} else {
				((TextView) findViewById(R.id.txtNoDataFound)).setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
