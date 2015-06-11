package com.example.quizdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShowQuestions extends Activity {

	private String quizid,timelimit;
	private TextView t1,time;
	private ListView lstView;	
	private ArrayList<String> questionid;
	private ArrayList<String> name;
	private ArrayList<String> questiontype;
	private ArrayList<String> questiontext;
	private ArrayList<String> defaultmarks;
	private ArrayList<Integer> dynamicid;
	private HashMap answers;
	private int inttime;
	
	private ArrayList<ArrayList<String>> marking = new ArrayList<ArrayList<String>>();
	private  ArrayList<ArrayList<String>> optionsStorage = new ArrayList<ArrayList<String>>();
	private  ArrayList<ArrayList<String>> userAnswersStorage = new ArrayList<ArrayList<String>>();

	private ArrayList<Float>finalMarksStore=new ArrayList<Float>();
	
	private ArrayList<Float> studentMarks;
	//private ArrayList<Float>
	//Toast.makeText(getApplicationContext(),""+marking.get(qno2).get(pos), Toast.LENGTH_LONG).show();
	
	 static String s="";
	
	private String NEW_URL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);
		
		t1=(TextView)findViewById(R.id.textView1);
		lstView = (ListView) findViewById(R.id.listView1);
		quizid = getIntent().getExtras().getString("quizid");
		time=(TextView)findViewById(R.id.time);
		timelimit = getIntent().getExtras().getString("timelimit");
		
		
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) 
			{
				try 
				{
					NEW_URL = getResources().getString(R.string.questiontype_url);
				} 
				catch (Exception e) 
				{
					e.getMessage();
				}
			
			
			/*	OnItemClickListener icl = new OnItemClickListener() {
				    public void onItemClick(AdapterView parent, View v, int position, long id) {
				        System.out.println("helloman"+quizid.get(position));
				        Intent inet=new Intent(getApplicationContext(),ShowQuestions.class);
				        inet.putExtra("quizid", quizid.get(position));
				        startActivity(inet);
				        
				    }
				};
				lstView.setOnItemClickListener(icl);
			*/	
				LoginTask lgn = new LoginTask();
				
					lgn.execute();
				
			}//if(activeN..
			
			else
			{
			Toast.makeText(getApplicationContext(), "Please check your internet connection and restart your application.", Toast.LENGTH_LONG).show();
			}
	}
	
	 //Class within a class
    private class LoginTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> 
    {
    	private URL url;
		//Intent inet;
		//Bundle bndl;

    	@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) 
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
				json.put("qid", quizid);
				

				writer.write(json.toString());
				writer.close();
				//JSON values sent(username and password)*/
				
				
				//Retrieving response..
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line = "";
			
				
			//String details="";<baz>
			
				ArrayList<String> details = new ArrayList<String>();  
				while ((line = reader.readLine()) != null) 
				{

					//JSONObject obj = new JSONObject(line);
					//Bundle helps in data transfer from 1 activity to another.
					//Storing the user id returned from the php page in a bundle
					
						//bndl.putString("userid", obj.getString("id"));
					
					
					//return obj.getString("status");
					details.add(line);
					
					
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

		protected void onPostExecute(ArrayList<String> details)
		{
				
			try 
			{
				
				
				
				
				//JSONObject json_reader = new JSONObject(details.get(7));
				//Toast.makeText(getApplicationContext(),json_reader.getString("type"), Toast.LENGTH_LONG).show();
				//TOASTED!
				

				//CountDownTimer(long millisInFuture, long countDownInterval)
		        
				inttime=Integer.parseInt(timelimit)*1000;
		        System.out.println(inttime);
		        new CountDownTimer(inttime, 1000) {

				     public void onTick(long millisUntilFinished) {
				         //Toast.makeText(getApplicationContext(), "Inside onTick", Toast.LENGTH_SHORT).show();
				    	 //time.setText("Time remaining: " + (millisUntilFinished / 1000));
				    	 String fmtTime=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
				    			    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - 
				    			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
				    			    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
				    			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
				    	 time.setText("Time remaining: " + fmtTime);
				     }

				     public void onFinish() {
				    	 //Toast.makeText(getApplicationContext(), "done!", Toast.LENGTH_LONG).show();
				    	 //txtView2.setText("DONE!!!!");
				    	 Toast.makeText(getApplicationContext(), "timer over", Toast.LENGTH_LONG).show();
				    	 // Intent intent=new Intent(getApplicationContext(), Completed.class);
				    	 //startActivity(intent);
				     }
				  }.start();
				
				/*String options[] = { details, details, details, details,
				   details };*/
				//String options[]={"1","1","1","","1"};
				
				
				//setting the layout..
				LinearLayout main_layout = (LinearLayout) findViewById(R.id.linear1);
				
				
				//note- index 0 ignored as it is used for the count of questions
				//note - the last \n from the php page adds as an extra index in the araylist that needs to be ignored.
				
				

				dynamicid = new ArrayList<Integer>();
				answers = new HashMap();
				
				for(int i=1,q_no=0;i<details.size()-1;i++,q_no++)
				{
					
					//q_no --> Question Number.. Increments as per new question display
					
						int a=details.size();
					//Toast.makeText(getApplicationContext(),"sa"+a, Toast.LENGTH_SHORT).show();
					final int qno2=q_no;
					//Welcoming our questions!!!
					//Escorted by JSON Hospitality Team
					JSONObject json_reader = new JSONObject(details.get(i));
					
					
					
					
					
					TextView title = new TextView(getApplicationContext());
					
					
					if(json_reader.getString("type").equals("shortanswer"))
					{
						ArrayList<String>questionGrades=new ArrayList<String>();
						ArrayList<String>questionOptions=new ArrayList<String>();
						
						finalMarksStore.add((float) 0.0);

						
						//Toast.makeText(getApplicationContext(),"essay!", Toast.LENGTH_SHORT).show();
						//print the question heading..
						title.setTextColor(Color.BLACK);
						title.setText("Q"+q_no+")"+json_reader.getString("text"));
						
						questionGrades.add(json_reader.getString("marks"));
						
						main_layout.addView(title);
						
						//put the edittext
						final EditText et=new EditText(getApplicationContext());
						et.setTextColor(Color.BLACK);
						main_layout.addView(et);
						//generate id
						dynamicid.add(q_no);
						//add the id of the view
						
						i++;//options
						json_reader = new JSONObject(details.get(i));
						for(int x=json_reader.length();x>0;x--)
						{
							
								questionOptions.add(0,json_reader.getString(""+(x)));
								//if(qno2==4)
								///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

								
								
						}
						
						
						i++;//grades
						
						json_reader = new JSONObject(details.get(i));
						//Toast.makeText(getApplicationContext(),""+json_reader.length(), Toast.LENGTH_LONG).show();

						for(int x=json_reader.length();x>0;x--)
						{
							
								questionGrades.add(0,json_reader.getString(""+(x)));
								//if(qno2==4)
								///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

								
								
								
						}
						
						//for(int x=0;x<questionGrades.size();x++)
						//	Toast.makeText(getApplicationContext(),questionGrades.get(x), Toast.LENGTH_SHORT).show();

						
						marking.add(questionGrades);
						optionsStorage.add(questionOptions);
						
						//Toast.makeText(getApplicationContext(),"short!", Toast.LENGTH_SHORT).show();


						
						
						
						
						
						et.setOnFocusChangeListener(new OnFocusChangeListener() {
							
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								String value=String.valueOf(et.getText());
								//System.out.println("VALUE ==>"+et.getText());
								// TODO Auto-generated method stub
								if(!hasFocus)
								{
									answers.put(qno2, value);
									ArrayList<String>userAnswers=new ArrayList<String>();
									userAnswers.add(value);
									/*for(int i=0;i<answers.size();i++)
									  {
										  Toast.makeText(getApplicationContext(),""+i+ answers.get(i), Toast.LENGTH_SHORT).show();
									  }
									  */
									//first time focus leave..so create arraylist
									if(userAnswersStorage.size()==qno2)
									{
										userAnswersStorage.add(userAnswers);
										//compare with options storage..if match then value goes in float score
										
										for(int ctr=0;ctr<optionsStorage.get(qno2).size();ctr++)
										{
											if(optionsStorage.get(qno2).get(ctr).equals(value))
											{
												finalMarksStore.set(qno2,Float.parseFloat(marking.get(qno2).get(ctr))*Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
													
											}
										}
										
									
									}
									//not first time focus leave..so edit arraylist
									else
									{
										int flg=0;
										for(int ctr=0;ctr<optionsStorage.get(qno2).size();ctr++)
										{
											if(optionsStorage.get(qno2).get(ctr).equals(value))
											{
												finalMarksStore.set(qno2,Float.parseFloat(marking.get(qno2).get(ctr))*Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
													flg=1;
											}
										}
										if(flg==0)
										{
											finalMarksStore.set(qno2,0.0f);

										}
										userAnswersStorage.set(qno2,userAnswers);
										//s+=value;
									}
									
								//	  Toast.makeText(getApplicationContext(),""+value, Toast.LENGTH_SHORT).show();

								}
								
								
							}
						});
					}//if
					
					
					
					
					
					
					
					
					else if(json_reader.getString("type").equals("essay"))
					{
						ArrayList<String>userAnswers=new ArrayList<String>();
						userAnswersStorage.add(userAnswers);
						
						ArrayList<String>questionOptions=new ArrayList<String>();//will be empty as grades dont have any values

						//Toast.makeText(getApplicationContext(),"essay!", Toast.LENGTH_SHORT).show();
						//print the question heading..
						finalMarksStore.add((float) 0.0);
						title.setTextColor(Color.BLACK);
						title.setText("Q"+q_no+")"+json_reader.getString("text")+"( Grade:"+Float.parseFloat(json_reader.getString("marks"))+" )");
						
						ArrayList<String>questionGrades=new ArrayList<String>();

						questionGrades.add(json_reader.getString("marks"));
						main_layout.addView(title);
						
						//put the edittext
						final EditText et=new EditText(getApplicationContext());
						et.setTextColor(Color.BLACK);
						main_layout.addView(et);
						//generate id
						dynamicid.add(q_no);
						//add the id of the view
						marking.add(questionGrades);
						optionsStorage.add(questionOptions);

						et.setOnFocusChangeListener(new OnFocusChangeListener() {
							
							

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								final String value=String.valueOf(et.getText());
								System.out.println("VALUE ==>"+et.getText());
								// TODO Auto-generated method stub
								if(!hasFocus)
								{
									answers.put(qno2, value);
									/*for(int i=0;i<answers.size();i++)
									  {
										  Toast.makeText(getApplicationContext(),""+i+ answers.get(i), Toast.LENGTH_SHORT).show();
									  }
									  */
								}
							}
						});
						

					}//if
					
					else if(json_reader.getString("type").equals("multichoice"))
					{
						ArrayList<String>userAnswers=new ArrayList<String>();
						userAnswersStorage.add(userAnswers);
						
						finalMarksStore.add((float) 0.0);
						ArrayList<String>questionGrades=new ArrayList<String>();
						
						ArrayList<String>questionOptions=new ArrayList<String>();

						

						questionGrades.add(json_reader.getString("marks"));
						
						//Toast.makeText(getApplicationContext(),"radio!", Toast.LENGTH_SHORT).show();
						//print the question heading..
						title.setTextColor(Color.BLACK);
						title.setText("Q"+q_no+")"+json_reader.getString("text"));
						main_layout.addView(title);
						
						//get the options.The options are in the next JSON.
						i++;
						json_reader = new JSONObject(details.get(i));
						
						//setting the radio button environment..
						 final RadioButton[] rb = new RadioButton[json_reader.length()];
						 final RadioGroup rg = new RadioGroup(getApplicationContext());
						 rg.setOrientation(RadioGroup.VERTICAL);
						   
						 
						//storing options...
							for(int x=json_reader.length();x>0;x--)
							{
								
									questionOptions.add(0,json_reader.getString(""+(x)));
									//if(qno2==4)
									///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

									
									
							}
							
						 //here j is the number of options
						 for (int j = 0; j < json_reader.length(); j++) 
						 {
						    rb[j] = new RadioButton(getApplicationContext());
						    rg.addView(rb[j]);
						    rb[j].setText(json_reader.getString(Integer.toString(j+1)));
						    rb[j].setTextColor(Color.BLACK);
						 
						 }
						 //incremenet for the grades json
						 i++;
							json_reader = new JSONObject(details.get(i));

						 //for(int x=json_reader.length();x>0;x--)
						//	if(qno2==4)
						//	Toast.makeText(getApplicationContext(), ""+json_reader.length(), Toast.LENGTH_SHORT).show();

						for(int x=json_reader.length();x>0;x--)
						{
							
								questionGrades.add(0,json_reader.getString(""+(x)));
								//if(qno2==4)
								///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

								
								
						}
						 marking.add(questionGrades);
						optionsStorage.add(questionOptions);

						 main_layout.addView(rg);
						//dynamicid.add(q_no);
						
						rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							int pos;
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								// TODO Auto-generated method stub
							
								pos=rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
							//	Toast.makeText(getApplicationContext(), "RD "+String.valueOf(pos), Toast.LENGTH_SHORT).show();
							//	Toast.makeText(getApplicationContext(), "RD "+rb[pos].getText(), Toast.LENGTH_SHORT).show();
							//	Toast.makeText(getApplicationContext(), "RD "+qno2, Toast.LENGTH_SHORT).show();
							//	String value=(String)rb[pos].getText();
								//answers.put(qno2,value);
								
							
							//Toast.makeText(getApplicationContext(),"question-number-"+qno2, Toast.LENGTH_LONG).show();
							/*
							 The position of the radio button (pos) should match with the index of the inner arraylist,
							 represented by the outer arraylist called marking,at position qno2-1
							 */
	
								
							finalMarksStore.set(qno2,Float.parseFloat(marking.get(qno2).get(pos))*Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
							//Toast.makeText(getApplicationContext(),""+marking.get(qno2).get(pos), Toast.LENGTH_LONG).show();
							
							
						//	Toast.makeText(getApplicationContext(),"Question number qno2="+qno2, Toast.LENGTH_SHORT).show();
						//	Toast.makeText(getApplicationContext(),"Arraylist Size"+marking.get(qno2).size(), Toast.LENGTH_SHORT).show();
						//	Toast.makeText(getApplicationContext(),"Overall Size"+marking.size(), Toast.LENGTH_SHORT).show();

							//Toast.makeText(getApplicationContext(),""+finalMarksStore.get(qno2), Toast.LENGTH_LONG).show();

								
							}
						});
						
					}
					
					else if(json_reader.getString("type").equals("checkbox"))
					{
						ArrayList<String>userAnswers=new ArrayList<String>();
						userAnswersStorage.add(userAnswers);
						
						finalMarksStore.add((float) 0.0);
						ArrayList<String>questionGrades=new ArrayList<String>();
						ArrayList<String>questionOptions=new ArrayList<String>();


						questionGrades.add(json_reader.getString("marks"));
						//Toast.makeText(getApplicationContext(),"checkbox!", Toast.LENGTH_SHORT).show();
						//print the question heading..
						title.setTextColor(Color.BLACK);
						title.setText("Q"+q_no+")"+json_reader.getString("text"));
						main_layout.addView(title);
						i++;
						json_reader = new JSONObject(details.get(i));
						
						for(int x=json_reader.length();x>0;x--)
						{
							
								questionOptions.add(0,json_reader.getString(""+(x)));
								//if(qno2==4)
								///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

								
								
						}
						
						
						
						CheckBox cb=null;
					 	for (int j = 0; j < json_reader.length(); j++) 
						{
					 		cb = new CheckBox(getApplicationContext());
					 		cb.setId(j+1);
					 		cb.setText(json_reader.getString(Integer.toString(j+1)));
					 		cb.setTextColor(Color.BLACK);
					 		main_layout.addView(cb);
						
						//dynamicid.add(q_no);
						
						final CheckBox cb2 = cb;
						cb.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String s="";
								boolean check = ((CheckBox) v).isChecked();
								if(check){
									//in getid id starts from 1 so make it 0
								//Toast.makeText(getApplicationContext(),""+cb2.getId(), Toast.LENGTH_LONG).show();
								//Toast.makeText(getApplicationContext(), cb2.getText().toString(), Toast.LENGTH_LONG).show();
								//s+=cb2.getId()+"->"+cb2.getText().toString();
									
								//	Toast.makeText(getApplicationContext(),"checked->"+qno2+"->"+(cb2.getId()-1), Toast.LENGTH_SHORT).show();
									//Toast.makeText(getApplicationContext(),""+marking.get(qno2).get(cb2.getId()-1), Toast.LENGTH_SHORT).show();
									finalMarksStore.set(qno2,(  finalMarksStore.get(qno2)+ Float.parseFloat(marking.get(qno2).get(cb2.getId()-1))));
									if(finalMarksStore.get(qno2)>=0.95f)
										finalMarksStore.set(qno2,1.0f);
									finalMarksStore.set(qno2,finalMarksStore.get(qno2) * Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
									//   *  Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
									//Toast.makeText(getApplicationContext(),""+finalMarksStore.get(qno2), Toast.LENGTH_SHORT).show();

								}//if
								else{
									//Toast.makeText(getApplicationContext(),"unchecked->"+qno2+"->"+(cb2.getId()-1), Toast.LENGTH_SHORT).show();
									finalMarksStore.set(qno2,finalMarksStore.get(qno2)- Float.parseFloat(marking.get(qno2).get(cb2.getId()-1)));// *  Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
									// the above line gets the score in decimals .ie 0.3 0.5 1.0(full) sometimes it is 0.9...convert it to 1
									if(finalMarksStore.get(qno2)>=0.95f)
										finalMarksStore.set(qno2,1.0f);
									finalMarksStore.set(qno2,finalMarksStore.get(qno2) * Float.parseFloat(marking.get(qno2).get(marking.get(qno2).size()-1)));
								//	Toast.makeText(getApplicationContext(),""+finalMarksStore.get(qno2), Toast.LENGTH_SHORT).show();
								//	s+=cb2.getId()+"-> ";
									
								}
								//for(int i=1;i<=chkAns.size();i++)
								//Toast.makeText(getApplicationContext(),"String answers "+ s, Toast.LENGTH_LONG).show();
							}
						});
						
						}//for
						
						
						 //incremenet for the grades json
						 i++;
							json_reader = new JSONObject(details.get(i));

							/*for(int x=1;x<=json_reader.length();x++)
							{
									questionGrades.add(json_reader.getString(""+(x)));
									
							}*/
							
							for(int x=json_reader.length();x>0;x--)
							{
								
									questionGrades.add(0,json_reader.getString(""+(x)));
									//if(qno2==4)
									///Toast.makeText(getApplicationContext(),""+x+" "+ json_reader.getString(""+(x)), Toast.LENGTH_SHORT).show();

									
									
							}
							 marking.add(questionGrades);
							optionsStorage.add(questionOptions);

							
							
							
						
								
						
					}
						
					else
					{
						
						Toast.makeText(getApplicationContext(),"umm...Error!"+json_reader.getString("type"), Toast.LENGTH_SHORT).show();
						
					}
					
					
					
					
					
					
						//Toast.makeText(getApplicationContext(),details.get(i), Toast.LENGTH_LONG).show();
					
						
					
					
				}//for loop
				
				
			/*	for (int i=0;i<marking.size();i++)
				{
					for(int x=0;x<marking.get(i).size();x++)	
					Toast.makeText(getApplicationContext(),""+x+""+marking.get(i).get(x), Toast.LENGTH_SHORT).show();
	
					
				}*/
				
				
				//	Toast.makeText(getApplicationContext(),""+finalMarksStore.size(), Toast.LENGTH_LONG).show();
	
					
					
				 /*
				 for (int i = 1; i <= 10; i++)
				  {
					  TextView title = new TextView(getApplicationContext());
					 switch(i)
					 {
					 	case 1:
						   //create text button
						 
						   title.setText("Q"+i+")Radio Buttons:-");
						   main_layout.addView(title);
						   
						   
						   // create radio button
						   final RadioButton[] rb = new RadioButton[5];
						   RadioGroup rg = new RadioGroup(getApplicationContext());
						   rg.setOrientation(RadioGroup.VERTICAL);
						   
						   //here j is the number of options
						   for (int j = 0; j < 5; j++) 
						   {
						    rb[j] = new RadioButton(getApplicationContext());
						    rg.addView(rb[j]);
						    rb[j].setText(options[j]);
						    rb[j].setTextColor(Color.BLACK);
						 
						   }
						   main_layout.addView(rg);
						   
						   break;
						   
						   
					 
					 	case 2:
						   //create text button
						  // TextView title = new TextView(this);
						   title.setText("Q"+i+")EditText:-");
						   main_layout.addView(title);
						   EditText et=new EditText(getApplicationContext());
						   et.setTextColor(Color.BLACK);
						   main_layout.addView(et);
						   
						   
						 break;
						 
					 	case 3:
					 		title.setText("Q"+i+")CheckBox:-");
							   main_layout.addView(title);
							  
							   
							   CheckBox cb=null;
						 		for (int j = 0; j < 5; j++) 
								{
						 			cb = new CheckBox(getApplicationContext());
						 		   cb.setId(j+1);
						 		   cb.setText(options[j]);
						 		  cb.setTextColor(Color.BLACK);
						 		  main_layout.addView(cb);
								}
							   
					 		
					 		
					 		
					 	break;
						 
					   
					  }
					  
				  }//for loop*/
					 
					  Button b1=new Button(getApplicationContext());
					  b1.setFocusable(true);
				      b1.setFocusableInTouchMode(true);///add this line
				
					  b1.setText("Submit All and Finish");
					  main_layout.addView(b1);
				
				OnClickListener l1 = new OnClickListener() {
					 
					@Override
					public void onClick(View v) {
					     v.requestFocus();
						// TODO Auto-generated method stub
					//	Toast.makeText(getApplicationContext(), "clicked dynamic id "+dynamicid.size()+"answers "+answers.size()
						//		, Toast.LENGTH_SHORT).show();
						
						/*for(int k=0;k<answers.size();k++)
						{
							//Toast.makeText(getApplicationContext(),"Ques ID: "+dynamicid.get(k), Toast.LENGTH_SHORT).show();
							
							Toast.makeText(getApplicationContext(),"Answer: "+answers.get(k), Toast.LENGTH_SHORT).show();
							
						}*/
					     
					     
					     
					     for(int i=0;i<optionsStorage.size();i++)
							{
					    	 for(int j=0;j<optionsStorage.get(i).size();j++)
								{
					    		// Toast.makeText(getApplicationContext(),""+i+"->"+optionsStorage.get(i).get(j),Toast.LENGTH_SHORT).show();
								}

							}
					     
					     
					     
					     for(int i=0;i<userAnswersStorage.size();i++)
							{
					    	 for(int j=0;j<userAnswersStorage.get(i).size();j++)
								{
					    		 	
						    	//	Toast.makeText(getApplicationContext(),""+i+"->"+userAnswersStorage.get(i).get(j),Toast.LENGTH_SHORT).show();

								}
						
							}
					     
					     
					  
					     
					     
						
						int total=0;
						for(int i=0;i<marking.size();i++)
						{
							total+=Math.round(Float.parseFloat((marking.get(i).get(marking.get(i).size()-1))));
						//	Toast.makeText(getApplicationContext(),""+i+"->"+marking.get(i).get(marking.get(i).size()-1), Toast.LENGTH_SHORT).show();

						}
						
						//Toast.makeText(getApplicationContext(),""+s, Toast.LENGTH_LONG).show();

						
						int userScore = 0;
						for(int j=0;j<finalMarksStore.size();j++)
						{
							userScore+=Math.round((finalMarksStore.get(j)));
							Toast.makeText(getApplicationContext(),""+j+"->"+finalMarksStore.get(j), Toast.LENGTH_SHORT).show();

						}
						
						Toast.makeText(getApplicationContext(),"Score: "+userScore+"/"+total, Toast.LENGTH_LONG).show();
						//Passing all values to next activity to enter in Database
						Intent inet=new Intent(getApplicationContext(),PostTest.class);
						inet.putExtra("quizid", getIntent().getExtras().getString("quizid"));
						inet.putExtra("userid", getIntent().getExtras().getString("userid"));
						inet.putExtra("grade", userScore);
						startActivity(inet);

						
					}
				};
				b1.setOnClickListener(l1);
				
					  
				//Toast.makeText(getApplicationContext(),
					//	details, Toast.LENGTH_LONG).show();
				/*
				t1.setText("Questions:\n"+details);
				if(details!=null)
				{
					try
					{
						JSONObject o1=new JSONObject(details);
						JSONObject o3;
						Iterator it = (Iterator) o1.keys();
						System.out.println("try1" + o1.keys());
						name = new ArrayList<String>();
						questionid = new ArrayList<String>();
						questiontype= new ArrayList<String>();
						questiontext = new ArrayList<String>();
						defaultmarks = new ArrayList<String>();
						

						while (it.hasNext()) {
							Object o2 = it.next();
							questionid.add(o2.toString());
							o3= new JSONObject(o1.getString(o2.toString()));
							name.add(o3.getString("name"));
							defaultmarks.add(o3.getString("defaultmark"));
							questiontext.add(o3.getString("questiontext"));
							questiontype.add(o3.getString("questiontype"));
							System.out.println("try5" + questionid+"name="+name + "text=" +questiontext + "type=" + questiontype +"marks=" + defaultmarks );
							
						}
				
				
			}
					catch(Exception e)
					{
						System.out.println("try5"+e);
					
						e.printStackTrace();
					}
				}*/
				 
			} //try
			catch (Exception e) 
			{
				Toast.makeText(getApplicationContext(),
						"Wrong credsdfdsentials. Please try again" +e,
						Toast.LENGTH_LONG).show();
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