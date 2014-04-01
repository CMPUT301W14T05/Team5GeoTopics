package ca.ualberta.cs.team5geotopics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.team5geotopics.R;

/**
 * The view you see when you go to "Browse" from the start screen. It holds all
 * the top level comments and displays them to the user.
 */

public class TopLevelActivity extends BrowseActivity implements AView<AModel> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);

		// Get the singletons we may need.
		this.application = GeoTopicsApplication.getInstance();
		this.application.setContext(getApplicationContext());
		this.myUser = User.getInstance();
		this.manager = CommentManager.getInstance();
		this.uController = new UserController();
		me = this;

		// Construct the model
		this.clm = new CommentListModel();

		// Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item,
				clm.getList());
		// Register the view with the model
		this.clm.addView(this.myView);

		// Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);

		// Register with the user
		this.myUser.addView(this);
		
		webConnectionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (application.isNetworkAvailable()) {
					Log.w("Connectivity", "Have network");
					Log.w("Connectivity", "Refreshing an activity");
					manager.refresh(clm, me, viewingComment);
				}
			}
		};
		
	}

	@Override
	protected void onResume() {
		invalidateOptionsMenu();
		manager.refresh(this.clm, this, viewingComment);
		Log.w("Refresh", "After manager refresh");
		// Reset the current viewing comment
		myView.notifyDataSetChanged(); // Ensure the view is up to date.
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> myView, View view,
							int position, long arg3) {
						CommentModel selected = (CommentModel) browseListView
								.getItemAtPosition(position);
						if (!bookmark) {
							Intent intent = new Intent(TopLevelActivity.this,
									ReplyLevelActivity.class);
							intent.putExtra("ViewingComment",
									selected.getmEsID());
							intent.putExtra("ViewingParent",
									selected.getmParentID());
							uController.readingComment(selected);
							startActivity(intent);
						}else{
							uController.bookmark(selected);
							update(null);
						}
					}

				});
		
		registerReceiver(webConnectionReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(webConnectionReceiver);
		super.onPause();
	}
	
	/**
	 * @return "TopLevel" The type of comment it is.
	 */
	public String getType() {
		return "TopLevel";
	}

	/**
	 * The update code for the top level activity. Refreshes the list view and
	 * updates any comments in the clm.
	 */
	@Override
	public void update(AModel model) {
		this.myView.notifyDataSetChanged();
	}
	
	//Ensures the favourites icon is the right color
		public boolean onPrepareOptionsMenu(Menu menu) {
			MenuItem item;
			item = menu.findItem(R.id.action_favourite);
			item.setVisible(false);
			
			return true;
		}
}
