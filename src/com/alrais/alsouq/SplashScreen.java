package com.alrais.alsouq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alrais.ui.SignInActivity;

public class SplashScreen  extends Activity{
	
	 private static int SPLASH_TIME_OUT = 3000;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		SharedPreferences userCredentials = getApplicationContext().getSharedPreferences("userpref", MODE_PRIVATE);
		final String email = userCredentials.getString("username", null );
		//Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				if(email!=null){
					Intent home = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(home);
				}
				else{
				Intent i = new Intent(SplashScreen.this,SignInActivity.class);
				startActivity(i);}
				finish();
			}
		},SPLASH_TIME_OUT);
			
		}
	}


