package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

/**
 * Shown when a user clicks on "My Comments" from the start screen of the application
 * In this activity, the user will be shown all of his/her comments including both top-level and reply comments.
 * Upon clicking a comment, EditCommentActivity will be shown and the user can edit the comment.
 * By clicking the '+' button, the user can create a new top-level comment. Sorting can also be done here.
 */

public class MyCommentsActivity extends BrowseActivity implements AView<AModel>{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ArrayList<CommentModel> myComments;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);
		
		//Change the title since we are piggy backing off the top level layout
		TextView title = (TextView) findViewById(R.id.top_level_title);
		title.setText("MY COMMENTS");

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software
		// back
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get the application
		application = GeoTopicsApplication.getInstance();

		// Construct the model
		this.clm = new CommentListModel();
		
		//Find the user
		this.myUser = User.getInstance();
		
		myComments = myUser.getMyComments();

		// Set my view to the history cache
		// This is a temporary fix
		this.clm.setList(myComments);
		
		/*Using a different solution for now
		//Register with all the comments in the list to get 
		for(CommentModel comment : myComments){
			comment.addView(this);
		}
		*/
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
		myView.notifyDataSetChanged(); //Ensure the view is up to date.
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				CommentModel selected = (CommentModel) browseListView
						.getItemAtPosition(position);
				Intent intent = new Intent(MyCommentsActivity.this, EditCommentActivity.class);
				intent.putExtra("ViewingComment",selected.getmEsID());
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
	 * @return "MyComments" The type of comment it is.
	 */
	@Override
	public String getType() {
		return "MyComments";
	}

}
