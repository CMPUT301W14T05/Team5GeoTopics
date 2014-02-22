package ca.ualberta.cs.team5geotopics;

import android.graphics.Bitmap;
import android.location.Location;

public class ReplyLevelModel extends CommentModel {

	public ReplyLevelModel(Location mGeolocation, String mBody, String mTitle,
			String mAuthor, Bitmap mPicture) {
		super(mGeolocation, mBody, mTitle, mAuthor, mPicture);
		super.putTimeStamp();
	}

}
