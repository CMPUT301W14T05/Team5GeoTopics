package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
import ca.ualberta.cs.team5geotopics.InspectOtherProfilesActivity;
import ca.ualberta.cs.team5geotopics.ProfilePush;
import ca.ualberta.cs.team5geotopics.ProfileSearch;
import ca.ualberta.cs.team5geotopics.User;

public class ProfileTests extends ActivityInstrumentationTestCase2<InspectOtherProfilesActivity> {
	private InspectOtherProfilesActivity mActivity;
	private User user;
	private ProfilePush pp;
	private String oldId;
	private ProfileSearch ps;
	
	public ProfileTests(){
		super(InspectOtherProfilesActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		mActivity = getActivity();
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		application.setContext(mActivity);
		user = User.getInstance();
		oldId = user.getProfileID();
		user.setmID("testProfile");
		pp = new ProfilePush();
		ps = new ProfileSearch();
		
		
	}
	
	// tests pushing and pulling a test profile
	public void testPushAndPullProfile(){
		Intent intent = new Intent();
		intent.putExtra("ProfileID", "testProfile");
		setActivityIntent(intent);
		JestResult result = null;
		Thread thread = pp.pushProfile(user);
		try{
			thread.join();
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			Log.w("aPushTest", "interrupt exception");
		}
		
		Thread thread2 = ps.pullProfile("testProfile", mActivity);
		try{
			thread2.join();
		}
		catch(InterruptedException e){
			Log.w("bPullTest", "interrupt exception");
		}
		
		result = ps.returnResult();
		assertTrue("JestResult is not null", result != null);
		Log.w("bTest", result.getJsonString());
		assertTrue("JestResult suceeded", ps.returnResult().isSucceeded());
		
		
		user.setmID(oldId);
	}
	
	
}
