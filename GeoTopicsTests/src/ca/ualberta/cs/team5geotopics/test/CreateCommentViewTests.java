package ca.ualberta.cs.team5geotopics.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Comment;

import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateCommentViewTests extends
		ActivityInstrumentationTestCase2<CreateCommentView> {
	
	CreateCommentView mActivity;
	Instrumentation mInstrumentation;
	EditText mTitle;
	EditText mBody;
	EditText mAuthor;
	ImageButton mPost;
	final int TIMEOUT_IN_MS = 10000;
	
	public CreateCommentViewTests(){
		super(CreateCommentView.class);
	}
	
	protected void setUp(){
		super.setUp();
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
		mTitle = (EditText)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.commentTitleEditTxt);
		mBody = (EditText)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.commentBodyEditTxt);
		mAuthor = (EditText)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.commentAuthorEditTxt);
		mPost = (ImageButton)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.commentPostBtn);
	}
	
	public final void testPreConditions(){
		assertNotNull(mActivity);
		assertNotNull(mInstrumentation);
		assertNotNull(mTitle);
		assertNotNull(mBody);
		assertNotNull(mAuthor);
		assertNotNull(mPost);
	}
	
	/*
	 * This tests to see if a new Top Level Comment is created if
	 * all the applicable fields are filled out, and buttons are pressed.
	 * After the comment is created CommentView is launched. Make
	 * sure data in CommentView matches the data in test.
	 * 
	 * --> at the moment there is no location funcitonality. <--
	 */
	@UiThreadTest
	public void testCreateTopLevelCommentOnlyText(){
		//I'm not sure but we might have to call getActivity() here
		//because the previous test we "ended" in another actvitiy?
	
		
		// our input values
		final String EXPECTED_TITLE = "Test Top Level Comment";
		final String EXPECTED_BODY = "This is body text.";
		final String EXPECTED_AUTHOR = "Peter Watts";
		final SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy:MM:DDD:HH");
		// calendar object to get time at creation
		Calendar creationTime;
		
		
		//input the values into the UI
		mTitle.requestFocus();
		mTitle.setText(EXPECTED_TITLE);
		mBody.requestFocus();
		mBody.setText(EXPECTED_BODY);
		mAuthor.requestFocus();
		mAuthor.setText(EXPECTED_AUTHOR);
		
		/*
		 *  In here we test funcitonality of pictures and location changing
		 *  ie, before we hit post.
		 */
		// get the time
		creationTime = Calendar.getInstance();
		final String EXPECTED_TIME = expectedDateFormat.format(creationTime).toString();
		/*
		 * I'm assuming that after you post the comment a View of only that comment is loaded.
		 * Like the OP on a forum.
		 */
		
		// Set up an ActivityMonitor
		ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(CommentView.class.getName(),
													null, false);
				        
		// post the comment
		mPost.performClick();
		
		// Validate that ReceiverActivity is started
		CommentView commentView = (CommentView)
								receiverActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		assertNotNull("ReceiverActivity is null", commentView);
		assertEquals("Monitor for ReceiverActivity has not been called",
				        1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
						CommentView.class, commentView.getClass());
		
		
		// we should now be in CommentView Activity
		EditText editAuthor = (EditText)commentView.findViewById
										(ca.ualberta.cs.team5geotopics.R.id.commentViewAuthorEditText);
		
		EditText editBody = (EditText)commentView.findViewById
										(ca.ualberta.cs.team5geotopics.R.id.commentViewBodyEditText);
				
		EditText editTitle = (EditText)commentView.findViewById
										(ca.ualberta.cs.team5geotopics.R.id.commentViewAuthorTitleText);
		
		EditText editTime =  (EditText)commentView.findViewById
										(ca.ualberta.cs.team5geotopics.R.id.commentViewAuthorTimeText);
		
		// assert that we got the views from the activity
		assertNotNull(editAuthor);
		assertNotNull(editBody);
		assertNotNull(editTitle);
		assertNotNull(editTime);
		
		/*
		 * add some ViewAsserts
		 * 
		 */
		
		/*
		 * add some assertions about location and picture being null.
		 * 
		 */
		
		
		
		
		// turn the text in the views into appropriate strings
		String author = editAuthor.getText().toString();
		String body = editBody.getText().toString();
		String title = editTitle.getText().toString();
		String time = editTime.getText().toString();
		
		// test to see if the strings are equal to the expected values
		assertTrue(author.equals(EXPECTED_AUTHOR));
		assertTrue(body.equals(EXPECTED_BODY));
		assertTrue(title.equals(EXPECTED_TITLE));
		assertTrue(time.equals(EXPECTED_TIME));
		
		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);
	}
}
