package ca.ualberta.cs.team5geotopics;


import com.example.team5geotopics.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class EditCommentActivity extends InspectCommentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_comment);
		setTitle("Edit Comment");
		// Associates the button with their ID.
		locationBtn = (ImageButton)findViewById(R.id.imageButtonLocationE);
		photoBtn = (ImageButton)findViewById(R.id.imageButtonImageE);
		cancelBtn = (ImageButton)findViewById(R.id.imageButtonCancelE);
		postBtn = (ImageButton)findViewById(R.id.imageButtonPostE);
		
		application = GeoTopicsApplication.getInstance();
		this.controller = new CommentController();
		
		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);
		
		// Comments already exists, put in the data to fields
		setTitle("Edit Comment");
		title = (EditText)findViewById(R.id.editCommentTitleE);
		title.setText(application.getCurrentViewingComment().getmTitle());
			
		author = (EditText)findViewById(R.id.editCommentAuthorE);
		author.setText(application.getCurrentViewingComment().getmAuthor());
			
		body = (EditText)findViewById(R.id.editCommentBodyE);
		body.setText(application.getCurrentViewingComment().getmBody());
			
		uploadedImage = (ImageView)findViewById(R.id.imageViewPicture);    
		uploadedImage.setImageBitmap(application.getCurrentViewingComment().getPicture());
		mPicture = application.getCurrentViewingComment().getPicture();
		
		mGeolocation =  application.getCurrentViewingComment().getGeoLocation();
		
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
				
				controller.updateComment(application.getCurrentViewingComment(), title.getText().toString(), author.getText().toString(), body.getText().toString(), mPicture, mGeolocation);
				finish();
		}
	}
}
