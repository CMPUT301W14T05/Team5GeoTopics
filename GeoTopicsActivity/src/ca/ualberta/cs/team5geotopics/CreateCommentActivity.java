package ca.ualberta.cs.team5geotopics;

import com.example.team5geotopics.R;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateCommentActivity extends InspectCommentActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);

		this.application = GeoTopicsApplication.getInstance();
		this.mCache = Cache.getInstance();
		this.viewingComment = application.getCurrentViewingComment();
		this.myUser = User.getInstance();

		setTitle("Create Comment");
		// Associates the button with their ID.
		locationBtn = (ImageButton) findViewById(R.id.imageButtonLocation);
		photoBtn = (ImageButton) findViewById(R.id.imageButtonImage);
		cancelBtn = (ImageButton) findViewById(R.id.imageButtonCancel);
		postBtn = (ImageButton) findViewById(R.id.imageButtonPost);

		application = GeoTopicsApplication.getInstance();

		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);

		//Find the edit text views
		title = (EditText) findViewById(R.id.editCommentTitle);
		author = (EditText) findViewById(R.id.editCommentAuthor);
		body = (EditText) findViewById(R.id.editCommentBody);

		// Replies do not have titles and thus we should disable it OR make a
		// new activity/layout
		if (viewingComment != null) {
			title.setVisibility(View.GONE);
			findViewById(R.id.textViewTitle).setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_comment, menu);
		return true;
	}

	// This function will check for which button was clicked.
	@SuppressWarnings("deprecation")
	public void onClick(View v){
		if (v == locationBtn){
			showDialog(1);
			/*
			 * SIMPLY HERE FOR TESTING PURPOSES:
			 * 
			 * generates a different location than default. TODO: For next part
			 * of project, let users choose a custom location
			 */
			Location loc = new Location("loc");
			loc.setLongitude(0.1);
			loc.setLatitude(0);
			mGeolocation = loc;
			/*------------------------------------------------------------------*/
		}
		if (v == photoBtn) {
			showDialog(0);
		}
		if (v == cancelBtn) {
			finish();
		}
		// Gets all the data from the text boxes and submits it as a new comment
		if (v == postBtn) {
			if (viewingComment == null)
				mTitle = title.getText().toString();
			
			mAuthor = author.getText().toString();
			mBody = body.getText().toString();
			if (mGeolocation == null) {
				/*
				 * For Now we just set a default location TODO: (next part) auto
				 * retrieve location using Mock Provider
				 */
				Location loc = new Location("loc");
				loc.setLongitude(0.005);
				loc.setLatitude(0);
				mGeolocation = loc;
			}

			if (viewingComment == null) {
				// Creates new top level comment.
				newComment = new CommentModel(mGeolocation, mBody,
						mAuthor, mPicture, mTitle);
				mCache.addToHistory(newComment, this);
				// CommentModel topLevel = new CommentModel(mGeolocation, mBody,
				// mAuthor, mPicture, mTitle);
				// Adds comment to top level browse
				// This will most likely change
				// BrowseActivity.clm.add(topLevel);
			} else {
				newComment = new CommentModel(mGeolocation, mBody,
						mAuthor, mPicture, mTitle);
				viewingComment.addReply(newComment);	
			}
			//Add the new comment to my comments.
			myUser.addToMyComments(newComment, this);

			finish();
		}
	}
}
