package com.example.cityrally.activities;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;
import com.example.cityrally.model.CheckPoint;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ChallengeFourActivity extends ChallengeActivity {
	
	private CheckBox chbox1;
	private CheckBox chbox2;
	private CheckBox chbox3;
	private CheckBox chbox4;
	
	private Button answer;
	private Button giveUpBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_four);
		
		answer = (Button) findViewById(R.id.button1);
		giveUpBtn = (Button) findViewById(R.id.btnGiveUp);
		
		chbox1 = (CheckBox) findViewById(R.id.chbox1);
		chbox2 = (CheckBox) findViewById(R.id.chbox2);
		chbox3 = (CheckBox) findViewById(R.id.chbox3);
		chbox4 = (CheckBox) findViewById(R.id.chbox4);
		
		answer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (chbox1.isChecked() && chbox3.isChecked() && chbox4.isChecked() && !chbox2.isChecked())
				{
					completeChallenge();
					showAlertDialogSolved();
					//Toast.makeText(ChallengeFourActivity.this, "Your answer is right.", Toast.LENGTH_LONG).show();
					//unChecked();
					//unChecked();
					
				} else {
					Toast.makeText(ChallengeFourActivity.this, "Your answer is wrong.", Toast.LENGTH_LONG).show();
					unChecked();
					
				}
				
				
			}
		});
		giveUpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				giveUpTheChallenge();
				showAlertDialogGaveUp();
			}
		});
		
	}
	
	public void unChecked(){
		chbox2.setChecked(false);
		chbox1.setChecked(false);
		chbox3.setChecked(false);
		chbox4.setChecked(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge_four, menu);
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
