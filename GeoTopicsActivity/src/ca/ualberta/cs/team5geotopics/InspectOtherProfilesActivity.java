package ca.ualberta.cs.team5geotopics;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class InspectOtherProfilesActivity extends InspectProfileActivity{
	
	User profile;
	ProfileSearch puller;
	Bundle b;
	String profileID;
	TextView username;
	TextView contact;
	TextView bio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspect_other_profiles);
		
		username = (TextView)findViewById(R.id.profile_username);
		contact = (TextView)findViewById(R.id.profile_contact);
		bio = (TextView)findViewById(R.id.biography_body);
		profileImage = (ImageView)findViewById(R.id.profile_image); 
		
		
		b = getIntent().getExtras();
		profileID = b.getString("ProfileID");
		puller = new ProfileSearch();
		puller.pullProfile(profileID, this);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.browse_view, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		this.updateUi();
		super.onResume();
	}
	
	private void updateUi(){
		if(profile != null){
			if(profile.getProfilePic() != null){
				profileImage.setImageBitmap(profile.getProfilePic());
			}		
			username.setText(profile.getUserName());
			contact.setText(profile.getContactInfo());
			bio.setText(profile.getBiography());
		}
	}
	
	public void passProfile(User profile){
		this.profile = profile;
		this.updateUi();
	}

}
