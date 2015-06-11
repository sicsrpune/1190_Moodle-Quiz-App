package com.example.quizdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectCourse extends Activity {

	private String userid;
	private TextView t1;
	private ListView lstView;	
	private ArrayList<String> cid;
	private ArrayList<String> name;
	private String NEW_URL;
	
	Intent inet;
	Bundle bndl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_course);
		
		t1=(TextView)findViewById(R.id.textView1);
		
		userid = getIntent().getExtras().getString("userid");
		lstView = (ListView) findViewById(R.id.listView1);
		
		
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) 
			{
				try 
				{
					NEW_URL = getResources().getString(R.string.course_url);
				} 
				catch (Exception e) 
				{
					e.getMessage();
				}
			
			

				OnItemClickListener icl = new OnItemClickListener() {
				    public void onItemClick(AdapterView parent, View v, int position, long id) {
				        System.out.println("helloman"+cid.get(position));
				        Intent inet=new Intent(getApplicationContext(),ShowQuiz.class);
				        inet.putExtra("courseid", cid.get(position));
				        inet.putExtra("userid", userid);
				        startActivity(inet);
				        
				    }
				};

				lstView.setOnItemClickListener(icl);
				LoginTask lgn = new LoginTask();
				
					lgn.execute();
					
			}//if(activeN..
			
			else
			{
			Toast.makeText(getApplicationContext(), "Please check your internet connection and restart your application.", Toast.LENGTH_LONG).show();
			}
	}
	
	 //Class within a class
    private class LoginTask extends AsyncTask<String, Void, String> 
    {
    	private URL url;
		//Intent inet;
		//Bundle bndl;

		@Override
		protected String doInBackground(String... arg0) 
		{

			try 
			{
				//inet = new Intent(Login.this, SelectAvtivity.class);
				
				//Establishing connection to PHP Page
				//bndl = new Bundle();
				url = new URL(NEW_URL);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				connection.setDoInput(true);
				connection.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(
						connection.getOutputStream());
				JSONObject json = new JSONObject();
				json.put("id", userid);
				

				writer.write(json.toString());
				writer.close();
				//JSON values sent(username and password)*/
				
				
				//Retrieving response..
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line = "";
			
				
			String details="";
				   
				while ((line = reader.readLine()) != null) 
				{

					//JSONObject obj = new JSONObject(line);
					//Bundle helps in data transfer from 1 activity to another.
					//Storing the user id returned from the php page in a bundle
					
						//bndl.putString("userid", obj.getString("id"));
					
					
					//return obj.getString("status");
					details+=line;
					
					
				}
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


		}//doInBackground

		protected void onPostExecute(String details)
		{
				
			try 
			{
				//Toast.makeText(getApplicationContext(),
					//	details, Toast.LENGTH_LONG).show();
				
//				t1.setText("Courses:\n"+details);
				
				if(details!=null)
				{//flagCourseCaptured=true;
					try
					{
						JSONObject o1=new JSONObject(details);
						Iterator it = (Iterator) o1.keys();
						System.out.println("try1" + o1.keys());
						name = new ArrayList<String>();
						cid = new ArrayList<String>();

						while (it.hasNext()) {
							Object o2 = it.next();
							cid.add(o2.toString());
							name.add(o1.getString(o2.toString()));
							System.out.println("try2" + cid+""+name);
						}
//						CustomAdapter adapter = new CustomAdapter(
//								getApplicationContext(), Integer.parseInt(string)name);
//						lstView.setAdapter(adapter);
						   ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCourse.this,
							          android.R.layout.simple_list_item_1,name);
							 
							           // Assign adapter to ListView
							          lstView.setAdapter(adapter); 

					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				/*if (details.toString().equals("success")) 
				{
					Toast.makeText(getApplicationContext(),
							"Login successful", Toast.LENGTH_LONG).show();
					
					//Committing username and password data into internal memory.
					//doing this so that if the credentials are correct,
					//it will log in automatically.
					/*
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
				}*/
//				b1.setOnClickListener(new OnClickListener()
//				{
//	
//						@Override
//						public void onClick(View arg0) 
//						{
//							inet = new Intent(SelectCourse.this, ShowQuiz.class);
//							
//							inet.putExtras(bndl);
//							startActivity(inet);
//							finish();
//						}
//				});
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
		getMenuInflater().inflate(R.menu.select_course, menu);
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
