package ca.ualberta.cs.team5geotopics;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;

/*
 * This is the base class for the TopLevelModel 
 * and ReplyLevelModel.
 */
public class CommentModel extends AModel<AView> {
	private Location mGeolocation;
	private String mBody;
	private String mTitle;
	private String mAuthor;
	private Bitmap mPicture;
	private Date mDate;
	private String mDMYFormatedDate;
	private String mHrSecFormatedDate;
	
	public CommentModel(Location mGeolocation, String mBody, String mTitle,
			String mAuthor, Bitmap mPicture) {
		super();
		this.mGeolocation = mGeolocation;
		this.mBody = mBody;
		this.mTitle = mTitle;
		this.mAuthor = mAuthor;
		this.mPicture = mPicture;
		putTimeStamp();
		
	}

	protected void putTimeStamp() {
		this.mDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dmy = new SimpleDateFormat("dd/MM/yyy");
		SimpleDateFormat hrSec = new SimpleDateFormat("hh:mm a");
		
		this.mDMYFormatedDate = dmy.format(mDate);
		this.mHrSecFormatedDate = hrSec.format(mDate);
		
	}
	
	
}
