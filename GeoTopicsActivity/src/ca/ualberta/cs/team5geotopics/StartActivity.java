package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.team5geotopics.R;

/**
 * Contains data for the start screen of the application.
 * Upon launching, the user is given the option to view top-level comments, view
 * his/her comments, browse his/her favorites, and to browse his/her bookmarks.
 */

public class StartActivity extends Activity {
	//private Button mBrowseTopLevel;
	private ImageButton mBrowseTopLevel;
	private ImageButton mBrowseMyComments;
	private ImageButton mBrowseFavourites;
	private ImageButton mBrowseBookmarks;
	private GeoTopicsApplication application;
	private User user;
	private Cache mCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_topics);
		//Get the application
		application = GeoTopicsApplication.getInstance();
		//Set the application context such that we can get
		//it from anywhere
		application.setContext(getApplicationContext());
		//Get the user
		user = User.getInstance();
		mCache = Cache.getInstance();
		
		if(!user.installFilesExist()){
			user.writeInstallFiles();
		}
		setUpBrowseButton();
		setUpFavouritesButton();
		setUpMyCommentsButton();
		setUpBrowseBookmarks();
	}

	
	private void setUpBrowseButton() {
		mBrowseTopLevel = (ImageButton)findViewById(R.id.start_browse_top_level);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, TopLevelActivity.class);
				startActivity(myIntent);
			}
		});
	}
	
	private void setUpMyCommentsButton() {
		mBrowseTopLevel = (ImageButton)findViewById(R.id.start_my_comments);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, MyCommentsActivity.class);
				startActivity(myIntent);
				
			}
		});
	}
	
	private void setUpFavouritesButton() {
		mBrowseTopLevel = (ImageButton)findViewById(R.id.start_favourite);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, MyFavouritesActivity.class);
				startActivity(myIntent);
			}
		});
	}
	
	private void setUpBrowseBookmarks() {
		mBrowseTopLevel = (ImageButton)findViewById(R.id.start_bookmark);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, MyBookmarksActivity.class);
				startActivity(myIntent);
				
			}
		});
	}
	
}
