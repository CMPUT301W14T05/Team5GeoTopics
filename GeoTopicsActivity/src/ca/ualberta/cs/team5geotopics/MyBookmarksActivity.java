package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class MyBookmarksActivity extends BrowseActivity implements AView<AModel> {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ArrayList<CommentModel> myComments = new ArrayList<CommentModel>();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);
		
		//Change the title since we are piggy backing off the top level layout
		TextView title = (TextView) findViewById(R.id.top_level_title);
		title.setText("MY BOOKMARKS");

		// Get the application
		manager = CommentManager.getInstance();
		this.uController = new UserController();

		// Construct the model
		this.clm = new CommentListModel();
		
		//Find the user
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
	protected void onResume(){
		invalidateOptionsMenu();
		manager.refreshMyBookmarks(clm);
		myView.notifyDataSetChanged(); //Ensure the view is up to date.
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				CommentModel selected = (CommentModel) browseListView
						.getItemAtPosition(position);
				Intent intent = new Intent(MyBookmarksActivity.this, ReplyLevelActivity.class);
				intent.putExtra("ViewingComment",selected.getmEsID());
				intent.putExtra("ViewingParent", selected.getmParentID());
				uController.readingComment(selected);
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
	 * @return "MyBookmarks" The type of comment it is.
	 */
	@Override
	public String getType() {
		return "MyBookmarks";
	}
	
	// Ensures the proper action bar items are shown
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;
		item = menu.findItem(R.id.action_favourite);
		item.setVisible(false);
		item = menu.findItem(R.id.action_bookmark);
		item.setVisible(false);
		item = menu.findItem(R.id.action_my_bookmarks);
		item.setVisible(false);
		item = menu.findItem(R.id.new_top_level_comment);
		item.setVisible(false);

		return true;
	}


}
