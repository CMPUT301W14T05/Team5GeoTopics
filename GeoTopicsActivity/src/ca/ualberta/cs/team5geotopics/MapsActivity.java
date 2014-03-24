package ca.ualberta.cs.team5geotopics;

import com.example.team5geotopics.R;
import com.example.team5geotopics.R.id;
import com.example.team5geotopics.R.layout;
import com.example.team5geotopics.R.menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;


public class MapsActivity extends Activity {
   static final LatLng TutorialsPoint = new LatLng(21 , 57);
   private GoogleMap googleMap;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_maps);
      try { 
            if (googleMap == null) {
               googleMap = ((MapFragment) getFragmentManager().
               findFragmentById(R.id.map)).getMap();
            }
         googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
         Marker TP = googleMap.addMarker(new MarkerOptions().
         position(TutorialsPoint).title("TutorialsPoint"));

      } catch (Exception e) {
         e.printStackTrace();
      }

   }

}