package ca.ualberta.cs.team5geotopics;

import android.graphics.Bitmap;
import android.location.Location;

public class ReplyLevelModel extends CommentModel {
	
	private CommentModel mParent;

	public ReplyLevelModel(Location mGeolocation, String mBody, String mTitle,
			String mAuthor, Bitmap mPicture, CommentModel mParent) {
		super(mGeolocation, mBody, mAuthor, mPicture);
		this.mParent = mParent;
		super.putTimeStamp();
	}

}
