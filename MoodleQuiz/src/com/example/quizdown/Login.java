package com.example.quizdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity 
{
	
	private Button btnLogin;
	private EditText edtTextUserName;
	private EditText edtTextPassword;
	private SharedPreferences pref;
	private String NEW_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) 
		{
			try 
			{
				NEW_URL = getResources().getString(R.string.moodle_url);
			} 
			catch (Exception e) 
			{
				e.getMessage();
			}
		
		// Setting up buttons and boxes..
			btnLogin = (Button) findViewById(R.id.login_button);
			edtTextUserName = (EditText) findViewById(R.id.username);
			edtTextPassword = (EditText) findViewById(R.id.password);

		// for storing credentials into internal memory
			pref = getApplicationContext().getSharedPreferences("MoodleCredentials", 0);
			
			if (!pref.getString("username", "null").equals("null")) 
			{
						LoginTask lgn = new LoginTask();
						lgn.execute(pref.getString("username", "null"),
						pref.getString("password", "null"));
			}

				btnLogin.setOnClickListener(new OnClickListener() 
				{
	
						@Override
						public void onClick(View arg0) 
						{
								LoginTask lgn = new LoginTask();
								String user = edtTextUserName.getText().toString();
								String pass = edtTextPassword.getText().toString();
								lgn.execute(user, pass);
						}
						
				});//setonclicklistener
			
		}//if(activeN..
		
		else
		{
		Toast.makeText(getApplicationContext(), "Please check your internet connection and restart your application.", Toast.LENGTH_LONG).show();
		}
		
    }//onCreate()
    
    //Class within a class
    private class LoginTask extends AsyncTask<String, Void, String> 
    {
    	private URL url;
		Intent inet;
		Bundle bndl;

		@Override
		protected String doInBackground(String... arg0) 
		{

			try 
			{
				//inet = new Intent(Login.this, SelectAvtivity.class);
				
				//Establishing connection to PHP Page
				bndl = new Bundle();
				url = new URL(NEW_URL);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				connection.setDoInput(true);
				connection.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(
						connection.getOutputStream());
				JSONObject json = new JSONObject();
				json.put("username", arg0[0]);
				json.put("password", arg0[1]);

				writer.write(json.toString());
				writer.close();
				//JSON values sent(username and password)
				
				
				//Retrieving response..
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line = "";
			
				
			
				   
				while ((line = reader.readLine()) != null) 
				{

					JSONObject obj = new JSONObject(line);
					//Bundle helps in data transfer from 1 activity to another.
					//Storing the user id returned from the php page in a bundle
					
						bndl.putString("userid", obj.getString("id"));
					
					
					return obj.getString("status");
					//details+=line;
					
					
				}
				//return details;
				
				
				//reader.close();
			}//try
			catch (MalformedURLException e) 
			{
				e.printStackTrace();

			} 
			
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			return null;


		}//doInBackground

		protected void onPostExecute(String details)
		{
				
			try 
			{
				
				
				if (details.toString().equals("success")) 
				{
					Toast.makeText(getApplicationContext(),
							"Login successful", Toast.LENGTH_LONG).show();
					
					//Committing username and password data into internal memory.
					//doing this so that if the credentials are correct,
					//it will log in automatically.
					
					Editor editor = pref.edit();
					editor.putString("username", edtTextUserName.getText()
							.toString());
					editor.putString("password", edtTextPassword.getText()
							.toString());
					editor.commit();
					
					//Login task is complete.
					//Sending user id to the next activity
					
					inet = new Intent(Login.this, SelectCourse.class);
					
					inet.putExtras(bndl);
					startActivity(inet);
					finish();

				} 
				else 
				{
					Toast.makeText(getApplicationContext(),
							"Wrong credentials. Please try again",
							Toast.LENGTH_SHORT).show();
				}
				
			} //try
			catch (Exception e) 
			{
				Toast.makeText(getApplicationContext(),
						"Wrong credsdfdsentials. Please try again",
						Toast.LENGTH_SHORT).show();
				// TODO: handle exception
			}

		}//onPostExecute
	}//LoginTask
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
