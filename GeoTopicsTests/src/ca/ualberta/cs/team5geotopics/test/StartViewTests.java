//package ca.ualberta.cs.team5geotopics.test;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.app.Instrumentation.ActivityMonitor;
//import android.content.Intent;
//import android.test.ActivityInstrumentationTestCase2;
//import android.widget.Button;
//import ca.ualberta.cs.team5geotopics.BrowseView;
//import ca.ualberta.cs.team5geotopics.StartActivity;
//
//import com.example.team5geotopics.R;
//
//public class StartViewTests extends ActivityInstrumentationTestCase2<StartActivity> {
//	Activity mActivity;
//	Instrumentation mInstrumentation;
//	Button mBrowseComments;
//	Intent mStartIntent;
//	final int TIMEOUT_IN_MS = 5000;
//	
//	public StartViewTests(){
//		super(StartActivity.class);
//	}
//	
//	protected void setUp() throws Exception{
//		super.setUp();
//		mActivity = getActivity();
//		mInstrumentation = getInstrumentation();
//		mBrowseComments = (Button) mActivity.findViewById(R.id.start_browse_top_level);
//		assertNotNull(mBrowseComments);
//		assertNotNull(mActivity);
//		assertNotNull(mInstrumentation);
//	}
//	
//	
//	
//	/*
//	 * This test will check to see if the BrowseComments activity is launched when the Browse Comments option
//	 * is selected at the start screen.
//	 */
//	
//	public void testBrowseActivityLaunched(){
//		// Add monitor to check for the second activity
//	      ActivityMonitor monitor = mInstrumentation.addMonitor(
//	          BrowseView.class.getName(), null, false);
//	      // find button and click it
//	      assertNotNull(monitor);
//	      assertNotNull(mBrowseComments);
//	      mActivity.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				mBrowseComments.performClick();
//				
//			}
//		});
//	      mInstrumentation.waitForIdleSync();
//	      
//
//	      
//	      BrowseView secondActivity = 
//	    		  (BrowseView) monitor.waitForActivityWithTimeout(25000);
//	      assertEquals("Monitor for ReceiverActivity has not been called",
//			        1, monitor.getHits());
//	      assertNotNull("Activity should not be null", secondActivity);
//	}
//}
