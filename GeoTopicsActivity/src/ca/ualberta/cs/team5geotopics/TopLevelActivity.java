package ca.ualberta.cs.team5geotopics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.team5geotopics.R;

/**
 * The view you see when you go to "Browse" from the start screen.
 * It holds all the top level comments and displays them to the user.
 */

public class TopLevelActivity extends BrowseActivity implements AView<AModel>{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);
		
		//Get the singletons we may need.
		this.application = GeoTopicsApplication.getInstance();
		this.application.setContext(getApplicationContext());
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();

		//Construct the model
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
		
		
	}

	@Override
	protected void onResume() {
		handleCommentLoad();
		// Reset the current viewing comment
		myView.notifyDataSetChanged(); // Ensure the view is up to date.
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> myView, View view,
							int position, long arg3) {
						CommentModel selected = (CommentModel) browseListView
								.getItemAtPosition(position);
						//Add this to the cache
						//mCache.addToHistory(selected);
						Intent intent = new Intent(TopLevelActivity.this,
								ReplyLevelActivity.class);
						intent.putExtra("ViewingComment",selected);
						startActivity(intent);
					}

				});

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	

	/**
	 * @return "TopLevel" The type of comment it is.
	 */
	public String getType(){
		return "TopLevel";
	}

	
	/**
	 * The update code for the top level activity. Refreshes the 
	 * list view and updates any comments in the clm.
	 */
	@Override
	public void update(AModel model) {
		if (model instanceof CommentModel) {
			clm.updateComment((CommentModel)model);
		}
		this.myView.notifyDataSetChanged();
	}
}
