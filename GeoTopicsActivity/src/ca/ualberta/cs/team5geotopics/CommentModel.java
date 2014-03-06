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

public class CommentModel extends AModel<AView> implements Serializable {
	// elastic search id variables
	private String mEsID;
	private String mParentID;
	private String mEsType;
	
	private static final long serialVersionUID = 1L;
	private Location mGeolocation;
	private String mBody;
	private String mAuthor;
	private String mTitle;
	private Bitmap mPicture;
	private Date mDate;
	private String mDMYFormatedDate;
	private String mHrSecFormatedDate;
	/*
	 * We still need these as Josh said we still need to reference comments this
	 * way when they are hot (In the application)
	 */
	private ArrayList<CommentModel> mReplies;
	private CommentModel mParent;

	// Test Constructor for Top Level Comments

	public CommentModel(String mBody, String mAuthor, String mTitle) {

		super();
		this.mGeolocation = null;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = mTitle;
		this.mPicture = null;
		putTimeStamp();
		this.mEsID = null;
		this.mParentID = null;
		this.mReplies = new ArrayList<CommentModel>();

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
		this.mEsID = null;
		this.mParentID = null;
		this.mReplies = new ArrayList<CommentModel>();

	}

	// Test Constructor for replies
	public CommentModel(String mBody, String mAuthor) {
		super();
		this.mGeolocation = null;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = null;
		this.mPicture = mPicture;
		putTimeStamp();
		this.mEsID = null;
		this.mParentID = null;
		this.mParent = null;
		this.mReplies = new ArrayList<CommentModel>();
	}

	// Constructor for replies
	public CommentModel(Location mGeolocation, String mBody, String mAuthor,
			Bitmap mPicture) {
		super();
		this.mGeolocation = mGeolocation;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = null;
		this.mPicture = mPicture;
		putTimeStamp();
		this.mEsID = null;
		this.mParentID = null;
		this.mParent = null;
		this.mReplies = new ArrayList<CommentModel>();
	}

	protected void putTimeStamp() {
		this.mDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dmy = new SimpleDateFormat("dd/MM/yyy");
		SimpleDateFormat hrSec = new SimpleDateFormat("hh:mm a");

		this.mDMYFormatedDate = dmy.format(mDate);
		this.mHrSecFormatedDate = hrSec.format(mDate);

	}

	public String getmEsID() {
		return mEsID;
	}

	public void setmEsID(String mEsID) {
		this.mEsID = mEsID;
	}

	public String getmParentID() {
		return mParentID;
	}

	public void setmParentID(String mParentID) {
		this.mParentID = mParentID;
	}

	public String getmEsType() {
		return mEsType;
	}

	public void setmEsType(String mEsType) {
		this.mEsType = mEsType;
		this.notifyViews();
	}
	public String getmBody() {
		return mBody;
	}

	public void setmBody(String mBody) {
		this.mBody = mBody;
		this.notifyViews();
	}

	public String getmAuthor() {
		return mAuthor;
	}

	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
		this.notifyViews();
	}

	public void setmPicture(Bitmap mPicture) {
		this.mPicture = mPicture;
		this.notifyViews();
	}

	public void setmGeolocation(Location mGeolocation) {
		this.mGeolocation = mGeolocation;
		this.notifyViews();
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
		this.notifyViews();
	}

	public Location getGeoLocation() {
		return mGeolocation;
	}

	public boolean isTopLevel() {
		return this.mParentID == null;
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

	public boolean hasTitle() {
		return mTitle != null;
	}

	public void addReply(CommentModel comment) {
		comment.setParent(this);
		mReplies.add(comment);
	}

	public void setParent(CommentModel comment) {
		this.mParent = comment;
	}
	
	public ArrayList<CommentModel> getReplies() {
		return this.mReplies;
	}

	
}
