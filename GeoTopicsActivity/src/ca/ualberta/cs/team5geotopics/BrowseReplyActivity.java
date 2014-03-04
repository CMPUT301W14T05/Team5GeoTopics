package ca.ualberta.cs.team5geotopics;

import com.example.team5geotopics.R;
import com.example.team5geotopics.R.layout;
import com.example.team5geotopics.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BrowseReplyActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getBundleExtra("TLComment");
		b.getSerializable("TLComment");
		
		setContentView(R.layout.activity_browse_relpy);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_relpy, menu);
		return true;
	}

}
