package ca.ualberta.cs.team5geotopics;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.team5geotopics.R;

public class EditMyProfileActivity extends InspectProfileActivity implements OnClickListener{
	User user;
	EditText username;
	EditText contact;
	EditText bio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_my_profile);
		GeoTopicsApplication.getInstance().setContext(this);
		try{
			uController = new UserController();
		} catch (NullPointerException e){
			uController = new UserController(this.user);
		}
		username = (EditText)findViewById(R.id.edit_profile_username);
		contact = (EditText)findViewById(R.id.edit_profile_contact);
		bio = (EditText)findViewById(R.id.edit_biography_body);
		profileImage = (ImageView)findViewById(R.id.profile_image);  
		
		photoBtn = (ImageButton)findViewById(R.id.imageButtonImageP);
		cancelBtn = (ImageButton)findViewById(R.id.imageButtonCancelP);
		postBtn = (ImageButton)findViewById(R.id.imageButtonPostP);
		
		// Allows the buttons to be checked for a click event.
		photoBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		postBtn.setOnClickListener(this);
		
		myUser = User.getInstance();
		username.setText(myUser.getUserName());
		contact.setText(myUser.getContactInfo());
		bio.setText(myUser.getBiography());
		if(myUser.getProfilePic() != null){
			profileImage.setImageBitmap(myUser.getProfilePic());
			mPicture = myUser.getProfilePic();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.browse_view, menu);
		return true;
	}
	
	// This function will check for which button was clicked.
	@SuppressWarnings("deprecation")
	public void onClick(View v){
		if (v == photoBtn){
			profileImage = (ImageView)findViewById(R.id.profile_image);
			showDialog(0);
		}
		if (v == cancelBtn){
			finish();
		}
		// Gets all the data from the text boxes and submits it as a edited comment
		if (v == postBtn){
				username = (EditText)findViewById(R.id.edit_profile_username);
				contact = (EditText)findViewById(R.id.edit_profile_contact);
				bio = (EditText)findViewById(R.id.edit_biography_body);
				
				uController.updateProfile(username.getText().toString(), contact.getText().toString(), bio.getText().toString(), mPicture);
				finish();
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
}
