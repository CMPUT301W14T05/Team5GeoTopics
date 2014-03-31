package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.CommentController;
import ca.ualberta.cs.team5geotopics.CommentManager;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.CommentPush;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;

public class EsTestsA extends ActivityInstrumentationTestCase2<InspectCommentActivity> {
	private Activity mActivity;
	public EsTestsA() {
		super(InspectCommentActivity.class);
		
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	public void testPushTopLevel(){
		String mBody = "BODY";
		String mAuthor = "AUTHOR";
		String mTitle = "TITLE";
		Bitmap mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);
		CommentManager manager = CommentManager.getInstance();
		
		CommentModel topLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mTitle, mPicture, null);
		topLevel.setES("test id", "-1", "test type");
		CommentPush cp = new CommentPush();
		
		Thread thread = cp.pushComment(topLevel, "TopLevel");
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPush", "Thread interrupt");
		}
		JestResult result = cp.returnResult();
		assertTrue("JestResult is not null", result != null);
		Log.w("EsTestPush", result.getJsonString());
		assertTrue("JestResult suceeded", cp.returnResult().isSucceeded());
	}
	
	public void testPushReplyLevel(){
		String mBody = "BODY";
		String mAuthor = "AUTHOR";
		String mTitle = "TITLE";
		Bitmap mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);
		CommentManager manager = CommentManager.getInstance();
		
		CommentModel replyLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mTitle, mPicture, null);
		replyLevel.setES("test reply to test id", "test id", "test id");
		CommentPush cp = new CommentPush();
		
		Thread thread = cp.pushComment(replyLevel, "ReplyLevel");
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPush", "Thread interrupt");
		}
		JestResult result = cp.returnResult();
		assertTrue("JestResult is not null", result != null);
		Log.w("EsTestPush", result.getJsonString());
		assertTrue("JestResult suceeded", cp.returnResult().isSucceeded());
	}
}
