package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.example.team5geotopics.R;

public class BrowseActivity extends Activity{
	
	BrowseView myView;
	CommentListModel clm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Construct the model
		this.clm = new CommentListModel();
		
		//REMOVE THIS AFTER TESTING
		//Fill the model with test data
		this.clm.add(new CommentModel("This is a body", "James", "This is a Title"));
		this.clm.add(new CommentModel("This is a my body", "Tyler", "This is a my Title"));
		//**********************************************************************************
		
		//Construct the View
		this.clm.addView(this.myView);
		this.myView = new BrowseView(this, R.layout.top_level_list_item, clm.getList());
		
		//Attach the list view to myView
		ListView browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
	}
	
	

}
