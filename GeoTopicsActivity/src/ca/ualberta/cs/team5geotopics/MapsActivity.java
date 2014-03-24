package ca.ualberta.cs.team5geotopics;

import java.text.DecimalFormat;

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
import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


public class MapsActivity extends Activity {
	 
    // Google Map
    private GoogleMap googleMap;
 
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
                Log.d("Map","Map clicked");
                DecimalFormat form = new DecimalFormat("0.00000");
                Toast.makeText(getBaseContext(), 
                		form.format(point.latitude)  + "," + 
                		form.format(point.longitude)  , 
                        Toast.LENGTH_SHORT).show();
                googleMap.addMarker(new MarkerOptions().position(point).title("Clicked!"));
            	}
            });
             
            
        }
	        
    }
 
    
    
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
 
    
}


