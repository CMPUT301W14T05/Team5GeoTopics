package ca.ualberta.cs.team5geotopics;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.team5geotopics.R;


public class CreateCommentActivity extends InspectCommentActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);

		setTitle("Create Comment");

		// Find the edit text views
		this.title = (EditText) findViewById(R.id.editCommentTitle);
		this.author = (EditText) findViewById(R.id.editCommentAuthor);
		this.body = (EditText) findViewById(R.id.editCommentBody);

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
		
		// Replies do not have titles and thus we should disable it OR make
		// a new activity/layout
		if (viewingComment != null) {
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
			 */
			Location loc = new Location("loc");
			loc.setLongitude(0.1);
			loc.setLatitude(0);
			mGeolocation = loc;
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
			if (viewingComment == null)
				this.mTitle = title.getText().toString();

			this.mAuthor = author.getText().toString();
			this.mBody = body.getText().toString();
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
			User user = User.getInstance();
			if (viewingComment == null) {
				// Creates new top level comment.
				newComment = new CommentModel(String.valueOf(mGeolocation
						.getLatitude()), String.valueOf(mGeolocation
						.getLongitude()), mBody, mAuthor, mTitle, mPicture);
				newComment.setES(
						user.readInstallIDFile() + user.readPostCount(), "-1",
						user.readInstallIDFile());
				controller.newTopLevel(newComment);
			} else {
				newComment = new CommentModel(mGeolocation, mBody, mAuthor,
						mPicture, mTitle);
				controller.newReply(newComment, viewingComment, this);
			}
			finish();
		}
	}
}
