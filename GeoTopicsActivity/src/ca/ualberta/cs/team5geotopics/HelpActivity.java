package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.os.Bundle;

public class HelpActivity extends Activity {

	/** Called when the activity is first created.
	 * Displays Help screen. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(com.example.team5geotopics.R.layout.activity_help);
	    getActionBar().hide();
	}

}
