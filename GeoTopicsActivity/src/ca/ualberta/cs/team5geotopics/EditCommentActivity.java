package ca.ualberta.cs.team5geotopics;


import com.example.team5geotopics.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * EditCommentActivity is called when a user is in the "My Comments" activity and
 * selects that he/she wishes to edit a comment by clicking on it.
 * 
 * This source code will allow the user to modify any field that he/she wishes
 * and even take a new photo if they want too.
 */
public class EditCommentActivity extends InspectCommentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_comment);
		setTitle("Edit Comment");
		
		this.manager = CommentManager.getInstance();
		
		Bundle b = getIntent().getExtras();
		viewingComment = manager.getMyComment(b.getString("ViewingComment"));
		// Associates the button with their ID.
		locationBtn = (ImageButton)findViewById(R.id.imageButtonLocationE);
		photoBtn = (ImageButton)findViewById(R.id.imageButtonImageE);
		cancelBtn = (ImageButton)findViewById(R.id.imageButtonCancelE);
		postBtn = (ImageButton)findViewById(R.id.imageButtonPostE);
		
		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);
		
		// Comments already exists, put in the data to fields
		setTitle("Edit Comment");
		title = (EditText)findViewById(R.id.editCommentTitleE);
		title.setText(viewingComment.getmTitle());
			
		author = (EditText)findViewById(R.id.editCommentAuthorE);
		author.setText(viewingComment.getmAuthor());
			
		body = (EditText)findViewById(R.id.editCommentBodyE);
		body.setText(viewingComment.getmBody());
			
		uploadedImage = (ImageView)findViewById(R.id.imageViewPicture);    
		uploadedImage.setImageBitmap(viewingComment.getPicture());
		mPicture = viewingComment.getPicture();
		
		mGeolocation =  viewingComment.getGeoLocation();
		
		// Replies do not have titles and thus we should disable it OR make
		// a new activity/layout
		if (viewingComment != null) {
			this.title.setVisibility(View.GONE);
			//findViewById(R.id.textViewTitle).setVisibility(View.GONE);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_comment, menu);
		return true;
	}
	
	// This function will check for which button was clicked.
	@SuppressWarnings("deprecation")
	public void onClick(View v){
		if (v == locationBtn){
			showDialog(1);
			
		}
		if (v == photoBtn){
			uploadedImage = (ImageView)findViewById(R.id.imageViewPicture);
			showDialog(0);
		}
		if (v == cancelBtn){
			finish();
		}
		// Gets all the data from the text boxes and submits it as a edited comment
		if (v == postBtn){
				title = (EditText)findViewById(R.id.editCommentTitleE);
				author = (EditText)findViewById(R.id.editCommentAuthorE);
				body = (EditText)findViewById(R.id.editCommentBodyE);
				
				//application.getCurrentViewingComment().setmPicture(mPicture);
				//application.getCurrentViewingComment().setmGeolocation(mGeolocation);
				
				controller.updateComment(viewingComment, title.getText().toString(), author.getText().toString(), body.getText().toString(), mPicture, mGeolocation);
				finish();
		}
	}
}
