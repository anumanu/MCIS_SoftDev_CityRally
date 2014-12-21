package com.example.cityrally.activities;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeTwoActivity extends ChallengeActivity {

	private EditText chTwoEditText;
	private Button chTwoAnswer;
	private Button giveUpBtn;
	
	private String answer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_two);
		
		//chTwoTextView = (TextView) findViewById(R.id.c)
		chTwoEditText = (EditText) findViewById(R.id.challanageTwoEditText);
		chTwoAnswer = (Button) findViewById(R.id.challanageTwoAnswer);
		giveUpBtn = (Button) findViewById(R.id.btnGiveUp);
		
		chTwoAnswer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				answer = chTwoEditText.getText().toString();
				if ( answer.toLowerCase().equals("apple") ) {
					completeChallenge();
					showAlertDialogSolved();
				} else {
					Toast.makeText(ChallengeTwoActivity.this, "Your answer is not right", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		giveUpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("GIVE UP! ");
				giveUpTheChallenge();
				showAlertDialogGaveUp();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge_two, menu);
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
