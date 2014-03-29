package ca.ualberta.cs.team5geotopics;


import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ualberta.cs.team5geotopics.AModel;
import ca.ualberta.cs.team5geotopics.AView;

/**
 * CommentModel is the actual model for each and every comment.
 * This holdsall the data about a single comment for references later in the use
 * of the program.
 */

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
	private int mSortWeight;
	private String authorID;
	
	/**
	 * Elastic search comment constructor
	 * @param lat The latitude of the comment.
	 * @param lon The longitude of the comment.
	 * @param body The body of the comment.
	 * @param author The author of the comment.
	 * @param title The title of the comment.
	 * @param image The image of the comment.
	 */
	public CommentModel(String lat, String lon, String body, String author,
						String title, Bitmap image, String authorID){
		super();
		this.lat = lat;
		this.lon = lon;
		this.epochTime = System.currentTimeMillis();
		this.mBody = body;
		this.mAuthor = author;
		this.mTitle = title;
		this.mPicture = image;
		this.mParentID = "-1";
		this.mDate = new Date(epochTime);
	}
	
	/**
	 * Sets the ElasticSearch ID for the comment. This string is used for storing
	 * and retrieving the comment from elasisearch. It is also useful to determine
	 * if two comment objects are the same.
	 * @param id The id of the comment.
	 * @param parent The parent of the comment.
	 * @param type The type of comment.
	 */
	public void setES(String id, String parent, String type){
		this.mEsID = id;
		this.mParentID = parent;
		this.mEsType = type;
	}

	/**
	 * Checks to see if the comment is top level or not.
	 * @return mEsType.equals("TopLevel") Boolean if the comment is top level or not.
	 */
	public boolean isTopLevel() {
		return mEsType.equals("TopLevel");
	}
	
	private Date mDate;
	
	/**
	 * Constructor for Top Level Comments
	 * @param lat The latitude of the comment.
	 * @param lon The longitude of the comment.
	 * @param mBody The body of the comment.
	 * @param mAuthor The author of the comment.
	 * @param mPicture The picture of the comment.
	 * @param mTitle The title of the comment.
	 */
	public CommentModel(String lat, String lon, String mBody, String mAuthor,
			Bitmap mPicture, String mTitle, String authorID) {
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
		this.mEsType = "TopLevel";
	}
	
	 /**
	  * Constructor for replies
	  * @param lat The latitude of the reply.
	  * @param lon The longitude of the reply.
	  * @param mBody The body of the reply.
	  * @param mAuthor The author of the reply.
	  * @param mPicture The picture of the reply.
	  */
	public CommentModel(String lat, String lon, String mBody, String mAuthor,
			Bitmap mPicture, String authorID) {
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
		this.mEsType = "ReplyLevel";
	}
	
	/**
	 * Parcable stuff
	 * @param in The parceable data.
	 */
	 
	private void readFromParcel(Parcel in) {
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
		this.mDate = new Date(this.epochTime);
	}
	
	/**
	 * Parcable constructor
	 * @param in The Parcel data to be read from.
	 */
	public CommentModel(Parcel in) {  
	     readFromParcel(in);  
	}  
	
	
	/**
	 * This writes to a parcel.
	 * @param out The Parcel data to be written to.
	 * @param flags The flags for the written data.
	 */
	@Override 
    public void writeToParcel(Parcel out, int flags) {  
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
	    
		/**
		 * Creates a new CommentModel from a Parcel
		 * @param in The Parcel to be created from.
		 */
        public CommentModel createFromParcel(Parcel in) {  
            return new CommentModel(in);  
        }  
   
        /**
         * Creates a new array of CommentModels
         * @param size The size of the new array.
         */
        public CommentModel[] newArray(int size) {  
            return new CommentModel[size];  
        }  
          
    }; 
    
    /**
     * Describes the contents as being 0.
     * @return 0 The number of contents.
     */
    @Override  
    public int describeContents() {  
        return 0;  
    }  

    /**
     * Puts the current system time into the date field for the comment. Used 
     * largely for new comments who need a post date associated with it.
     */
	protected void putTimeStamp() {
		this.mDate = new Date(System.currentTimeMillis());
	}

	/**
	 * This function gets the elastisearch ID of the comment. Useful for 
	 * determining if two comment objects are the same or for elastisearch operations.
	 * @return mEsID The ID of the ElasticSearch 
	 */
	public String getmEsID() {
		return mEsID;
	}

	/**
	 * Sets the elastisearch ID of the comment.
	 * @param mEsID The ElasticSearch ID for the comment.
	 */
	public void setmEsID(String mEsID) {
		this.mEsID = mEsID;
	}
	
	/**
	 * Gets the comments parent ID. Since comments do not keep a list of their 
	 * children this field allows you to find the children of a comment by searching
	 * for all comments who's mParentID field == parent EsID.
	 * @return mParentID The ID of the comment's parent.
	 */
	public String getmParentID() {
		return mParentID;
	}

	/**
	 * Sets the parent's ID to that of another parent ID. Used to link
	 * a comment to a parent.
	 * @param mParentID The ID of the comment's parent.
	 */
	public void setmParentID(String mParentID) {
		this.mParentID = mParentID;
	}

	/**
	 * Gets the ElasticSearch type of comment. Will return either "TopLevel" 
	 * or "ReplyLevel".
	 * @return mEsType The type of comment from ElasticSearch.
	 */
	public String getmEsType() {
		return mEsType;
	}

	/**
	 * Sets the type of comment. Anything other than "TopLevel" or "ReplyLevel" will 
	 * have un defined application behaviour. This function does not sanitise input.
	 * @param mEsType The type of comment.
	 */
	public void setmEsType(String mEsType) {
		this.mEsType = mEsType;
		//this.notifyViews();
	}
	
	/**
	 * Gets the body from the comment.
	 * @return mBody The body of the comment.
	 */
	public String getmBody() {
		return mBody;
	}

	/**
	 * Sets the body for the comment.
	 * @param mBody The body for the comment.
	 */
	public void setmBody(String mBody) {
		this.mBody = mBody;
		//this.notifyViews();
	}

	/**
	 * Gets the author from the comment.
	 * @return mAuthor The author of the comment.
	 */
	public String getmAuthor() {
		return mAuthor;
	}
	
	/**
	 * Returns the unique identifier that identifies the authors profile. User to
	 * retrieve the authors profile from online.
	 * @return The authors profile id
	 */
	public String getAuthorID(){
		return this.authorID;
	}

	/**
	 * Sets the author of the comment.
	 * @param mAuthor The author of the comment.
	 */
	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
		//this.notifyViews();
	}

	/**
	 * Sets the picture of the comment.
	 * @param mPicture The Bitmap picture to be set.
	 */
	public void setmPicture(Bitmap mPicture) {
		this.mPicture = mPicture;
		//this.notifyViews();
	}

	/**
	 * Sets the geolocation for the comment. Used mostly when 
	 * making a new comment however can be used to re-define the location
	 * for a comment.
	 * @param geolocation The geolocation to be set.
	 */
	public void setmGeolocation(Location geolocation) {
		this.lat = Double.toString(geolocation.getLatitude());
		this.lon = Double.toString(geolocation.getLongitude());
		//this.notifyViews();
	}
	
	/**
	 * Sets the geolocation for the comment given longitude and latitude. Allows 
	 * one to set the location of a comment view lat and long directly.
	 * @param lat The latitude for the comment.
	 * @param lon The longitude for the comment.
	 */
	public void setmGeolocation(double lat, double lon) {
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
		//this.notifyViews();
	}

	/**
	 * Sets the title of the comment.
	 * @param mTitle The title to be set for the comment.
	 */
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
		//this.notifyViews();
	}

	/**
	 * Gets the geolocation of the comment.
	 * @return loc The lcoation of the comment.
	 */
	public Location getGeoLocation() {
		Location loc = new Location("loc");
		loc.setLatitude(Double.parseDouble(this.lat));
		loc.setLongitude(Double.parseDouble(this.lon));
		return loc;
	}

	/**
	 * Gets the title of the comment.
	 * @return this.mTitle The title of the comment.
	 */
	public CharSequence getmTitle() {
		return this.mTitle;
	}

	/**
	 * Gets the date of the comment.
	 * @return mDate The date of the comment.
	 */
	public Date getDate() {
		return mDate;
	}

	/**
	 * Gets the picture of the comment.
	 * @return mPicture The picture of the comment.
	 */
	public Bitmap getPicture() {
		return mPicture;
	}

	/**
	 * Does the comment have a picture?
	 * @return Boolean True if the comment has a picture.
	 */
	public boolean hasPicture() {
		return mPicture != null;
	}

	/**
	 * Does the comment have a title?
	 * @return Boolean True if the comment has a title.
	 */
	public boolean hasTitle() {
		return mTitle != null;
	}
	
	/**
	 * Gets the latitude of the comment.
	 * @return lat The latitude of the comment.
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * Sets the latitude of the comment.
	 * @param lat The latitude of the comment.
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * Gets the longitude of the comment.
	 * @return lon The longitude of the comment.
	 */
	public String getLon() {
		return lon;
	}

	/**
	 * Sets the longitude of the comment
	 * @param lon The longitude of the comment.
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * Gets the epoch time of the comment. Useful mostly
	 * for comment sorting and such where a raw number is 
	 * more useful than a formatted date.
	 * @return epochTime The epoch time of the comment.
	 */
	public long getEpochTime() {
		return epochTime;
	}

	/**
	 * Sets the epoch time for the comment.
	 * @param epochTime The epoch time for the comment.
	 */
	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	/**
	 * Gets the date of the comment.
	 * @return mDate The date of the comment.
	 */
	public Date getmDate() {
		return mDate;
	}

	
	/**
	 * Sets the date for the comment.
	 * @param mDate The comment date (new).
	 */
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	/**
	 * Gets the picture from the comment.
	 * @return mPicture The picture of the comment.
	 */
	public Bitmap getmPicture() {
		return mPicture;
	}

	/**
	 * Gets the sort weight of the comment.
	 * @return mSortWeight The sort weight of the comment.
	 */
	public int getSortWeight() {
		return mSortWeight;
	}

	/**
	 * Sets the sort weight for the comment.
	 * @param mSortWeight The sort weight for the comment.
	 */
	public void setSortWeight(int mSortWeight) {
		this.mSortWeight = mSortWeight;
	}

}
