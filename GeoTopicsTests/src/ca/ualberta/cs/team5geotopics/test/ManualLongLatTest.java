package ca.ualberta.cs.team5geotopics.test;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.ManualLocationActivity;

public class ManualLongLatTest extends ActivityInstrumentationTestCase2<ManualLocationActivity> {

	public ManualLongLatTest() {
		super(ManualLocationActivity.class);
	}

	public void testManualLongLatTest(){
		
		String longitude = "-4.7";
		String latitude = "45.21";
		
		Location loc = new Location("loc");
		loc.setLatitude(Double.parseDouble(latitude));
		loc.setLongitude(Double.parseDouble(longitude));
	
		assertTrue("Longitude is correct", loc.getLongitude() == -4.7);
		assertTrue("Latitude is correct", loc.getLatitude() == 45.21);
		}
	}