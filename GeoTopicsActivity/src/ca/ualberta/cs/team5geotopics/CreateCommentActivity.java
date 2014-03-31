package ca.ualberta.cs.team5geotopics;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.team5geotopics.R;

/**
 * Called when a user hits the '+' button at the top of any screen that has one.
 * This action opens this activity which presents the user with options to fill in a Author,
 * Body, Title, and even attach a photo via the camera or via the local storage image gallery. 
 */

public class CreateCommentActivity extends InspectCommentActivity implements
		OnClickListener {
	
	Intent returnIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);
		myUser = User.getInstance();

		setTitle("Create Comment");

		// Find the edit text views
		this.title = (EditText) findViewById(R.id.editCommentTitle);
		this.author = (TextView) findViewById(R.id.editCommentAuthor);
		this.body = (EditText) findViewById(R.id.editCommentBody);
		
		this.author.setText(myUser.getUserName());

		// Associates the button with their ID.
		locationBtn = (ImageButton) findViewById(R.id.imageButtonLocation);
		photoBtn = (ImageButton) findViewById(R.id.imageButtonImage);
		cancelBtn = (ImageButton) findViewById(R.id.imageButtonCancel);
		postBtn = (ImageButton) findViewById(R.id.imageButtonPost);
		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);

		parentID = b.getString("ParentID");
		
		// Replies do not have titles and thus we should disable it OR make
		// a new activity/layout
		if (commentType.equals("ReplyLevel")) {
			this.title.setVisibility(View.GONE);
			findViewById(R.id.textViewTitle).setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.create_comment, menu);
		return true;
	}

	// This function will check for which button was clicked.
	@SuppressWarnings("deprecation")
	public void onClick(View v) {
		if (v == locationBtn) {
			showDialog(1);
			/*
			 * SIMPLY HERE FOR TESTING PURPOSES:
			 * 
			 * generates a different location than default. TODO: For next part
			 * of project, let users choose a custom location
			 */			///Location loc = new Location("loc");
			//loc.setLongitude(0.1);
			//loc.setLatitude(0);
			//mGeolocation = loc;
			/*------------------------------------------------------------------*/
		}
		if (v == photoBtn) {
			uploadedImage = (ImageView) findViewById(R.id.imageViewPicture);
			showDialog(0);
		}
		if (v == cancelBtn) {
			finish();
		}
		// Gets all the data from the text boxes and submits it as a new
		// comment
		if (v == postBtn) {
			if (commentType.equals("TopLevel"))
				this.mTitle = title.getText().toString();

			this.mAuthor = author.getText().toString();
			if(mAuthor.isEmpty()){
				mAuthor = "Anonymous";
			}
			this.mBody = body.getText().toString();
			if (mGeolocation == null) {

				Log.w("COMMENT_LOC", "geoLoc appears to be null");
				mGeolocation = myUser.getCurrentLocation();
				Log.w("COMMENT_LOC", "(" + Double.toString(mGeolocation.getLatitude()) + ", " + Double.toString(mGeolocation.getLongitude()) + ")");
				if(mGeolocation == null)
					Log.w("COMMENT_LOC", "still null");

			}
			User user = User.getInstance();
			if (commentType.equals("TopLevel")) {
				Log.w("CreateCommentActivity", "viewingComment == null");
				// Creates new top level comment.
				newComment = new CommentModel(Double.toString(mGeolocation.getLatitude()), 
						Double.toString(mGeolocation.getLongitude()), mBody, myUser.getUserName(), mPicture, mTitle, myUser.getProfileID());
				newComment.setES(
						user.readInstallIDFile() + user.readPostCount(), "-1",
						"TopLevel");
				controller.newTopLevel(newComment);
			} else {
				Log.w("MID", "Lat: " + Double.toString(mGeolocation.getLatitude()));
				Log.w("MID", "Long: " + Double.toString(mGeolocation.getLongitude()));
				Log.w("MID", "body: " + mBody);
				Log.w("MID", "User: " + myUser.getUserName());
				if(mPicture != null)
					Log.w("MID", "Pic: Not NUll");
				Log.w("MID", "PID: " + myUser.getProfileID());
				newComment = new CommentModel(Double.toString(mGeolocation.getLatitude()), 
						Double.toString(mGeolocation.getLongitude()), 
						mBody, myUser.getUserName(), mPicture, myUser.getProfileID());
				newComment.setES(
						user.readInstallIDFile() + user.readPostCount(), this.parentID, 
						this.parentID);
				controller.newReply(newComment, this);
			}
			returnIntent = new Intent();
			returnIntent.putExtra("esID", newComment.getmEsID());
			returnIntent.putExtra("parentId", newComment.getmParentID());
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}
}
