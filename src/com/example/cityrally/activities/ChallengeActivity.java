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

	// returns checkPoint ID
	public int getCheckPointID() {
		return checkPointID;
	}
	
	//will set isSolved from false to true in Database
	public void completeChallenge() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		checkPoint.setSolved(true);
		
		db.updateCheckPoint(checkPoint);
	
		//System.out.println("Challenge completed: " + checkPoint.getId());
	}
	
	//will set isSolved from false to true and also isGiveUp from false to true
	public void giveUpTheChallenge() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		checkPoint.setSolved(true);
		checkPoint.setGiveUp(true);
		
		db.updateCheckPoint(checkPoint);
	
		System.out.println("You gave up the challenge: " + checkPoint.getId());
	}
	
	// building a message
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
	
	// message if the challenge is solved
	public void showAlertDialogSolved() {
		DatabaseHandler db = new DatabaseHandler(this);		
		CheckPoint checkPoint = db.getCheckPoint(this.checkPointID);
		
		String title = "Congratulation!";
		String message = "You guessed the right answer. The clue is:\n"+ checkPoint.getClue();
		
		this.showAlertDialog(title, message);
	}
	
	// message if the challenge is give up
	public void showAlertDialogGaveUp() {
		String title = ":(";
		String message = "No clues for those who give up!";
		
		this.showAlertDialog(title, message);
	}
	
}
