package net.waqassiddiqi.mrt.ui;

import net.waqassiddiqi.mrt.App;
import net.waqassiddiqi.mrt.Constants;
import net.waqassiddiqi.mrt.R;
import net.waqassiddiqi.mrt.model.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {
	
	EditText txtUsername;
	EditText txtPassword;
	Button btnLogin;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		
		txtUsername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(txtUsername.getText().toString().trim().length() > 0) {
					btnLogin.setEnabled(true);
				} else {
					btnLogin.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		
		txtPassword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(txtPassword.getText().toString().trim().length() > 0) {
					btnLogin.setEnabled(true);
				} else {
					btnLogin.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		btnLogin = (Button) findViewById(R.id.btnLogin); 
		btnLogin.setEnabled(false);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {							
				invokeWebservice(Constants.OPCODE_LOGIN, "username", txtUsername.getText().toString(), 
						"password", txtPassword.getText().toString());				
			}
		});
	}

	@Override
	protected void bindData(Response response) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		builder.setMessage("Request to server failed")
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {		            	  		            	   			            	   			            	   			            	   			            	   
         	   
         	   dialog.dismiss();
            }
        })
        .setTitle("Error");					    	
		
		if(response != null && response.getResultCode().equals("0")) {
			
			App app = (App) getApplication();
			app.setCurrentUsername(txtUsername.getText().toString());
			app.setCurrentPassword(txtPassword.getText().toString());
			
			Intent intent = new Intent(this, DashboardActivity.class);
			startActivity(intent);
			finish();
			
		} else if(response != null && response.getResultCode().equals("1")) {
			builder.setMessage("Incorrect username/password");
			
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			AlertDialog alert = builder.create();
			alert.show();
		}			
	}
}
