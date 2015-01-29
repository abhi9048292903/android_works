package com.alrais.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alrais.alsouq.MainActivity;
import com.alrais.alsouq.R;
import com.alrais.alsouq.webservice.JSONParser;

public class SignInActivity extends Activity {
	public Button Signin;
	public EditText InputEmail, InputPassword;
	public TextView Forgotpassword, SignUp;
	public String paramEmail, paramPassword, line=null, result = null;
	InputStream is = null;
   
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    
 // Progress Dialog
 	private ProgressDialog pDialog;

 	// JSON parser class
 	JSONParser jsonParser = new JSONParser();
    
    private static final String LOGIN_URL = "http://calicut.alraislabs.com:8002/shopinhand-php/login.php";

	@Override
	protected void onCreate(Bundle stateInstance){
		super.onCreate(stateInstance);
		setContentView(com.alrais.alsouq.R.layout.signin);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		InputEmail = (EditText) findViewById(com.alrais.alsouq.R.id.email);
		InputPassword = (EditText) findViewById(com.alrais.alsouq.R.id.password);
		Signin = (Button) findViewById(com.alrais.alsouq.R.id.SignIn);
	}
	
	public void click(View v) {
		Intent intent = null;
		Forgotpassword = (TextView) findViewById(R.id.textView3);
		SignUp = (TextView) findViewById(R.id.signup);
		switch (v.getId()){
			case R.id.signup:
				SignUp.setTextColor(Color.BLUE);
				intent = new Intent(SignInActivity.this, SignupUser.class);
				startActivity(intent);
				break;
			case R.id.textView3:
				Forgotpassword.setTextColor(Color.GREEN);
				intent = new Intent(SignInActivity.this, ForgotPassword.class);
				startActivity(intent);
				break;	
			case R.id.SignIn:
				paramEmail = InputEmail.getText().toString();
			    paramPassword = InputPassword.getText().toString();	
				new AttemptLogin().execute();
				break;
		  }
	  }
	
//	public void SignIn(){
//		
//		ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
//		param.add(new BasicNameValuePair("email", paramEmail));
//		param.add(new BasicNameValuePair("password", paramPassword));
//		try {
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost("http://calicut.alraislabs.com:8002/shopinhand-php/login.php");
//			//String encoded_login = Base64.encodeToString(unp.getBytes(), Base64.NO_WRAP);
//			//httpPost.setHeader(new BasicHeader("Authorization", "Basic "+encoded_login));   
//			httpPost.setEntity(new UrlEncodedFormEntity(param));
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			Log.d("HTTP", "HTTP: OK");
//			
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e("HTTP", "Error in http connection " + e.toString());
//			Toast.makeText(getApplicationContext(), "Invalid IP Address",
//					Toast.LENGTH_LONG).show();
//		}
//		
//		try {
//			BufferedReader reader = new BufferedReader
//					(new InputStreamReader(is,"iso-8859-1"),8);
//		            StringBuilder sb = new StringBuilder();
//		            while ((line = reader.readLine()) != null)
//			    {
//		                sb.append(line + "\n");
//		            }
//		            is.close();
//		       result = sb.toString();
//			    Log.e("pass 2", "connection success ");
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			 Log.e("Fail 2", e.toString());
//		}
//		try{
//            JSONObject json_data = new JSONObject(result);
//            int code = (json_data.getInt("code"));
//			
//            if(code==1)
//            {
//		Toast.makeText(getBaseContext(), "Login Success",
//			Toast.LENGTH_SHORT).show();
//		Intent Accessgrant = new Intent(SignInActivity.this, MainActivity.class);
//		startActivity(Accessgrant);
//            }
//            else
//            {
//		 Toast.makeText(getBaseContext(), "Sorry, Try Again",
//			Toast.LENGTH_LONG).show();
//            }
//	}
//	catch(Exception e)
//	{
//            Log.e("Fail 3", e.toString());
//	}
//		
//	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignInActivity.this);
			pDialog.setMessage("Sign-IN");
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
				param.add(new BasicNameValuePair("email", paramEmail));
				param.add(new BasicNameValuePair("password", paramPassword));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						param);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				code = json.getInt("code");
				if (code == 1) {
					Log.d("Login Successful!", json.toString());
					// save user data
					SharedPreferences.Editor editor = getSharedPreferences("userpref", MODE_PRIVATE).edit();	
					editor.putString("username", paramEmail);
					editor.commit();
					
					Intent i = new Intent(SignInActivity.this, MainActivity.class);
					finish();
					startActivity(i);
					return json.getString(KEY_ERROR);
				} else {
					Log.d("Login Failure!", json.getString(KEY_ERROR_MSG));
					return json.getString(KEY_ERROR);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(SignInActivity.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}


}
