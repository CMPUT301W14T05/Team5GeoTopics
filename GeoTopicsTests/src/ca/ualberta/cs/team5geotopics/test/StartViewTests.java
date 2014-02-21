package ca.ualberta.cs.team5geotopics.test;

import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class StartViewTests extends ActivityInstrumentationTestCase2<StartView> {

	Activity mActivity;
	Instrumentation mInstrumentation;
	Button mBrowseComments;
	Intent mStartIntent;
	
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
	
	//http://stackoverflow.com/questions/13041890/testing-that-an-activity-returns-the-expected-result/13113137#13113137
	//http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
	/*
	 * This test will check to see if the BrowseComments activity is launched when the Browse Comments option
	 * is selected at the start screen.
	 */
	
	public void testBrowseActivityLaunched(){
		ActivityMonitor monitor = mInstrumentation.addMonitor(BrowseTopLevelView.class.getName(), null, false);
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mBrowseComments.performClick();
				
			}
		});
		
		BrowseTopLevelView browseView = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
		// next activity is opened and captured.
		assertNotNull(browseView);
		browseView.finish();
	}
	
	
}
