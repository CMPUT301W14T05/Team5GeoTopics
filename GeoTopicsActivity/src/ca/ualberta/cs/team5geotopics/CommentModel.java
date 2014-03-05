package ca.ualberta.cs.team5geotopics;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;
import ca.ualberta.cs.team5geotopics.AModel;
import ca.ualberta.cs.team5geotopics.AView;

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
	private String esID;
	private Bitmap mPicture;
	private Date mDate;
	private CommentModel mParent;
	private String mDMYFormatedDate;
	private String mHrSecFormatedDate;

	
	// Constructor for Test Top Level Comments
		public CommentModel( String mBody, String mAuthor, String mTitle, ArrayList<CommentModel> replies) {
			super();
			this.mGeolocation = null;
			this.mBody = mBody;
			this.mAuthor = mAuthor;
			this.mTitle = mTitle;
			this.mPicture = null;
			putTimeStamp();
			this.replies = replies;
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
	
	public void setmPicture(Bitmap mPicture) {
		this.mPicture = mPicture;
	}
	
	public void setmGeolocation(Location mGeolocation) {
		this.mGeolocation = mGeolocation;
	}
	
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public Location getGeoLocation() {
		return mGeolocation;
	}
	
	public boolean isTopLevel(){
		return mParent == null;
	}

	public CharSequence getmTitle() {
		return this.mTitle;
	}

	public Date getDate() {
		return mDate;
	}
	
	public Bitmap getPicture() {
		return mPicture;
	}
	
	public boolean hasPicture() {
		return mPicture != null;
	}

	public ArrayList<CommentModel> getReplies() {
		return replies;
	}
	
	public void addReply(CommentModel reply) {
		replies.add(reply);
	}
	
	public boolean hasTitle() {
		return mTitle != null;
	}
}
