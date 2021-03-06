package com.example.cityrally.activities;

import com.example.cityrally.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class ChallangeOneActivity extends ChallengeActivity {

	private Button answerBtn;
	private Button giveUpBtn;
	
	private RadioButton rb0;
	private RadioButton rb1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challange_one);
		
		answerBtn = (Button) findViewById(R.id.button1);
		giveUpBtn = (Button) findViewById(R.id.btnGiveUp);
		rb0 = (RadioButton) findViewById(R.id.radio0);
		rb1 = (RadioButton) findViewById(R.id.radio1);
		
		
		answerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(rb0.isChecked() || rb1.isChecked()){
					Toast.makeText(ChallangeOneActivity.this, "Your answer is not right", Toast.LENGTH_SHORT).show();
				} else {
					completeChallenge();
					showAlertDialogSolved();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challange_one, menu);
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
