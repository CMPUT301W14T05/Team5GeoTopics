package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
		this.clm.add(new CommentModel("This is a body", "James", "This is a much longer Title that will cut off "));
		this.clm.add(new CommentModel("This is a my body", "Tyler", "This is a my Title"));
		//***********************************************************************************************************
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.top_level_list_item, clm.getList());
		//Register the view with the model
		this.clm.addView(this.myView);
		
		//Attach the list view to myView
		ListView browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
	}
	
	//Creates the options menu using the layout in menu.
		public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate the menu items for use in the action bar
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.browse_view, menu);    
		    return super.onCreateOptionsMenu(menu);
		}
		
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case R.id.new_top_level_comment:
				Intent intent = new Intent(this, InspectCommentActivity.class);
				startActivity(intent);
				break;
				
			default:
				return super.onOptionsItemSelected(item);
			}
		return true;
		}
}
