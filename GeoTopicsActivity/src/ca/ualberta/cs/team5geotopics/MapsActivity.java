package ca.ualberta.cs.team5geotopics;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.example.team5geotopics.R;
import com.example.team5geotopics.R.id;
import com.example.team5geotopics.R.layout;
import com.example.team5geotopics.R.menu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This file is activated once the user selects that he/she wishes to manually set a location.
 * Upon entering this file, a map is displayed and a user can select a location on the map.
 * Submitting the location changes the mGeolocation's longitude and latitude and tags it into the comment.
 *
 */
public class MapsActivity extends InspectCommentActivity {
	 
    // Google Map
    private GoogleMap googleMap;
    private LatLng geoPoint;
    private Marker marker;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            
            googleMap.setOnMapClickListener(new OnMapClickListener(){
            
            @Override
            public void onMapClick(LatLng point) {
            	if(marker != null){
	    			marker.remove();
	    		}
                geoPoint = point;
                DecimalFormat form = new DecimalFormat("0.00000");
                String lat = form.format(point.latitude);
                String longit = form.format(point.longitude);
                Toast.makeText(getBaseContext(), 
                		form.format(point.latitude)  + "," + 
                		form.format(point.longitude)  , 
                        Toast.LENGTH_SHORT).show();
                marker = googleMap.addMarker(new MarkerOptions().position(point).title("Latitude:" + lat  + " Longitude:" + longit));
            	}
            });
             
            
        }
        
        Button button = new Button(this);
        button.setText("Set Location");
        addContentView(button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        button.setOnClickListener(new View.OnClickListener() {

        @Override
        	 public void onClick(View v) {
        	    	if(geoPoint != null){
        	    	DecimalFormat form = new DecimalFormat("0.0000");
        	    	form.format(geoPoint.latitude);
        	    	form.format(geoPoint.longitude);
        	    	Location loc = new Location("loc");
        	    	loc.setLatitude(geoPoint.latitude);
        	    	loc.setLongitude(geoPoint.longitude);
        	    	mGeolocation = loc;
        	    	finish();
        	    	}
        	finish();
        	}
       });
	        
    }
 
    
    
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

        
}


