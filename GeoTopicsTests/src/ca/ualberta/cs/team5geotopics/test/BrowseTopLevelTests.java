package ca.ualberta.cs.team5geotopics.test;

import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BrowseTopLevelTests extends ActivityInstrumentationTestCase2<BrowseTopLevelView> {
	Activity mActivity;
	Instrumentation mInstrumentation;
	Button mCreateNewComment;
	Intent mStartIntent;
	ListView mTopLevelListView;
	
	public BrowseTopLevelTests(){
		super(BrowseTopLevelView.class);
	}
	
	protected void setUp(){
		super.setUp();
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
		mCreateNewComment = (Button)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.id.newCommentBtn);
		mTopLevelListView = (ListView)mActivity.findViewById(ca.ualberta.cs.team5geotopicsR.id.topLevelListView);
	}
	
	public final void testPreConditions(){
		assertNotNull(mActivity);
		assertNotNull(mInstrumentation);
		assertNotNull(mCreateNewComment);
		assertNotNull(mTopLevelListView);
	}
	
	//http://stackoverflow.com/questions/13041890/testing-that-an-activity-returns-the-expected-result/13113137#13113137
	//http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
		
	/*
	 * this tests that the CreateCommentActivity is launched if the 
	 * new comment button is pressed in the BrowseTopLevel Activity.
	 */
	
	public void testCreateCommentLaunched(){
		ActivityMonitor monitor = mInstrumentation.addMonitor(CreateCommentView.class.getName(), null, false);
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mCreateNewComment.performClick();
				
			}
		});
		
		CreateCommentView newCommentView = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
		// next activity is opened and captured.
		assertNotNull(newCommentView);
		newCommentView.finish();
	}
	
	/*
	 * this tests to see if we add a new TopLevel Comment to the QueueController
	 * in list and push the list, then the TopLevel Comment appears in the ListView
	 * for BrowseTopLevelView. At this point in the development the Comment is text only. 
	 * 
	 * This test mocks the functionality of the Web Service, ie, no real internet stuff.
	 */
	@UiThreadTest
	public void testCreateTopLevelAppearsOnlyTextNoWeb(){
		// our mock values
		final String EXPECTED_TITLE = "Test Top Level Comment";
		final String EXPECTED_BODY = "This is body text.";
		final String EXPECTED_AUTHOR = "Peter Watts";
		Comment newTopLevel = new Comment(EXPECTED_TITLE, EXPECTED_BODY, EXPECTED_AUTHOR
										  null, null);
		
		// this is the adapter for the ListVIew
		ArrayAdapter<Comment> adapter = mTopLevelListView.getAdapter();
		
		// this should get GeoTOpicsApplication
		GeoTopicsApplication application = (GeoTopicsApplication) mActivity.getApplication();
		
		// this is the controller for the web service model
		QueueController qController = application.getQueueController();
		
		// lets pretend that the mock comment was just pulled by our Web Service Controller
		qController.getTopLevelInList().add(newTopLevel);
		
		// now that the list has changed we need to update 
		// the CommentListAdapter for BrowseTopLevelView. 
		// The adapter should have been notified of a change
		// in the mTopLevelInList in the QueueController
		
		
		// assert that the adapter has only one comment
		assertTrue(adapter.size() == 1);
		
		// assert that the zero'th Comment in the adapter is a TopLevelComment
		// assumes CommentModel has a getType() method that returns a string indicating
		// the model's type (Reply, TopLevel).
		assertTrue(adapter.getItem(0).getType().equals("TopLevel"));
		
		// this is that comment in the adapter
		TopLevelComment topLevel = (TopLevelComment) adapter.getItem(0);
		
		// assert that the TopLevel Comment's state is equal to our mock Comment
		assertTrue(topLevel.equals(newTopLevel));
		
		// now we need to assert that the view for the TopLevelComment
		// is on the ListView
		// http://stackoverflow.com/questions/11541114/unittesting-of-arrayadapter
		ViewAsserts.assertOnScreen(mTopLevelListView, adapter.getView(0, null, null));
	}
}
