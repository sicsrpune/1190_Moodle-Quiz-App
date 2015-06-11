package com.example.quizdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PostTest extends Activity {
    private String quizid;
    private String userid;
    private int grade;
	private String NEW_URL;
	private Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_test);
		
		b1=(Button)findViewById(R.id.button1);
		
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) 
			{
				try 
				{
					NEW_URL = getResources().getString(R.string.posttest_url);
				} 
				catch (Exception e) 
				{
					e.getMessage();
				}
				OnClickListener icl2=new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent inet=new Intent(getApplicationContext(),Login.class);
				        startActivity(inet);
					}
				};
				b1.setOnClickListener(icl2);
		      quizid = getIntent().getExtras().getString("quizid");
	          userid = getIntent().getExtras().getString("userid");
		      grade = getIntent().getExtras().getInt("grade");
		      InsertRow r1=new InsertRow();
		      r1.execute();
			}
	}
		private class InsertRow extends AsyncTask
		    {
		    	private URL url;
				//Intent inet;
				//Bundle bndl;

		   
				@Override
				protected Object doInBackground(Object... params) {

					try 
					{
						
						url = new URL(NEW_URL);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();

						connection.setDoInput(true);
						connection.setDoOutput(true);
						OutputStreamWriter writer = new OutputStreamWriter(
								connection.getOutputStream());
						JSONObject json = new JSONObject();
						json.put("qid", quizid);
						json.put("uid", userid);
						json.put("grade",grade);
						json.put("timemodified", System.currentTimeMillis() / 1000L);
						

						writer.write(json.toString());
						writer.close();
						//JSON values sent(username and password)*/
						
						
						//Retrieving response..
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(connection.getInputStream()));
						String line = "";
					
						
					
						ArrayList<String> details = new ArrayList<String>();  
						while ((line = reader.readLine()) != null) 
						{						
							details.add(line);
						}				
						System.out.println("details in posttest" + details);
						return details;
						
						
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
					
					/*catch (JSONException e) 
					{
						e.printStackTrace();
					}*/
					
					catch (Exception e) 
					{
						e.printStackTrace();
					}

					return null;


					
					
				}
		    }
	
}
