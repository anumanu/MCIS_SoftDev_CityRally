package com.example.cityrally.activities;

import java.util.ArrayList;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class ChallengeThreeActivity extends ChallengeActivity implements OnGesturePerformedListener  {


	private int mActivePointerId;
	private GestureLibrary gLibrary;
	
	private Button giveUpBtn;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_three);
		
	
		giveUpBtn = (Button) findViewById(R.id.btnGiveUp);
		
		gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLibrary.load()) {
            finish();
            
        }

        GestureOverlayView gOverlay = (GestureOverlayView) findViewById(R.id.gOverlay);
        gOverlay.addOnGesturePerformedListener(this);
        
        
        giveUpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				giveUpTheChallenge();
				showAlertDialogGaveUp();
			}
		});
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture
            gesture) {
        ArrayList<Prediction> predictions =
                gLibrary.recognize(gesture);

     //   if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
        if (predictions.get(0).name.equals("HOUSE")) {
            //String action = predictions.get(0).name;
           
            completeChallenge();
			showAlertDialogSolved();
        }
        else {
        	Toast.makeText(ChallengeThreeActivity.this, "Your answer is wrong.", Toast.LENGTH_SHORT).show();
        }
        
       
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
