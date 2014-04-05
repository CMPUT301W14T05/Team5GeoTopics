package ca.ualberta.cs.team5geotopics.test;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;


public class EditCommentTest extends ActivityInstrumentationTestCase2<InspectCommentActivity> {

	public EditCommentTest() {
		super(InspectCommentActivity.class);	
	}
	
	public void testEditComment(){
		// Variables for the new Top Level Comment
		Location mGeolocation = new Location("");
		String mBody = null; 
		String mAuthor = null;
		Bitmap mPicture = null;
		String mTitle = null;
		
		mBody = "BODY";
		mAuthor = "AUTHOR";
		mTitle = "TITLE";
		
		mGeolocation.setLatitude(30.6282);
		mGeolocation.setLongitude(55.3116);
		
		mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);
		
		CommentModel comment = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mPicture, mTitle);
		
		// New data for comment
		mBody = "BODY_MODIFIED";
		mAuthor = "AUTHOR_MODIFIED";
		mTitle = "TITLE_MODIFIED";
		
		mGeolocation.setLatitude(35);
		mGeolocation.setLongitude(65);
		
		mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_4444);
		
		comment.setmAuthor(mAuthor);
		comment.setmBody(mBody);
		comment.setmTitle(mTitle);
		comment.setmGeolocation(mGeolocation);
		comment.setmPicture(mPicture);
		
		assertTrue("Edit Comment Body is correct", comment.getmBody() == "BODY_MODIFIED");
		assertTrue("Edit Comment Author is correct", comment.getmAuthor() == "AUTHOR_MODIFIED");
		assertTrue("Edit Comment Title is correct", comment.getmTitle() == "TITLE_MODIFIED");
		assertTrue("Edit Comment Picture is correct", comment.getPicture() == mPicture);
		assertEquals("Comment latitude is correct", 35.0 , comment.getGeoLocation().getLatitude());
		assertEquals("Comment Longitude is correct", 65.0, comment.getGeoLocation().getLongitude());
		}
	}