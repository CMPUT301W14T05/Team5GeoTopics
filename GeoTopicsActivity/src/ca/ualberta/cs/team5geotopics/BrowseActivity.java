package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
			Intent intent = new Intent(this, InspectCommentActivity.class);
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
<<<<<<< HEAD
							if(which == 0){ //Proximity to me
								
							}
							if(which == 1){ //Proximity to location
							
							}
							if(which == 2){ //Proximity to picture
							
							}
							if(which == 3){ //Scoring system
							
							}
							if(which == 4){ //Time
							
=======
							if (which == 0) { // Proximity to me
							}
							if (which == 1) { // Proximity to location
							}
							if (which == 2) { // Proximity to picture
							}
							if (which == 3) { // Scoring system
							}
							if (which == 4) { // Time
>>>>>>> james
							}
						}
					});
			return builder.create();
		}
<<<<<<< HEAD
			return null;
		}
		
		public class CommentReciever extends BroadcastReceiver{
			public static final String ACCEPT_COMMENTS = "cs.ualberta.cs.team5geotopics.ACCEPT_COMMENTS";
			@SuppressWarnings("unchecked")
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();
				List<CommentModel> comments = (List<CommentModel>) (bundle.getSerializable("comments"));
				if(comments.size() == 0){
					Log.w("GetAllService", "commentList is empty");
				}
				if(comments.get(0).equals(null)){
					Log.w("GetAllService", "element is null");
				}
				for(CommentModel cmnt : comments){
					clm.add(cmnt);
				}
			}
		}
		
=======
		return null;
	}

>>>>>>> james
}
