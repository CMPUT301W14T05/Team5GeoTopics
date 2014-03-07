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
		
		// Allows the buttons to be checked for a click event.
		locationBtn.setOnClickListener(this);
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);
		
		// Comments already exists, put in the data to fields
		setTitle("Edit Comment");
		EditText editText = (EditText)findViewById(R.id.editCommentTitleE);
		editText.setText(application.getCurrentViewingComment().getmTitle());
			
		editText = (EditText)findViewById(R.id.editCommentAuthorE);
		editText.setText(application.getCurrentViewingComment().getmAuthor());
			
		editText = (EditText)findViewById(R.id.editCommentBodyE);
		editText.setText(application.getCurrentViewingComment().getmBody());
			
		ImageView uploadedImage = (ImageView)findViewById(R.id.imageViewPictureE);    
		uploadedImage.setImageBitmap(application.getCurrentViewingComment().getPicture());
		
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
			uploadedImage = (ImageView)findViewById(R.id.imageViewPictureE);
			showDialog(0);
		}
		if (v == cancelBtn){
			finish();
		}
		// Gets all the data from the text boxes and submits it as a edited comment
		if (v == postBtn){
				EditText editText = (EditText)findViewById(R.id.editCommentTitleE);
				application.getCurrentViewingComment().setmTitle(editText.getText().toString());
				editText = (EditText)findViewById(R.id.editCommentAuthorE);
				application.getCurrentViewingComment().setmAuthor(editText.getText().toString());
				editText = (EditText)findViewById(R.id.editCommentBodyE);
				application.getCurrentViewingComment().setmBody(editText.getText().toString());
				application.getCurrentViewingComment().setmPicture(mPicture);
				application.getCurrentViewingComment().setmGeolocation(mGeolocation);
				finish();
		}
	}
}
