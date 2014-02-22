package ca.ualberta.cs.team5geotopics.test;

import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.widget.Button;

public class StartViewTests extends ActivityInstrumentationTestCase2<StartView> {

	Activity mActivity;
	Instrumentation mInstrumentation;
	Button mBrowseComments;
	Intent mStartIntent;
	final int TIMEOUT_IN_MS = 10000;
	
	public StartViewTests(){
		super(StartView.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
		
		
		mBrowseComments = (Button) mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.id.browseCommentsBtn);
		
		
	}
	
	public final void testPreConditions(){
		assertNotNull(mActivity);
		assertNotNull(mBrowseComments);
		assertNotNull(mInstrumentation);
	}
	
	//http://developer.android.com/training/activity-testing/activity-functional-testing.html
	/*
	 * This test will check to see if the BrowseComments activity is launched when the Browse Comments option
	 * is selected at the start screen.
	 */
	@UiThreadTest
	public void testBrowseActivityLaunched(){
		// Set up an ActivityMonitor
		ActivityMonitor receiverActivityMonitor =
		        getInstrumentation().addMonitor(BrowseTopLevel.class.getName(),
		        null, false);

		// Validate that ReceiverActivity is started
		TouchUtils.clickView(this, mBrowseComments);
		BrowseTopLevel browseTopLevel = (BrowseTopLevel) 
		        receiverActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		assertNotNull("ReceiverActivity is null", browseTopLevel);
		assertEquals("Monitor for ReceiverActivity has not been called",
		        1, browseTopLevel.getHits());
		assertEquals("Activity is of wrong type",
				BrowseTopLevel.class, browseTopLevel.getClass());

		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);
	}
	
	
}
