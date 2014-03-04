package ca.ualberta.cs.team5geotopics.test;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;
import junit.framework.TestCase;

public class EditTLComment extends ActivityInstrumentationTestCase2<InspectCommentActivity> {

	public EditTLComment() {
		super(InspectCommentActivity.class);	
	}
	
	public void testEditTLComment(){
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
		
		CommentModel topLevel = new CommentModel(mGeolocation, mBody, mAuthor, mPicture, mTitle);
		
		// New data for comment
		mBody = "BODY_MODIFIED";
		mAuthor = "AUTHOR_MODIFIED";
		mTitle = "TITLE_MODIFIED";
		
		mGeolocation.setLatitude(35);
		mGeolocation.setLongitude(65);
		
		mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_4444);
		
		topLevel.setmAuthor(mAuthor);
		topLevel.setmBody(mBody);
		topLevel.setmTitle(mTitle);
		topLevel.setmGeolocation(mGeolocation);
		topLevel.setmPicture(mPicture);
		
		assertTrue("Edit Comment Body is correct", topLevel.getmBody() == "BODY");
		assertTrue("Edit Comment Author is correct", topLevel.getmAuthor() == "AUTHOR");
		assertTrue("Edit Comment Title is correct", topLevel.getmTitle() == "TITLE");
		assertTrue("Edit Comment Picture is correct", topLevel.getPicture() == mPicture);
		assertTrue("Edit Comment Geolocation is correct", topLevel.getGeoLocation() == mGeolocation);
		}
	}