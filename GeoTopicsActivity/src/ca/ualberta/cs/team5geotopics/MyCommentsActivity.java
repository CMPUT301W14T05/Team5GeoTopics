package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

/**
 * Shown when a user clicks on "My Comments" from the start screen of the
 * application In this activity, the user will be shown all of his/her comments
 * including both top-level and reply comments. Upon clicking a comment,
 * EditCommentActivity will be shown and the user can edit the comment. By
 * clicking the '+' button, the user can create a new top-level comment. Sorting
 * can also be done here.
 */

public class MyCommentsActivity extends BrowseActivity implements AView<AModel> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ArrayList<CommentModel> myComments = new ArrayList<CommentModel>();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);

		// Change the title since we are piggy backing off the top level layout
		TextView title = (TextView) findViewById(R.id.top_level_title);
		title.setText("MY COMMENTS");

		// Get the application
		application = GeoTopicsApplication.getInstance();
		manager = CommentManager.getInstance();

		// Construct the model
		this.clm = new CommentListModel();

		// Find the user
		this.myUser = User.getInstance();

		// Set my view to the history cache
		// This is a temporary fix
		this.clm.setList(myComments);

		// Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item,
				clm.getList());
		// Register the view with the model
		this.clm.addView(this.myView);

		// Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);

	}

	@Override
	protected void onResume() {
		invalidateOptionsMenu();
		manager.refreshMyComments(clm);
		myView.notifyDataSetChanged(); // Ensure the view is up to date.
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> myView, View view,
							int position, long arg3) {
						CommentModel selected = (CommentModel) browseListView
								.getItemAtPosition(position);
						Intent intent = new Intent(MyCommentsActivity.this,
								EditCommentActivity.class);
						intent.putExtra("ViewingComment", selected.getmEsID());
						intent.putExtra("ViewingParent",
								selected.getmParentID());
						startActivity(intent);
					}

				});
		super.onResume();
	}

	/**
	 * Updates the model.
	 */
	@Override
	public void update(AModel model) {
		// TODO Auto-generated method stub
		myView.notifyDataSetChanged();
	}

	/**
	 * Gets the current type of comment.
	 * 
	 * @return "MyComments" The type of comment it is.
	 */
	@Override
	public String getType() {
		return "MyComments";
	}

	// Ensures the proper action bar items are shown
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;
		item = menu.findItem(R.id.action_favourite);
		item.setVisible(false);
		item = menu.findItem(R.id.action_bookmark);
		item.setVisible(false);
		item = menu.findItem(R.id.action_my_comments);
		item.setVisible(false);
		item = menu.findItem(R.id.new_top_level_comment);
		item.setVisible(false);

		return true;
	}

}
