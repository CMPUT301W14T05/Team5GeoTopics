package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.team5geotopics.R;

/**
 * BrowseActivity is responsible for mainly handling the load of the comments. 
 * It is what is called when "Browse" is called from the main program screen.
 */

public abstract class BrowseActivity extends Activity {
	protected BrowseView myView;
	/**
	 * The comment list model that the activity uses to store and display its comments
	 */
	public CommentListModel clm;
	protected ListView browseListView;
	protected CommentModel viewingComment;
	protected GeoTopicsApplication application;
	protected Cache mCache;
	protected User myUser;
	protected Intent intent;
	protected CommentSearch modelController;
	
	/**
	 * New comment request code. 
	 */
	public static final int NEW_COMMENT = 1;

	public abstract String getType();
	
	/**
	 * The necessary code for the creation of this activity.
	 *
	 * @param  Bundle	Any necessary state for the creation of the activity
	 */
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
	}

	// Creates the options menu using the layout in menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browse_view, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * The necessary code for what to do on a menu item select
	 *
	 * @param  item  The menu item that was selected
	 * @return     If the selection was sucessfull.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// User clicks new comment button.
		case R.id.new_top_level_comment:
			intent = new Intent(this, CreateCommentActivity.class);
			if(this instanceof TopLevelActivity ){
				intent.putExtra("CommentType", "TopLevel");
			}else{
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
		case R.id.action_refresh:
			this.handleCommentLoad();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	

	/**
	 * The code for creating a custom dialogue box for sorting.
	 *
	 * @return     The dialog created
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
							Location myLoc = new Location("myLoc");
							clm.setSortFlag(which);
							clm.sortOnUpdate();
						}
					});
			return builder.create();
		}
		return null;
	}

	/**
	 * Returns an Image object that can then be painted on the screen. 
	 *
	 *@author Alexandre Jasmin
	 *Link: http://stackoverflow.com/questions/4238921/android-detect-whether-there-is-an-internet-connection-available
	 * @return  True is the network is available, false if not.
	 */
	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
	}
	
	/**
	 * Handles loading comments for the view into its comment list model. Attempts to load from the web else
	 * it loads from the cache.
	 *
	 */
	public void handleCommentLoad(){
		modelController = new CommentSearch(this.clm);
		if (isNetworkAvailable()) {
			
			if(this.getType().equals("TopLevel")){
				modelController.pullTopLevel(this);
			}
			else{
				modelController.pullReplies(this, viewingComment.getmEsID());
			}
			Log.w("Cache", "Have Internet");
		} else {
			Log.w("Cache", "No Internet");
			mCache.loadFileList(); //gets record of cache from previous session
			mCache.registerModel(clm);
			
			if (this.getType().equals("TopLevel")) {
				mCache.loadFromCache("history.sav", this);
			}
			else{
				String filename = viewingComment.getmEsID();
				if (mCache.repliesExist(filename)){
					mCache.loadFromCache(filename, this);
					Log.w("Cache", "load replies from cache");
				}
				else{
					Toast toast = Toast.makeText(this,"Unable to load from the cache, Please try again with internet",5);
					toast.show();
				}
			}
		}
	}
	
	/**
	 * Used to capture returned results from other activities/intents.
	 *
	 * @param  requestCode The request code for which this result is returning
	 * @param	resultCode The result code for the result
	 * @param	data The returned data from the intent
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NEW_COMMENT) {
			if(resultCode == RESULT_OK){
				CommentModel newComment;
				Bundle b = data.getExtras();
				newComment = b.getParcelable("NewComment");
				
				Log.w("NewComment", Integer.toString(this.clm.getList().size()));
				this.clm.add(newComment);
				Log.w("NewComment", Integer.toString(this.clm.getList().size()));
				Log.w("NewComment", newComment.getmBody());
				this.myView.notifyDataSetChanged();
			}
		}
	}
	
}


