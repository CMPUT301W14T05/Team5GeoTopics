package ca.ualberta.cs.team5geotopics;


import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ualberta.cs.team5geotopics.AModel;
import ca.ualberta.cs.team5geotopics.AView;


public class CommentModel extends AModel<AView> implements Parcelable {
	// elastic search dependent variables
	private String mEsID;
	private String mParentID;
	private String mEsType;
	private String lat;
	private String lon;
	private long epochTime; 
	private String mBody;
	private String mAuthor;
	private String mTitle;
	private Bitmap mPicture;
	
	// elastic search comment constructor
	
	public CommentModel(String lat, String lon, String body, String author,
						String title, Bitmap image){
		super();
		this.lat = lat;
		this.lon = lon;
		this.epochTime = System.currentTimeMillis();
		this.mBody = body;
		this.mAuthor = author;
		this.mTitle = title;
		this.mPicture = image;
		this.mParentID = "-1";
		//this.mReplies = new ArrayList<CommentModel>();
		this.mDate = new Date(epochTime);
	}
	
	public void setES(String id, String parent, String type){
		this.mEsID = id;
		this.mParentID = parent;
		this.mEsType = type;
	}
	
	public boolean isTopLevel() {
		return mParentID == "-1";
	}
	
	private Date mDate;
	
	// Constructor for Top Level Comments
	public CommentModel(String lat, String lon, String mBody, String mAuthor,
			Bitmap mPicture, String mTitle) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = mTitle;
		this.mPicture = mPicture;
		putTimeStamp();
		this.mEsID = null;
		this.mParentID = "-1";
		//this.mReplies = new ArrayList<CommentModel>();

	}
	// Constructor for replies
	//Think this constructor is redundant
	public CommentModel(String lat, String lon, String mBody, String mAuthor,
			Bitmap mPicture) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.mBody = mBody;
		this.mAuthor = mAuthor;
		this.mTitle = null;
		this.mPicture = mPicture;
		putTimeStamp();
		this.mEsID = null;
		this.mParentID = null;
		//this.mReplies = new ArrayList<CommentModel>();
	}
	
	/*
	 * parcable stuff
	 */
	private void readFromParcel(Parcel in) {
		//this.mGeolocation = in.readParcelable(Location.class.getClassLoader());
		this.mPicture = in.readParcelable(Bitmap.class.getClassLoader());
		this.mTitle = in.readString();
		this.mAuthor = in.readString();
		this.mBody = in.readString();
		this.lat = in.readString();
		this.lon = in.readString();
		this.epochTime = in.readLong();
		this.mEsID = in.readString();
		this.mEsType = in.readString();
		this.mParentID = in.readString();
		//this.mReplies = new ArrayList<CommentModel>();
		//this.mParent = null;
		this.mDate = new Date(this.epochTime);
	}
	
	// parcable constructor
	public CommentModel(Parcel in) {  
	     readFromParcel(in);  
	}  
	
	@Override  
    public void writeToParcel(Parcel out, int flags) {  
        //out.writeParcelable(mGeolocation, flags);
		out.writeParcelable(mPicture, flags);
		out.writeString(mTitle);
		out.writeString(mAuthor);
		out.writeString(mBody);
		out.writeString(lat);
		out.writeString(lon);
		out.writeLong(epochTime);
		out.writeString(mEsID);
		out.writeString(mEsType);
		out.writeString(mParentID);
	}  
	
	public static final Parcelable.Creator<CommentModel> CREATOR = new Parcelable.Creator<CommentModel>() {  
	    
        public CommentModel createFromParcel(Parcel in) {  
            return new CommentModel(in);  
        }  
   
        public CommentModel[] newArray(int size) {  
            return new CommentModel[size];  
        }  
          
    }; 
    
    @Override  
    public int describeContents() {  
        return 0;  
    }  

    /*
     * end of parcable stuff
     */
	protected void putTimeStamp() {
		this.mDate = new Date(System.currentTimeMillis());
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

	public void setmGeolocation(Location geolocation) {
		this.lat = Double.toString(geolocation.getLatitude());
		this.lon = Double.toString(geolocation.getLongitude());
		this.notifyViews();
	}
	
	public void setmGeolocation(double lat, double lon) {
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
		this.notifyViews();
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
		this.notifyViews();
	}

	public Location getGeoLocation() {
		Location loc = new Location("loc");
		loc.setLatitude(Double.parseDouble(this.lat));
		loc.setLongitude(Double.parseDouble(this.lon));
		return loc;
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
	
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public long getEpochTime() {
		return epochTime;
	}

	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	public Date getmDate() {
		return mDate;
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	public Bitmap getmPicture() {
		return mPicture;
	}

}
