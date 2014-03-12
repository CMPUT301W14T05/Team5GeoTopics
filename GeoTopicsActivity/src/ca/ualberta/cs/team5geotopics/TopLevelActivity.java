package ca.ualberta.cs.team5geotopics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.team5geotopics.R;

public class TopLevelActivity extends BrowseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);

		// Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item,
				clm.getList());
		// Register the view with the model
		this.clm.addView(this.myView);

		// Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);

		if (isNetworkAvailable()) {
			modelController = new CommentListController(this.clm);
			modelController.getTopLevel(this);
			Log.w("Cache", "Have Internet");
		} else {
			Log.w("Cache", "No Internet");
			// Need a spinner here
			if (mCache.isCacheLoaded()) {
				Log.w("Cache", "Cache is loaded");
				this.clm.replaceList(mCache.getHistory());
				Log.w("Cache", "Got History");
			} else {
				// Waiting for a few seconds then trying to cache again in case
				// its still loading.
				// This may casue the UI thread to become none responsive not
				// sure so may want to find a better
				// way to do this. Maybe a loop with a small wait, One that will
				// not freeze the ui thread.
				// ---------------------------------------------------------------------------------------------
				// wait(3000);
				if (mCache.isCacheLoaded()) {
					Log.w("Cache", "Cache is loaded");
					this.clm.replaceList(mCache.getHistory());
				} else {
					// Should put the toast string inside the strings xml
					Toast toast = Toast.makeText(this,
							"Unable to load the cache, Please try again later",
							5);
					toast.show();
					Log.w("Cache", "Not loaded");
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// Reset the current viewing comment
		myView.notifyDataSetChanged(); // Ensure the view is up to date.
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> myView, View view,
							int position, long arg3) {
						Intent intent = new Intent(TopLevelActivity.this,
								ReplyLevelActivity.class);
						intent.putExtra("ViewingComment",
								(CommentModel) browseListView
										.getItemAtPosition(position));
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
