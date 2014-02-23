package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.team5geotopics.R;


public class StartActivity extends Activity {
	private Button mBrowseTopLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_topics);
		
		setUpBroweButton();
	}
	
	private void setUpBroweButton() {
		mBrowseTopLevel = (Button)findViewById(R.id.start_browse_top_level);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, BrowseTopLevelView.class);
				StartActivity.this.startActivity(myIntent);
				
			}
		});
	}

	

}
