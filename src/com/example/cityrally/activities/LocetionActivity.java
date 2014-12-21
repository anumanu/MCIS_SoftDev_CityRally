package com.example.cityrally.activities;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

public class LocetionActivity extends ActionBarActivity  implements LocationListener  {

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	TextView txtLat;
	String lat;
	String provider;
	protected String latitude,longitude; 
	protected boolean gps_enabled,network_enabled;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		
		txtLat = (TextView) findViewById(R.id.testview);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
	

		@Override
		public void onLocationChanged(Location location) {
		txtLat = (TextView) findViewById(R.id.testview);
		txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
		
		Location location1 = new Location(location);
        //Location near_locations;
		location1.setLatitude(17.372102);
        location1.setLongitude(78.484196);
        Location location2 = new Location("locationA");
        location2.setLatitude(17.375775);
        location2.setLongitude(78.469218);
        double distance=location2.distanceTo(location1);
        System.out.println("Distance --------------"+ distance);
		}

		@Override
		public void onProviderDisabled(String provider) {
		Log.d("Latitude","disable");
		}

		@Override
		public void onProviderEnabled(String provider) {
		Log.d("Latitude","enable");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude","status");
		}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
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
