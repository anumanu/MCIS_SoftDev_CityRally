package com.example.cityrally.activities;


import java.util.List;

import junit.framework.Test;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;
import com.example.cityrally.model.CheckPoint;
import com.example.cityrally.model.DatabaseHandler;
import com.google.android.gms.internal.db;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private TextView cluesCount;
	
	private Intent mapIntent;
	private Intent cluesIntent;
	private Intent misteryIntent;
	
	private Button mapBtn;
	private Button cluesBtn;
	private Button misteryBtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // initialise the Database
        initDatabase();
        
        cluesCount = (TextView) findViewById(R.id.scoreText);
        
        mapBtn = (Button) findViewById(R.id.mapBtn);
        cluesBtn = (Button) findViewById(R.id.cluesBtn);
        misteryBtn = (Button) findViewById(R.id.misteryBtn);
        
        mapBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 mapIntent = new Intent(getApplicationContext(), MapActivity.class); 
				 startActivity(mapIntent);
				
			}
		});
        
        cluesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cluesIntent = new Intent(getApplicationContext(), CluesActivity.class);
				startActivity(cluesIntent);
				
			}
		});
        
        misteryBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// will open Mistery Intent only if all challenges had been solved or given up
				if(AllChallangesAreSolved()) {
		    			misteryIntent = new Intent(getApplicationContext(), MisteryActivity.class);
		    			startActivity(misteryIntent);
		    		} else {
		    			Toast.makeText(MainActivity.this, "You need to solve all challanges!", Toast.LENGTH_SHORT).show();
		    		}
			}
		});
        
        updateSolvedCluesCount();
        
    }
 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    @Override
    protected void onResume() {
    	System.out.println("onResume --------");
    	updateSolvedCluesCount();
    	super.onResume();
    }
    
    private void initDatabase() {
    	DatabaseHandler db = new DatabaseHandler(this);

    	if (db.getCheckPointCount() == 0) {
    	
	    	db.addCheckPoint(new CheckPoint(1, 49.610465,  6.130872, "c1", "- It is over 100 years old. -",  false, false)); 
	    	db.addCheckPoint(new CheckPoint(2, 49.609275,  6.129372, "c2", "- It has a clone in Philadelphia. -",  false, false));  
	    	db.addCheckPoint(new CheckPoint(3, 49.628897,  6.214764, "c3", "- It crosses Pétrusse river. -",  false, false));  
	    	db.addCheckPoint(new CheckPoint(4, 49.626244,  6.158729, "c4", "- It was named after one of the Grand Duke of Luxembourg. -", false, false));  
	     
    	}
        // Reading all checkpoints
        Log.d("Reading: ", "Reading all CheckPoints.."); 
        List<CheckPoint> checkPoint = db.getAllCheckPoint();       
         
        for (CheckPoint cn : checkPoint) {
            String log = "Id: "+cn.getId()+" ,Challange: " + cn.getChallangeID() + " ,Latitude: " + cn.getLatitude() + " ,Longitude: " + cn.getLongitude() + " ,Clue: " + cn.getClue() + " ,Solved: " + cn.isSolved();
        // Writing Contacts to log
        Log.d("Challenge: ", log);
        }
    }
    
    // will return true if all challenges are solved
    private boolean AllChallangesAreSolved(){
    	DatabaseHandler db = new DatabaseHandler(this);
    	
    	if(db.getSolvedClues(true).size() == db.getCheckPointCount()) 
        	return true;
	
		return false;
    }
    
    // will show the total number and the number of solved/gave up challenges on the mane page
    private void updateSolvedCluesCount() {
    	DatabaseHandler db = new DatabaseHandler(this);
		//List<String> clues = db.getSolvedClues(); 
		int clues = db.getSolvedCluesCount();
    	
		cluesCount.setText(clues + "/4");
    }
    
    
}
