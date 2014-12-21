package com.example.cityrally.activities;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MisteryActivity extends ActionBarActivity {

	private TextView misteryText;
	private Button misteryAnswer;
	
	private EditText misteryEditText;
	
	private String answer;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mistery);
		
		misteryText = (TextView) findViewById(R.id.misteryText);
		misteryText.setText("It crosses a river. Tourists like it, but some are afraid of it.");
		
		misteryEditText = (EditText) findViewById(R.id.editText1);
		
		misteryAnswer = (Button) findViewById(R.id.misteryAnswer);
		
		misteryAnswer.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				answer = misteryEditText.getText().toString();
				if ( answer.toLowerCase().equals("adolphe bridge") ) {
					
					Toast.makeText(MisteryActivity.this, "Congratulations! You solved the mystery.", Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK, intent);
			        finish();
				}
				else 
				{
					Toast.makeText(MisteryActivity.this, "Your answer is not right. Please try again!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
 		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mistery, menu);
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
