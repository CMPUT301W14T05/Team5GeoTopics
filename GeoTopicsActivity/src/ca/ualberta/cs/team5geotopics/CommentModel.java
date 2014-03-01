package ca.ualberta.cs.team5geotopics;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;

/*
 * This is the base class for the TopLevelModel 
 * and ReplyLevelModel.
 */
//TODO: implement parcable or sometype of JSon serialization.
public class CommentModel extends AModel<AView> implements Serializable{
	private Location mGeolocation;
	private String mBody;
	private String mAuthor;
	private ArrayList<ReplyLevelModel> replies;
	//private Bitmap mPicture;
	private Date mDate;
	private String mDMYFormatedDate;
	private String mHrSecFormatedDate;
	
	public CommentModel(Location mGeolocation, String mBody,
			String mAuthor, Bitmap mPicture) {
		super();
		this.mGeolocation = mGeolocation;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		//this.mPicture = mPicture;
		putTimeStamp();
		this.replies = new ArrayList<ReplyLevelModel>();
		
	}

	protected void putTimeStamp() {
		this.mDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dmy = new SimpleDateFormat("dd/MM/yyy");
		SimpleDateFormat hrSec = new SimpleDateFormat("hh:mm a");
		
		this.mDMYFormatedDate = dmy.format(mDate);
		this.mHrSecFormatedDate = hrSec.format(mDate);
		
	}

	public String getmBody() {
		return mBody;
	}

	public void setmBody(String mBody) {
		this.mBody = mBody;
	}

	public String getmAuthor() {
		return mAuthor;
	}

	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}
	
	public Location getGeoLocation() {
		return mGeolocation;
	}
	
	public Date getDate() {
		return mDate;
	}
	
	
	
}
