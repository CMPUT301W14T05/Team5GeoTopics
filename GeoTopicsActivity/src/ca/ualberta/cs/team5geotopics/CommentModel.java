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

public class CommentModel extends AModel<AView> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Location mGeolocation;
	private String mBody;
	private String mAuthor;
	private String mTitle;
	private ArrayList<CommentModel> replies;
	private Bitmap mPicture;
	private Date mDate;
	private CommentModel mParent;
	private String mDMYFormatedDate;
	private String mHrSecFormatedDate;
	private boolean mTopLevel;

	
	// Constructor for Test Top Level Comments
		public CommentModel( String mBody, String mAuthor, String mTitle) {
			super();
			this.mGeolocation = null;
			this.mBody = mBody;
			this.mAuthor = mAuthor;
			this.mTitle = mTitle;
			this.mPicture = null;
			putTimeStamp();
			this.replies = new ArrayList<CommentModel>();
			this.mParent = null;
		}
	// Constructor for Top Level Comments
	public CommentModel(Location mGeolocation, String mBody, String mAuthor,
			Bitmap mPicture, String mTitle) {
		super();
		this.mGeolocation = mGeolocation;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = mTitle;
		this.mPicture = mPicture;
		putTimeStamp();
		this.replies = new ArrayList<CommentModel>();
		this.mParent = null;
		this.mTopLevel = true;
	}

	// Constructor for replies
	public CommentModel(Location mGeolocation, String mBody, String mAuthor,
			Bitmap mPicture, CommentModel mParent) {
		super();
		this.mGeolocation = mGeolocation;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = null;
		this.mPicture = mPicture;
		putTimeStamp();
		this.replies = new ArrayList<CommentModel>();
		this.mParent = mParent;
		this.mTopLevel = false;
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
	
	public boolean isTopLevel(){
		return mParent == null;
	}

	public CharSequence getmTitle() {
		//TODO: Return empty string if it is not a top level
		return this.mTitle;
	}

	public Date getDate() {
		return mDate;
	}

}
