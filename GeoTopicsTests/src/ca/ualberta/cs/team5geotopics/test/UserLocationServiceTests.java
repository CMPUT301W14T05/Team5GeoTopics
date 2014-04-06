package ca.ualberta.cs.team5geotopics.test;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.TopLevelActivity;
import ca.ualberta.cs.team5geotopics.UserLocationServices;

public class UserLocationServiceTests extends
		ActivityInstrumentationTestCase2<TopLevelActivity> {

	public UserLocationServiceTests() {
		super(TopLevelActivity.class);
	}
	
	public void testGetCurrentLocation() {
		UserLocationServices uls = new UserLocationServices();
		
		Location myLoc = null;
		assertNull(myLoc);
		
		myLoc = uls.getCurrentLocation();
		assertNotNull(myLoc);
		assertEquals("Since there is no provider the last location is (0,0)", 0.0, myLoc.getLatitude());
		assertEquals("Since there is no provider the last location is (0,0)", 0.0, myLoc.getLongitude());
		
		
	}

}
