package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class TopLevelActivity extends BrowseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get the application
		application = new GeoTopicsApplication();
		
		//Construct the model
		this.clm = new CommentListModel();
		
		//REMOVE THIS AFTER TESTING
		//Fill the model with test data
		this.clm.add(new CommentModel("This is a my body", "Tyler", "This is a my Title"));
		this.clm.add(new CommentModel("This is a Really Really Really Really Really Really Really Really(8)" +
				" Really Really(10) Really Really(12) Really Really(14) Really Really Really Really Really Really(20) Really" +
				" Really Really Really Really Really Really Really Really Really Really" +
				" Really Really Really Really Really Really Really Really Really(40) Really Really Really Really Really Really" +
				" Really Really Really Really Really Really Really Really Really Really Really Really Really Really Really" +
				" Really(62) long body", "Matt", "Title, this is."));
		//***********************************************************************************************************
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item, clm.getList());
		//Register the view with the model
		this.clm.addView(this.myView);
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
		
	}
	
	@Override
	protected void onResume(){
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				Intent intent = new Intent(TopLevelActivity.this, ReplyLevelActivity.class);
				application.setCurrentViewingComment((CommentModel)browseListView.getItemAtPosition(position));
				startActivity(intent);
			}
			
		});
		super.onResume();
	}	

}
