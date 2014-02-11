package com.example.team5geotopics;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GeoTopicsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_topics);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geo_topics, menu);
		return true;
	}

}
