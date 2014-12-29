package com.example.cityrally.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;

import com.example.cityrally.R;
import com.example.cityrally.model.CheckPoint;
import com.example.cityrally.model.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity implements LocationListener {

	final int MARKER_UPDATE_INTERVAL = 2000;
	Handler handler = new Handler();
	
	private SupportMapFragment mapFragment;
	private GoogleMap map;

	///for current location
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	
	String provider;
	protected String latitude,longitude; 
	protected boolean gps_enabled,network_enabled;
	
	private Location lastKnownLocation;
	
	//private LocationManager lm;
	
	// will add all Markers in the HashMap 
	private Map<Marker, Class<?>> allMarkersForClass = new HashMap<Marker, Class<?>>();
	//will add all checkPoints in the HashMap
	private HashMap<Marker, CheckPoint> allMarkersForCheckPoint = new HashMap<Marker, CheckPoint>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    map = mapFragment.getMap();
	    if (map == null) {
	      finish();
	      return;
	    }
	   
	    final DatabaseHandler db = new DatabaseHandler(this);
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			// on click on the marker it will do following actions:
			@Override
			public boolean onMarkerClick (Marker myMarker) {
				
				Class<?> classForMarker = allMarkersForClass.get(myMarker);
		
				final CheckPoint checkPoint = allMarkersForCheckPoint.get(myMarker);
				
				// if the checkPoint (Challenge) is solved
				if( checkPoint.isSolved()) {
					Toast.makeText(MapActivity.this, "The challenge is already solved!", Toast.LENGTH_SHORT).show();
					
				} else {
					// checkPoint (next Challenge) is unlocked if and only if the previous challenge is already solved/given up
					if ( db.isCheckPointUnLocked(checkPoint) ) {
						
						// if we un-comment the code bellow, we can check whether the Marker is: close up to 100m to the Challenge activity,
						// which wants to solve the player, otherwise it will offer to show the direction
						
						// if(isCheckPointClose(checkPoint)){
						
							Intent intent = new Intent(MapActivity.this, classForMarker);			
							intent.putExtra("checkPointID", checkPoint.getId());
							startActivity(intent);	
							
						//} else {
						//	showAlertMsgForDirection(checkPoint);
						//}
							
					} else {
						Toast.makeText(MapActivity.this, "This challenge is locked!", Toast.LENGTH_SHORT).show();
					}
				}
				
				return false;
			}
		});
	
		//current location
		map.setMyLocationEnabled(true);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    map = mapFragment.getMap();
	    if (map == null) {
	      finish();
	      return;
	    }
	    
	    map.clear();
	    this.allMarkersForCheckPoint.clear();
	    this.allMarkersForClass.clear();
	    
	    addMarkerToMap();
		
		super.onResume();
	}
	
	// adding Markers(checkPoints) on the map
	private void addMarkerToMap() {
		
		DatabaseHandler db = new DatabaseHandler(this);
		List<CheckPoint> checkPoint = db.getAllCheckPoint();  
		
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    map = mapFragment.getMap();
	    if (map == null) {
	      finish();
	      return;
	    }
	    LatLng previousPoint = null;
		for (final CheckPoint cn : checkPoint) {
			
			double lat = cn.getLatitude();
			double log = cn.getLongitude();
			
			LatLng Markers = new LatLng(lat, log);
			
			if ( previousPoint != null) {
				if ( db.isCheckPointUnLocked(cn) ) {
					// setting the color for solved checkpoint 
					map.addPolyline(new PolylineOptions().add(previousPoint, Markers ).width(5).color(Color.GREEN));
				}
				else {
					// setting the color for unsolved checkpoint 
					map.addPolyline(new PolylineOptions().add(previousPoint, Markers ).width(5).color(Color.RED));
				}
			} 
			previousPoint = Markers;
		    
		    BitmapDescriptor bdf = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
		    if ( cn.isSolved() ) {
		    	bdf = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		    }
		    
		    MarkerOptions mo = new MarkerOptions()
		    		.position(Markers)
		    		.icon(bdf);
		    		//.title("Markers")
		  
		    Marker myMarker = map.addMarker(mo);
		    
		    if ( cn.isSolved() ) {
		    	
		    	this.allMarkersForCheckPoint.put(myMarker, cn);
			} else {
				
					// if CID = c1, open ChallangeOneActivity activity
			    if ( cn.getChallangeID().equals("c1")) {
			    	this.allMarkersForClass.put(myMarker, ChallangeOneActivity.class);
			    	this.allMarkersForCheckPoint.put(myMarker, cn);
			    
			    	// if CID = c2, open ChallengeTwoActivity activity
			    } else if ( cn.getChallangeID().equals("c2") ) {
			    	this.allMarkersForClass.put(myMarker, ChallengeTwoActivity.class);
			    	this.allMarkersForCheckPoint.put(myMarker, cn);
			    	
			    	// if CID = c3, open ChallengeThreeActivity activity
			    } else if ( cn.getChallangeID().equals("c3") ) {
					this.allMarkersForClass.put(myMarker, ChallengeThreeActivity.class);
					this.allMarkersForCheckPoint.put(myMarker, cn);
					
					// if CID = c4, open ChallengeFourActivity activity
			    } else if ( cn.getChallangeID().equals("c4") ) {
					this.allMarkersForClass.put(myMarker, ChallengeFourActivity.class);
					this.allMarkersForCheckPoint.put(myMarker, cn);
					
				}
		    
			}
		    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Markers, 10));
	
		    // Zoom in, animating the camera.
		    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		     
		}
	}
		public void onLocationChanged(Location location) {
			
			this.lastKnownLocation = location;  
			
		}
	
		public void onProviderDisabled(String provider) {
			Log.d("Latitude","disable");
		}
	
		public void onProviderEnabled(String provider) {
			Log.d("Latitude","enable");
		}
	
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("Latitude","status");
		}
		
		// checks whether the distance between the player and the challenge that he is going to solve is less than 100 meters? 
		public boolean isCheckPointClose(CheckPoint checkPoint) {
			boolean isClose = false;
			
			Location checkPointLocation = new Location("cpLocation");
			checkPointLocation.setLatitude(checkPoint.getLatitude());
			checkPointLocation.setLongitude(checkPoint.getLongitude());
			
			if ( this.lastKnownLocation != null ) {
				double distance = this.lastKnownLocation.distanceTo(checkPointLocation);
				
				isClose = distance <= 100; 
			}
			
			return isClose;
		}
		
		// suggest to give a direction from user's current location to the challenge which he is going to solve.
		public void showAlertMsgForDirection(final CheckPoint checkPoint){
			
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Would you like to get the directions to the next Challange?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Uri gmmIntentUri = Uri.parse("google.navigation:q="+checkPoint.getLatitude()+","+checkPoint.getLongitude());
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
						mapIntent.setPackage("com.google.android.apps.maps");
						startActivity(mapIntent);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		}
}