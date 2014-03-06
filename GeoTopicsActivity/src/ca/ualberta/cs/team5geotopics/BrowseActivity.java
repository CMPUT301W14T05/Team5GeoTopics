package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

public abstract class BrowseActivity extends Activity {
	protected BrowseView myView;
	public static CommentListModel clm;
	protected ListView browseListView;
	protected CommentModel viewingComment;
	protected GeoTopicsApplication application;

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
			Intent intent = new Intent(this, CreateCommentActivity.class);
			startActivity(intent);
			break;
		case R.id.action_sort:
			showDialog(0);
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
			String options[] = new String[5];

			options[0] = "Sort by proximity to me";
			options[1] = "Sort by proximity to location";
			options[2] = "Sort by proximity to picture";
			options[3] = "Sort by scoring system";
			options[4] = "Sort by time";

			builder.setTitle("Select Option").setItems(options,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which option to sort by?
							if (which == 0) { // Proximity to me
								/*
								 * Here is where we would have to get the current location
								 * Defaulting to lat= 0, long = 0
								 */
								Location myLoc = new Location("myLoc");
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByProximityToLoc(myLoc);
							}
							if (which == 1) { // Proximity to location
								/*
								 * Here is where we would have to get the users desired location
								 * Defaulting to lat= 0, long = 0
								 */
								Location myLoc = new Location("myLoc");
								myLoc.setLatitude(0);
								myLoc.setLongitude(0);
								clm.sortCommentsByProximityToLoc(myLoc);
							}
							if (which == 2) { // Proximity to picture
								clm.sortCommentsByPicture();
							}
							if (which == 3) { // Scoring system
								/*
								 * we are just default sorting by date right now
								 */
								clm.sortAllCommentsByDate();
							}
							if (which == 4) { // Time
								/*
								 * not sure if this is supposed to be part of the scoring system
								 */
								clm.sortAllCommentsByDate();
							}
						}
					});
			return builder.create();
		}
			return null;
		}
}
