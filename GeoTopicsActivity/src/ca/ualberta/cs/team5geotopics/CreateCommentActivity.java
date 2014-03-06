package ca.ualberta.cs.team5geotopics;


import com.example.team5geotopics.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;


public class CreateCommentActivity extends InspectCommentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);
		setTitle("Create Comment");
		// Associates the button with their ID.
		locationBtn = (ImageButton)findViewById(R.id.imageButtonLocation);
		photoBtn = (ImageButton)findViewById(R.id.imageButtonImage);
		cancelBtn = (ImageButton)findViewById(R.id.imageButtonCancel);
		postBtn = (ImageButton)findViewById(R.id.imageButtonPost);
		
		application = GeoTopicsApplication.getInstance();
		
		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);
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
			
		}
		if (v == photoBtn){
			showDialog(0);
		}
		if (v == cancelBtn){
			finish();
		}
		// Gets all the data from the text boxes and submits it as a new comment
		if (v == postBtn){
				EditText editText = (EditText)findViewById(R.id.editCommentTitle);
				mTitle = editText.getText().toString();
				editText = (EditText)findViewById(R.id.editCommentAuthor);
				mAuthor = editText.getText().toString();
				editText = (EditText)findViewById(R.id.editCommentBody);
				mBody = editText.getText().toString();
			
				// Creates new top level comment.
				CommentModel topLevel = new CommentModel(mGeolocation, mBody, mAuthor, mPicture, mTitle);
				// Adds comment to top level browse
				// This will most likely change
				BrowseActivity.clm.add(topLevel);
				
				finish();
		}
	}
}
