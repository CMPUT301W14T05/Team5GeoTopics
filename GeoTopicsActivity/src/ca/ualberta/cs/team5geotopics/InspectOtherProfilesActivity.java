package ca.ualberta.cs.team5geotopics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
		b = getIntent().getExtras();
		profileID = b.getString("ProfileID");
		puller = new ProfileSearch();
		profile = puller.pullProfile(profileID, this);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.browse_view, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		this.updateUi();
		super.onResume();
	}
	
	private void updateUi(){
		if(profile != null){
			username = (EditText)findViewById(R.id.profile_username);
			contact = (EditText)findViewById(R.id.profile_contact);
			bio = (EditText)findViewById(R.id.biography_body);
			
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
