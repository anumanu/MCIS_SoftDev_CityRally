package com.example.cityrally.activities;

import java.util.List;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;
import com.example.cityrally.model.CheckPoint;
import com.example.cityrally.model.DatabaseHandler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CluesActivity extends ActionBarActivity {

	ListView cluesListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clues);
		
		cluesListView = (ListView) findViewById(R.id.cluesListView);
		
		
		DatabaseHandler db = new DatabaseHandler(this);
		List<String> clues = db.getSolvedClues(false); 
		
		
		//String[] myStringArray= (String[]) clues.toArray(); //{"a","b","c"};
	    ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clues); 
	    
	   //to tell what to list vew
	   
	    cluesListView.setAdapter(adapter);
	   //Handling click events
	        
	   	//cluesListView.setOnItemClickListener(new OnItemClickListener() {
	    
	   		//public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
	        
	   		//	textView.setText(items[myItemInt]); 
	   			
	   			//}
	        //});
	        
		//make a list and initialize isSolved data. 
	    //Create a method in DBheandler getSolvedCheckPoints().
		
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clues, menu);
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
