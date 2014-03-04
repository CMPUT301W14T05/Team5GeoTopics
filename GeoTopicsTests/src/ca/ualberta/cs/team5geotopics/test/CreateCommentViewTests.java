//package ca.ualberta.cs.team5geotopics.test;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import org.w3c.dom.Comment;
//
//import com.example.team5geotopics.R;
//
//import ca.ualberta.cs.team5geotopics.CommentView;
//import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
//import ca.ualberta.cs.team5geotopics.NewCommentView;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.app.Instrumentation.ActivityMonitor;
//import android.content.Intent;
//import android.test.ActivityInstrumentationTestCase2;
//import android.test.TouchUtils;
//import android.test.UiThreadTest;
//import android.test.ViewAsserts;
//import android.webkit.WebView.FindListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//public class CreateCommentViewTests extends
//		ActivityInstrumentationTestCase2<NewCommentView> {
//	
//	NewCommentView mActivity;
//	Instrumentation mInstrumentation;
//	EditText mTitle;
//	EditText mBody;
//	EditText mAuthor;
//	Button mPost;
//	final int TIMEOUT_IN_MS = 10000;
//	
//	public CreateCommentViewTests(){
//		super(NewCommentView.class);
//	}
//	
//	protected void setUp() throws Exception{
//		super.setUp();
//		mActivity = getActivity();
//		assertNotNull(mActivity);
//		mInstrumentation = getInstrumentation();
//		assertNotNull(mInstrumentation);
//		mTitle = (EditText)mActivity.findViewById(R.id.new_comment_title);
//		assertNotNull(mTitle);
//		mBody = (EditText)mActivity.findViewById(R.id.new_comment_body);
//		assertNotNull(mBody);
//		mAuthor = (EditText)mActivity.findViewById(R.id.new_comment_author);
//		assertNotNull(mAuthor);
//		mPost = (Button)mActivity.findViewById(R.id.new_comment_ok);
//		assertNotNull(mPost);
//	}
//	
//	/*
//	 * This tests to see if a new Top Level Comment is created if
//	 * all the applicable fields are filled out, and buttons are pressed.
//	 * After the comment is created CommentView is launched. Make
//	 * sure data in CommentView matches the data in test.
//	 * 
//	 * --> at the moment there is no location funcitonality. <--
//	 */
//	//TODO: implement date field check in test & view
//	//TODO: implement picture field check in test & view
//	//TODO: implement location field check in test & view
//	public void testCreateTopLevelCommentOnlyText() throws Throwable{
//		//I'm not sure but we might have to call getActivity() here
//		//because the previous test we "ended" in another actvitiy?
//	
//		
//		// our input values
//		final String EXPECTED_TITLE = "Test Top Level Comment";
//		final String EXPECTED_BODY = "This is body text.";
//		final String EXPECTED_AUTHOR = "Peter Watts";
//		final SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy:MM:DDD:HH");
//		// calendar object to get time at creation
//		Calendar creationTime;
//		
//		runTestOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				//input the values into the UI
//				mTitle.requestFocus();
//				mTitle.setText(EXPECTED_TITLE);
//				mBody.requestFocus();
//				mBody.setText(EXPECTED_BODY);
//				mAuthor.requestFocus();
//				mAuthor.setText(EXPECTED_AUTHOR);
//				
//			}
//		});
//		mInstrumentation.waitForIdleSync();
//		
//		
//		/*
//		 *  In here we test funcitonality of pictures and location changing
//		 *  ie, before we hit post.
//		 */
//		// get the time
//		creationTime = Calendar.getInstance();
//		//final String EXPECTED_TIME = expectedDateFormat.format(creationTime).toString();
//		/*
//		 * I'm assuming that after you post the comment a View of only that comment is loaded.
//		 * Like the OP on a forum.
//		 */
//		
//		// Set up an ActivityMonitor
//		ActivityMonitor monitor = mInstrumentation.addMonitor(CommentView.class.getName(),
//													null, false);
//		
//		runTestOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// post the comment
//				mPost.performClick();
//				
//			}
//		});
//		mInstrumentation.waitForIdleSync();
//		
//		// Validate that ReceiverActivity is started
//		CommentView commentView = (CommentView)
//				monitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
//		
//		assertEquals("Monitor for ReceiverActivity has not been called",
//				        1, monitor.getHits());
//		assertNotNull("ReceiverActivity is null", commentView);
//		
//		
//		
//		// we should now be in CommentView Activity
//		TextView textAuthor = (TextView)commentView.findViewById
//										(R.id.comment_view_author);
//		
//		TextView textBody = (TextView)commentView.findViewById
//										(R.id.comment_view_body);
//				
//		TextView textTitle = (TextView)commentView.findViewById
//										(R.id.comment_view_title);
//		
//		
//		
//		// assert that we got the views from the activity
//		assertNotNull(textAuthor);
//		assertNotNull(textBody);
//		assertNotNull(textTitle);
//		
//		ViewAsserts.assertOnScreen(textAuthor.getRootView(), textAuthor);
//		ViewAsserts.assertOnScreen(textBody.getRootView(), textBody);
//		ViewAsserts.assertOnScreen(textTitle.getRootView(), textTitle);
//		/*
//		 * add some ViewAsserts
//		 * 
//		 */
//		
//		/*
//		 * add some assertions about location and picture being null.
//		 * 
//		 */
//		
//		
//		
//		
//		// turn the text in the views into appropriate strings
//		String author = textAuthor.getText().toString();
//		String body = textBody.getText().toString();
//		String title = textTitle.getText().toString();
//		
//		
//		// test to see if the strings are equal to the expected values
//		assertEquals("Title should be " + EXPECTED_TITLE, EXPECTED_TITLE, title);
//		assertEquals("Body should be " + EXPECTED_BODY, EXPECTED_BODY, body);
//		
//		assertEquals("Author should be " + EXPECTED_AUTHOR, EXPECTED_AUTHOR, author);
//		
//		
//		
//		
//		// Remove the ActivityMonitor
//		mInstrumentation.removeMonitor(monitor);
//	}
//}
