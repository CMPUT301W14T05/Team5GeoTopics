package ca.ualberta.cs.team5geotopics.test;

import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;
import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

public class Sorting extends ActivityInstrumentationTestCase2<InspectCommentActivity> {

	public Sorting() {
		super(InspectCommentActivity.class);
	}
	
	@SuppressWarnings("null")
	public void testSortCommentsByPicture(){
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
		
		CommentModel withPicture = new CommentModel(mGeolocation, mBody, mAuthor, mPicture, mTitle);
		
		CommentModel withoutPicture = new CommentModel(mGeolocation, mBody, mAuthor, null, mTitle);
		
		CommentListModel clm = new CommentListModel();
		
		clm.add(withoutPicture);
		clm.add(withPicture);
		
		clm.sortCommentsByPicture(null);
		
		assertTrue("First comment has a picture", clm.getList().get(0).hasPicture());
		
	}
	
}
