package ca.ualberta.cs.team5geotopics;

import com.example.team5geotopics.R;
import com.example.team5geotopics.R.id;
import com.example.team5geotopics.R.layout;
import com.example.team5geotopics.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.os.Build;

public class ManualLocationActivity extends Activity {
Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_location);
	
		submit = (Button) findViewById(R.id.manualLocation);
	
		OnClickListener onClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent locIntent = new Intent();
				EditText longitude = (EditText)findViewById(R.id.manualLongitude);
				EditText latitude = (EditText)findViewById(R.id.manualLatitude);
				Location loc = new Location("loc");
	    		loc.setLatitude(Double.parseDouble(latitude.getText().toString()));
	    		loc.setLongitude(Double.parseDouble(longitude.getText().toString()));
	    		locIntent.putExtra("location_return", loc);
	    		setResult(RESULT_OK, locIntent);
	    		finish();
			}
		};
	
		submit.setOnClickListener(onClick);
		
	}

}