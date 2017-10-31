package com.androidcodehub.googleplacesapi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.androidcodehub.googleplacesdetail.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends FragmentActivity {

   private static final String GOOGLE_API_KEY = "AIzaSyCqoOPjf0vze3dN_2xUwEk7xV5jltrQQJ4";
    GoogleMap googleMap;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_main);

        placeText = (EditText) findViewById(R.id.place);
        
        Button btnFind = (Button) findViewById(R.id.find);
        
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
    
       fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {

             googleMap=googleMap1;
                googleMap1.setMyLocationEnabled(true);
           //     checkLocationPermission();
locationAndContactsTask();


              }
        });


   //

        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
   	
            	
            	String type = placeText.getText().toString();
                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=2000");
                googlePlacesUrl.append("&type=restaurant");

                googlePlacesUrl.append("&keyword=" + type);
           
                googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

                GooglePlacesTask googlePlacesTask = new GooglePlacesTask();
                Object[] toPass = new Object[2];
                toPass[0] = googleMap;
                toPass[1] = googlePlacesUrl.toString();
                
                
                System.out.println("llllllllllll"+ googlePlacesUrl.toString());
                
                
                
                googlePlacesTask.execute(toPass);
            }
        });
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    
    final LocationListener locationListener = new LocationListener() {
	     
		 public void onLocationChanged(Location location) {
	        
	        	longitude = location.getLongitude();
	            latitude = location.getLatitude();
	 
	        
	        }

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	   
	 };



    public void locationAndContactsTask() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
            //         Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


            longitude = myLocation.getLongitude();

            latitude = myLocation.getLatitude();

            System.out.println("lllll" + longitude);

            System.out.println("lllll" + latitude);

        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(latitude,
                        longitude))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);



    } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this,"Location",
                   MY_PERMISSIONS_REQUEST_LOCATION, perms);
        }
    }


}