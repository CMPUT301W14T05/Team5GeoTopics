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
		
		handleCommentLoad();
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
						CommentModel selected = (CommentModel) browseListView
								.getItemAtPosition(position);
						//Add this to the cache
						mCache.addToHistory(selected);
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
	
	public String getType(){
		return "TopLevel";
	}
}
