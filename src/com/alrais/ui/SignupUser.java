package com.alrais.ui;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alrais.alsouq.MainActivity;
import com.alrais.alsouq.R;
import com.alrais.alsouq.webservice.JSONParser;

public class SignupUser extends Activity{
	
	public EditText inputname, inputemail, password, re_password, address, phone;
	public Button SignUp;
	public String paramName, paramEmail,paramPassword, paramre_password, paramAddress,paramPhone ;
	
	private ProgressDialog pDialog;
	
	private static String KEY_SUCCESS = "success";
    private static String TAG_MESSAGE = "error";
   
	
	// JSON parser class
	 	JSONParser jsonParser = new JSONParser();
	 	 private static final String SIGNUP_URL = "http://calicut.alraislabs.com:8002/shopinhand-php/signup.php";

	
	@Override
	protected void onCreate(Bundle savedInstanceStates){
		super.onCreate(savedInstanceStates);
		setContentView(com.alrais.alsouq.R.layout.signup);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		inputemail = (EditText) findViewById(R.id.fname);
		inputemail = (EditText) findViewById(R.id.inputemail);
		password =(EditText) findViewById(R.id.password);
		re_password = (EditText) findViewById(R.id.re_password);
		address = (EditText) findViewById(R.id.address);
		phone = (EditText) findViewById(R.id.phone);
		SignUp = (Button) findViewById(R.id.sign_up);
		
		SignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 paramName = inputname.getText().toString();
				 paramEmail = inputemail.getText().toString();
				 paramPassword = password.getText().toString();
				 paramre_password = re_password.getText().toString();
				 paramAddress = address.getText().toString();
				 paramPhone = phone.getText().toString();
	            
				new SignUp().execute();
				
			}
		});
		
	   }
	 
	
	class SignUp extends AsyncTask<String, String, String> {

		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupUser.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
			int code;
			
            try {
                // Building Parameters
            	ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("firstname", paramName));
            	param.add(new BasicNameValuePair("email", paramEmail));
				param.add(new BasicNameValuePair("password", paramPassword));
				param.add(new BasicNameValuePair("address", paramAddress));
				param.add(new BasicNameValuePair("mobile", paramPhone));

                
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                       SIGNUP_URL, "POST", param);
 
                // full json response
                Log.d("Registering attempt", json.toString());
 
                // json success element
                code= json.getInt("code");
                if (code == 1) {
                	Log.d("User Created!", json.toString());   
                	SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(SignupUser.this);
					Editor edit = sp.edit();
					edit.putString("username",paramEmail);
					edit.commit();
					Intent i = new Intent(SignupUser.this, MainActivity.class);
					finish();
					startActivity(i);

                	//finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(SignupUser.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 


}
