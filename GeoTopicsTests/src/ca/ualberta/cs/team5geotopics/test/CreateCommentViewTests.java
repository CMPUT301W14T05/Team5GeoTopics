package ca.ualberta.cs.team5geotopics.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Comment;

import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
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
	 * 
	 * --> at the moment there is no location funcitonality. <--
	 */
	@UiThreadTest
	public void testCreateTopLevelCommentOnlyText(){
		// our input values
		final String EXPECTED_TITLE = "Test Top Level Comment";
		final String EXPECTED_BODY = "This is body text.";
		final String EXPECTED_AUTHOR = "Peter Watts";
		//year-month-dayInYear-hourInDay 
		//24hour range
		final SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy:MM:DDD:HH");
		// calendar object to get time at creation
		Calendar creationTime;
		// this is the return intent. This will have the Comment object contained in it.
		Intent returnIntent;
		
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
		
		// post the comment
		mPost.performClick();
		// get the time
		creationTime = Calendar.getInstance();
		
		//create Mock comment
		Comment cmt = new Comment(EXPECTED_TITLE, EXPECTED_BODY, EXPECTED_AUTHOR,
									null, null); // picture and location are both null at this point
		
		
		// test to see if comment is in out list
		GeoTopicsApplication application = (GeoTopicsApplication) mActivity.getApplication();
		CommentQueueController queueController = application.getQueueController();
		ArrayList<Comment> outList = queueController.getmOut();
		
		// assert that the outList only contains one comment
		assertTrue(outList.size() == 1);
		
		// assert that the outList has a comment with
		// the same state as the mock comment.
		assertTrue(outList.get(0).equals(cmt));
	}
}
