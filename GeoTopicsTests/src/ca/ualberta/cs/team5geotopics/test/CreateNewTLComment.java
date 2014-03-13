package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentController;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;

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
	
	CommentModel topLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mPicture, mTitle);
	
	assertTrue("Comment Body is correct", topLevel.getmBody() == "BODY");
	assertTrue("Comment Author is correct", topLevel.getmAuthor() == "AUTHOR");
	assertTrue("Comment Title is correct", topLevel.getmTitle() == "TITLE");
	assertTrue("Comment Picture is correct", topLevel.getPicture() == mPicture);
	assertTrue("Comment latitude is correct", topLevel.getGeoLocation().getLatitude() == 30.6282);
	assertTrue("Comment Longitude is correct", topLevel.getGeoLocation().getLongitude() == 55.3116);
	
	}
}
