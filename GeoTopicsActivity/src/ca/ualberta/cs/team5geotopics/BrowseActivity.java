package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.team5geotopics.R;

/**
 * BrowseActivity is responsible for mainly handling the load of the comments.
 * It is what is called when "Browse" is called from the main program screen.
 */

public abstract class BrowseActivity extends Activity {
	protected BrowseView myView;
	public CommentListModel clm;
	protected ListView browseListView;
	protected CommentModel viewingComment = null;
	protected GeoTopicsApplication application;
	protected User myUser;
	protected Intent intent;
	protected CommentSearch modelController;
	protected CommentManager manager;
	protected Bundle b;
	protected boolean bookmark = false;
	protected boolean favourite = false;
	protected UserController uController;
	protected BroadcastReceiver webConnectionReceiver;
	protected BrowseActivity me;

	/**
	 * New comment request code.
	 */
	public static final int NEW_COMMENT = 1;
	public static final int SELECT_LOCATION_REQUEST_CODE = 200;

	public abstract String getType();

	/**
	 * The necessary code for the creation of this activity.
	 * 
	 * @param Bundle
	 *            Any necessary state for the creation of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove the top back button, not going to use it.
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);

	}
	// Creates the options menu using the layout in menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browse_view, menu);

		return super.onCreateOptionsMenu(menu);
	}

	//Ensures the favourites icon is the right color
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;
		item = menu.findItem(R.id.action_favourite);
		if (viewingComment != null) {
			if (myUser.inFavourites(viewingComment)) {
				favourite = true;
				item.setIcon(R.drawable.ic_action_favorite_b);
			} else {
				item.setIcon(R.drawable.ic_action_favorite);
				favourite = false;
			}
		}
		return true;
	}

	/**
	 * The necessary code for what to do on a menu item select
	 * 
	 * @param item
	 *            The menu item that was selected
	 * @return If the selection was sucessfull.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// User clicks new comment button.
		case R.id.new_top_level_comment:
			intent = new Intent(this, CreateCommentActivity.class);
			if (this instanceof TopLevelActivity) {
				intent.putExtra("CommentType", "TopLevel");
			} else {
				intent.putExtra("CommentType", "ReplyLevel");
				intent.putExtra("ParentID", viewingComment.getmEsID());
			}
			startActivityForResult(intent, NEW_COMMENT);
			break;
		case R.id.action_sort:
			showDialog(0);
			break;
		case R.id.action_my_comments:
			intent = new Intent(this, MyCommentsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_profile:
			intent = new Intent(this, EditMyProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.action_my_bookmarks:
			intent = new Intent(this, MyBookmarksActivity.class);
			startActivity(intent);
			break;
		case R.id.action_my_favourites:
			intent = new Intent(this, MyFavouritesActivity.class);
			startActivity(intent);
			break;
		case R.id.action_refresh:
			manager.refresh(this.clm, this, viewingComment);
			break;
		case R.id.action_bookmark:
			if (bookmark) {
				// set to white
				item.setIcon(R.drawable.ic_notification_bookmark);
				bookmark = false;
			} else {
				// set to blue
				item.setIcon(R.drawable.ic_notification_bookmark_b);
				bookmark = true;
			}
			break;
		case R.id.action_favourite:
			if (favourite) {
				// set to white
				item.setIcon(R.drawable.ic_action_favorite);
				favourite = false;
				uController.favourite(this.viewingComment);
			} else {
				// set to blue
				item.setIcon(R.drawable.ic_action_favorite_b);
				favourite = true;
				uController.favourite(this.viewingComment);
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/**
	 * The code for creating a custom dialogue box for sorting.
	 * 
	 * @return The dialog created
	 */
	@Override
	protected Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// User has selected sorting options
		if (i == 0) {
			String options[] = new String[5];

			options[0] = "Sort by proximity to me";
			options[1] = "Sort by proximity to location";
			options[2] = "Sort by freshness";
			options[3] = "Sort by proximity to picture";
			options[4] = "Sort by time";

			builder.setTitle("Select Option").setItems(options,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which option to sort by?
							if (which == 1) {
								// SET LOCATION VIA GOOGLE MAP
								Intent myIntent = new Intent(BrowseActivity.this, MapsActivity.class);
								//startActivity(myIntent);
								startActivityForResult(myIntent, SELECT_LOCATION_REQUEST_CODE);
								
								if (clm.getCustomSortLoc() != null) {
									clm.setSortFlag(which);
									clm.sortOnUpdate();
								}
							} else {
								clm.setSortFlag(which);
								clm.sortOnUpdate();
							}
						}
					});
			return builder.create();
		}
		return null;
	}

	/**
	 * Used to capture returned results from other activities/intents.
	 * 
	 * @param requestCode
	 *            The request code for which this result is returning
	 * @param resultCode
	 *            The result code for the result
	 * @param data
	 *            The returned data from the intent
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NEW_COMMENT) {
			if (resultCode == RESULT_OK) {
				CommentModel newComment;
				String esID = data.getStringExtra("esID");
				String parentId = data.getStringExtra("parentId");
				newComment = manager.getComment(parentId, esID);
				
				Log.w("NewComment", Integer.toString(this.clm.getList().size()));
				this.clm.add(newComment);
				Log.w("NewComment", Integer.toString(this.clm.getList().size()));
				Log.w("NewComment", newComment.getmBody());
				this.myView.notifyDataSetChanged();
			}
		}
		
		/*
		 *  Retrieve the set location. If it wasn't set, keep the current location
		 */
		if (requestCode == SELECT_LOCATION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Location searchLoc = data.getParcelableExtra("location_return");
				clm.setCustomSortLoc(searchLoc);
				Log.d("SET_LOCATION_COORDS", "(" + Double.toString(searchLoc.getLatitude()) +
						", " + Double.toString(searchLoc.getLongitude()) + ")");
			}
		}
	}

}
