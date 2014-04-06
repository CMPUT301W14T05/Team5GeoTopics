package ca.ualberta.cs.team5geotopics.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageButton;
import ca.ualberta.cs.team5geotopics.EditMyProfileActivity;


import ca.ualberta.cs.team5geotopics.User;

import com.example.team5geotopics.R;

public class AuthorTests extends ActivityInstrumentationTestCase2<EditMyProfileActivity> {
	private EditMyProfileActivity mActivity;
	private User user;
	private String oldUsername;
	private EditText userNameText;
	private ImageButton postBtn;
	private final String testUserName = "TestUserName";
	
	public AuthorTests(){
		super(EditMyProfileActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		user = User.getInstance();
		
		
		oldUsername = user.getUserName();
		userNameText = (EditText) mActivity.findViewById(R.id.edit_profile_username);
		postBtn = (ImageButton) mActivity.findViewById(R.id.imageButtonPostP);
	}
	
	public void testAuthorSetsUsername(){
		
		
		try {
			runTestOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					userNameText.setText(testUserName);
					postBtn.performClick();
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		assertTrue("user name is now: " + testUserName, user.getUserName().equals(testUserName));
		user.setUserName(oldUsername);
		
	}
}
