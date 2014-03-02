package ca.ualberta.cs.team5geotopics;

import com.example.team5geotopics.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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
		//Construct the View
		this.myView = new BrowseView(this, R.layout.top_level_list_item, clm);
		
		this.clm.add(new CommentModel("This is a body", "James", "This is a Title"));
	}
	
	//Creates the options menu using the layout in menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		   // Inflate the menu items for use in the action bar
		   MenuInflater inflater = getMenuInflater();
		   inflater.inflate(R.menu.browse_view, menu);
		   return super.onCreateOptionsMenu(menu);
	}

}
