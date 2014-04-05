package ca.ualberta.cs.team5geotopics.test;


import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;

public class CreateNewTLComment extends ActivityInstrumentationTestCase2<InspectCommentActivity> {
	
	
	
	public CreateNewTLComment() {
		super(InspectCommentActivity.class);
	}
	
	
	

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
	
	
	CommentModel topLevel = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mTitle, mPicture, "testId");
	assertTrue("Comment Body is correct", topLevel.getmBody().equals(mBody));
	assertTrue("Comment Author is correct", topLevel.getmAuthor().equals(mAuthor));
	assertTrue("Comment Title is correct", topLevel.getmTitle().equals(mTitle));
	assertTrue("Comment Picture is correct", topLevel.getPicture().equals(mPicture));
	assertTrue("Comment latitude is correct", topLevel.getLat().equals("30.6282"));
	assertTrue("Comment Longitude is correct", topLevel.getLon().equals("55.3116"));
	
	}
}
