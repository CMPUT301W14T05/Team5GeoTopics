package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.CommentController;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;

public class EsTestPush extends ActivityInstrumentationTestCase2<InspectCommentActivity> {
	private Activity mActivity;
	public EsTestPush() {
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
		
		CommentModel topLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mTitle, mPicture);
		topLevel.setES("test id", "-1", "test type");
		CommentController cc = new CommentController(mActivity.getApplicationContext());
		
		Thread thread = cc.pushComment(topLevel, "TopLevel");
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPush", "Thread interrupt");
		}
		JestResult result = cc.returnResult();
		assertTrue("JestResult is not null", result != null);
		Log.w("EsTestPush", result.getJsonString());
		assertTrue("JestResult suceeded", cc.returnResult().isSucceeded());
	}
	
	public void testPushReplyLevel(){
		String mBody = "BODY";
		String mAuthor = "AUTHOR";
		String mTitle = "TITLE";
		Bitmap mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);
		
		CommentModel topLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mTitle, mPicture);
		topLevel.setES("test reply to test id", "test id", "test id");
		CommentController cc = new CommentController(mActivity.getApplicationContext());
		
		Thread thread = cc.pushComment(topLevel, "ReplyLevel");
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPush", "Thread interrupt");
		}
		JestResult result = cc.returnResult();
		assertTrue("JestResult is not null", result != null);
		Log.w("EsTestPush", result.getJsonString());
		assertTrue("JestResult suceeded", cc.returnResult().isSucceeded());
	}
}
