package com.alrais.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity{
	
	public EditText Useremail;
	public Button SendEmail;
	@Override
	protected void onCreate(Bundle savedInstancState){
		super.onCreate(savedInstancState);
		setContentView(com.alrais.alsouq.R.layout.forgotpassword);
		Useremail = (EditText) findViewById(com.alrais.alsouq.R.id.forgot_email);
		SendEmail = (Button) findViewById(com.alrais.alsouq.R.id.Send_email);
		SendEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String email = Useremail.getText().toString();
				
				Toast.makeText(getApplicationContext(), "Email is"+email, Toast.LENGTH_LONG).show();
				
			}
		});
	}

}
