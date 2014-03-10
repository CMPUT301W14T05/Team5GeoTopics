package ca.ualberta.cs.team5geotopics.test;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;
import junit.framework.TestCase;

public class AddPictureToComment extends ActivityInstrumentationTestCase2<InspectCommentActivity> {

	public AddPictureToComment() {
		super(InspectCommentActivity.class);
	}

	public void testAddPictureToComment(){
		String mBody = "Body"; 
		String mAuthor = "James";
		Bitmap mPicture = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);;
		String mTitle = "Title";

		CommentModel comment = new CommentModel("30.6282", "55.3116", mBody, mAuthor, mPicture, mTitle);
		
		assertTrue("Picture is inside the model", comment.getmPicture() == mPicture);
	}
			
}
