package com.example.cityrally.activities;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;
import com.example.cityrally.model.CheckPoint;
import com.example.cityrally.model.DatabaseHandler;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChallengeActivity extends ActionBarActivity {

	private int checkPointID;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int cid = getIntent().getIntExtra("checkPointID", 0);
	
		System.out.println("We get following input: ------" + cid);
		
		this.checkPointID = cid;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge, menu);
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

	public int getCheckPointID() {
		return checkPointID;
	}
	
	public void completeChallenge() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		checkPoint.setSolved(true);
		
		db.updateCheckPoint(checkPoint);
	
		System.out.println("Challenge completed: " + checkPoint.getId());
	}
	
	public void giveUpTheChallenge() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		checkPoint.setSolved(true);
		checkPoint.setGiveUp(true);
		
		db.updateCheckPoint(checkPoint);
	
		System.out.println("You gave up the challenge: " + checkPoint.getId());
	}
	
	private void showAlertDialog(String title, String message) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(title);
		builder1.setMessage(message);
		builder1.setCancelable(true);
		builder1.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		    	setResult(RESULT_OK, intent);
		        finish();
		    }
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	
	public void showAlertDialogSolved() {
		DatabaseHandler db = new DatabaseHandler(this);		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		String title = "Congratulation!";
		String message = "You guessed the right answer. The clue is:\n"+ checkPoint.getClue();
		
		this.showAlertDialog(title, message);
	}
	
	public void showAlertDialogGaveUp() {
		String title = ":(";
		String message = "No clues for those who give up!";
		
		this.showAlertDialog(title, message);
	}
	/*
	public void showAlertDialog(){
		
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle("Congratulation!");
		builder1.setMessage("You guessed the right answer. The clue is:\n"+ checkPoint.getClue());
		builder1.setCancelable(true);
		builder1.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		        
		    	//dialog.cancel();
		    	//intent = new Intent();
		    	setResult(RESULT_OK, intent);
		        finish();
		    }
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	
	public void showAlertDialogNoClue(){
		
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(":( ");
		builder1.setMessage("No clues for those who give up!");
		builder1.setCancelable(true);
		builder1.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		        
		    	//dialog.cancel();
		    	//intent = new Intent();
		    	setResult(RESULT_OK, intent);
		        finish();
		    }
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}*/
}
