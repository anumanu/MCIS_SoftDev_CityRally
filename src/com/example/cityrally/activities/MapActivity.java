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
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;

import com.example.cityrally.R;
import com.example.cityrally.R.id;
import com.example.cityrally.R.layout;
import com.example.cityrally.R.menu;
import com.example.cityrally.model.CheckPoint;
import com.example.cityrally.model.DatabaseHandler;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
	////end
	public LocationClient mLocationClient;
	
	//Mock location
	private LocationManager lm;
	
	private Map<Marker, Class<?>> allMarkersForClass = new HashMap<Marker, Class<?>>();
	private HashMap<Marker, CheckPoint> allMarkersForCheckPoint = new HashMap<Marker, CheckPoint>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
		
		//---
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);        

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {       
		            @Override
		            public void onStatusChanged(String provider, int status, Bundle extras) {}
		            @Override
		            public void onProviderEnabled(String provider) {}
		            @Override
		            public void onProviderDisabled(String provider) {}
		            @Override
		            public void onLocationChanged(Location location) {}
		});
		            
		/* Set a mock location for debugging purposes */
		setMockLocation(6.130872, 49.610465, 500);
		//---
	    
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    map = mapFragment.getMap();
	    if (map == null) {
	      finish();
	      return;
	    }
	   
	    final DatabaseHandler db = new DatabaseHandler(this);
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick (Marker myMarker) {
				
				Class<?> classForMarker = allMarkersForClass.get(myMarker);
		
				final CheckPoint checkPoint = allMarkersForCheckPoint.get(myMarker);
				
				if( checkPoint.isSolved()) {
					Toast.makeText(MapActivity.this, "The challenge is already solved!", Toast.LENGTH_SHORT).show();
					
				} else {
					
					if ( db.isCheckPointUnLocked(checkPoint) ) {
						if(isCheckPointClose(checkPoint)){
							Intent intent = new Intent(MapActivity.this, classForMarker);			
							intent.putExtra("checkPointID", checkPoint.getId());
							startActivity(intent);	
						} else {
							showAlertMsgForDirection(checkPoint);
						}
					} else {
						Toast.makeText(MapActivity.this, "This challenge is locked!", Toast.LENGTH_SHORT).show();
					}
				}
				
				return false;
			}
		});
	
		
		/*
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
		
			@Override
			public void onInfoWindowClick(Marker myMarker) {
				Class<?> classForMarker = allMarkersForClass.get(myMarker); 
				
				//CheckPoint checkPoint = allMarkersForCheckPoint.get(myMarker);
				//if( ! checkPoint.isSolved() ) {
					Intent intent = new Intent(MapActivity.this, classForMarker);			
					//intent.putExtra("checkPointID", checkPoint.getId());
					startActivity(intent);
			//	} else {
					// Show Alert message
				//}
			}
		});
		*/
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
		System.out.println("MAP ON RESUME --------");
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
					map.addPolyline(new PolylineOptions().add(previousPoint, Markers ).width(5).color(Color.GREEN));
				}
				else {
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
		    	System.out.println("first isSolved value is: " + cn.isSolved());
		    	
		    	this.allMarkersForCheckPoint.put(myMarker, cn);
			} else {
		    
			    if ( cn.getChallangeID().equals("c1")) {
					
			    	this.allMarkersForClass.put(myMarker, ChallangeOneActivity.class);
			    	this.allMarkersForCheckPoint.put(myMarker, cn);
			    	
			    } else if ( cn.getChallangeID().equals("c2") ) {
			    	this.allMarkersForClass.put(myMarker, ChallengeTwoActivity.class);
			    	this.allMarkersForCheckPoint.put(myMarker, cn);
			    	
			    } else if ( cn.getChallangeID().equals("c3") ) {
					this.allMarkersForClass.put(myMarker, ChallengeThreeActivity.class);
					this.allMarkersForCheckPoint.put(myMarker, cn);
					
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
		
		public boolean isCheckPointClose(CheckPoint checkPoint) {
			boolean isClose = false;
			
			Location checkPointLocation = new Location("cpLocation");
			checkPointLocation.setLatitude(checkPoint.getLatitude());
			checkPointLocation.setLongitude(checkPoint.getLongitude());
			
			if ( this.lastKnownLocation != null ) {
				double distance = this.lastKnownLocation.distanceTo(checkPointLocation);
				System.out.println("DISTANCE is ---------------" + distance);
				isClose = distance <= 100; 
			}
			
			return isClose;
		}
		
		public void showAlertMsgForDirection(final CheckPoint checkPoint){
			
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			
 
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
		private void setMockLocation(double latitude, double longitude, float accuracy) {
		    lm.addTestProvider (LocationManager.GPS_PROVIDER,
		                        "requiresNetwork" == "",
		                        "requiresSatellite" == "",
		                        "requiresCell" == "",
		                        "hasMonetaryCost" == "",
		                        "supportsAltitude" == "",
		                        "supportsSpeed" == "",
		                        "supportsBearing" == "",
		                         android.location.Criteria.POWER_LOW,
		                         android.location.Criteria.ACCURACY_FINE);      

		    Location newLocation = new Location(LocationManager.GPS_PROVIDER);

		    newLocation.setLatitude(latitude);
		    newLocation.setLongitude(longitude);
		    newLocation.setAccuracy(accuracy);

		    lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

		    lm.setTestProviderStatus(LocationManager.GPS_PROVIDER,
		                             LocationProvider.AVAILABLE,
		                             null,System.currentTimeMillis());    
		  
		    lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);      

		}
		
}


