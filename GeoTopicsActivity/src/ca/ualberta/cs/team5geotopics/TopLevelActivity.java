package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.team5geotopics.R;

public class TopLevelActivity extends BrowseActivity {
	private CommentListController modelController;
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
		application = GeoTopicsApplication.getInstance();
		
		//Construct the model
		this.clm = new CommentListModel();
		this.mCache = Cache.getInstance();
		
		
		
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item, clm.getList());
		//Register the view with the model
		this.clm.addView(this.myView);
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
		
		modelController = new CommentListController(this.clm);
		modelController.getTopLevel(this);
		
		//Get from Internet if available else get from cache
		//this.clm.setList(mCache.getHistory());
		
	}
	
	@Override
	protected void onResume(){
		//Reset the current viewing comment
		application.setCurrentViewingComment(viewingComment);
		myView.notifyDataSetChanged(); //Ensure the view is up to date.

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

	
	@Override
	protected void onPause() {
		
		super.onPause();
	}
}
