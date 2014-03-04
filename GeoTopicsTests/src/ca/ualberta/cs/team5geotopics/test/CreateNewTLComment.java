package ca.ualberta.cs.team5geotopics.test;

import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import junit.framework.TestCase;

public class CreateNewTLComment extends ActivityInstrumentationTestCase2<InspectCommentActivity> {

	public CreateNewTLComment() {
		super(InspectCommentActivity.class);
	}
	
	@SuppressWarnings("null")
	@SuppressLint("NewApi")
	public void testCreateNewComment(){
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
	
	assertTrue("Comment Body is correct", topLevel.getmBody() == "BODY");
	assertTrue("Comment Author is correct", topLevel.getmAuthor() == "AUTHOR");
	assertTrue("Comment Title is correct", topLevel.getmTitle() == "TITLE");
	assertTrue("Comment Picture is correct", topLevel.getPicture() == mPicture);
	assertTrue("Comment Geolocation is correct", topLevel.getGeoLocation() == mGeolocation);
	}
}
