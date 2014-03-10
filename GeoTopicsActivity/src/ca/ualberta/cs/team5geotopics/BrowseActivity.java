package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.team5geotopics.R;

public abstract class BrowseActivity extends Activity {
	protected BrowseView myView;
	public static CommentListModel clm;
	protected ListView browseListView;
	protected CommentModel viewingComment;
	protected GeoTopicsApplication application;
	protected Cache mCache;
	protected User myUser;
	protected Intent intent;
	protected CommentListController modelController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get the singletons we may need.
		this.application = GeoTopicsApplication.getInstance();
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();
		this.mCache = Cache.getInstance();
		
		//Construct the model
		this.clm = new CommentListModel();		
	}

	// Creates the options menu using the layout in menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browse_view, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// User clicks new comment button.
		case R.id.new_top_level_comment:
			intent = new Intent(this, CreateCommentActivity.class);
			startActivity(intent);
			break;
		case R.id.action_sort:
			showDialog(0);
			break;
		case R.id.action_my_comments:
			intent = new Intent(this, MyCommentsActivity.class);
			startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	// This creates the dialog for taking sorting
	@Override
	protected Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// User has selected sorting options
		if (i == 0) {
			String options[] = new String[6];

			options[0] = "Sort by proximity to me";
			options[1] = "Sort by proximity to location";
			options[2] = "Sort by freshness";
			options[3] = "Sort by proximity to picture";
			options[4] = "Sort by scoring system";
			options[5] = "Sort by time";

			builder.setTitle("Select Option").setItems(options,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which option to sort by?
							Location myLoc = new Location("myLoc");
							switch (which) {
							case 0: // Proximity to me
								/*
								 * Here is where we would have to get the
								 * current location Defaulting to lat= 0, long =
								 * 0
								 */
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByProximityToLoc(myLoc);
								break;
							case 1: // Proximity to location
								/*
								 * Here is where we would have to get the users
								 * desired location Defaulting to lat= 0, long =
								 * 0
								 */
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByProximityToLoc(myLoc);
								break;
							case 2: // Sort By freshness
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByFreshness(myLoc);
								break;
							case 3: // Proximity to picture
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByPicture(myLoc);
								break;
							case 4: // Scoring system
								/*
								 * we are just default sorting by date right now
								 */
								clm.sortAllCommentsByDate();
								break;
							case 5: // Time
								/*
								 * not sure if this is supposed to be part of
								 * the scoring system
								 */
								clm.sortAllCommentsByDate();
								break;
							}
						}
					});
			return builder.create();
		}
		return null;
	}

	/*
	 * Credit to Alexandre Jasmin for this code. Link:
	 * http://stackoverflow.com/questions
	 * /4238921/android-detect-whether-there-is-an-internet-connection-available
	 */
	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
