package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// code adapted from http://android-developers.blogspot.ca/2011/03/identifying-app-installations.html

/**
 * Holds all the data that the individual user whom is using the application creates/modifies.
 * This includes top-level comments and replies and makes the view availible when the user
 * goes to "My Comments" from the start screen.
 * 
 */

public class User extends AModel<AView> {
	transient private static final String INSTALLATION_ID = "INSTALLATION";
	transient private static final String POST_COUNT = "POSTCOUNT";
	transient private static final String USER = "user.save";
	transient private File mInstallation;
	transient private File mPostCount;
	transient private static User myself;
	transient private GeoTopicsApplication application;
	transient private boolean ioDisabled = false;
	transient private LocationManager lm;
	transient private String provider;
	transient private boolean needUpdate = false;
	transient private BroadcastReceiver webConnectionReceiver;
	//User Profile Saved Variables
	private String mID;
	private ArrayList<String> mBookMarks;
	private ArrayList<String> mFavourites;
	private ArrayList<String> mComments; 
	private String userName;
	private String biography;
	private String contactInfo;
	private Bitmap profilePic;
	private Double myLat;
	private Double myLong;

	private User() {
		this.application = GeoTopicsApplication.getInstance();
		mBookMarks = new ArrayList<String>();
		mFavourites = new ArrayList<String>();
		mComments = new ArrayList<String>(); 
		this.mID = 	Secure.getString(application.getContext().getContentResolver(),
	               Secure.ANDROID_ID);
		setUserName("Anonymous");
		mInstallation = new File(application.getContext().getFilesDir(),
				INSTALLATION_ID);
		mPostCount = new File(application.getContext().getFilesDir(),
				POST_COUNT);
		setUpLocationServices();
		setInitialLocation();
		
		webConnectionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (application.isNetworkAvailable() && needUpdate == true) {
					Log.w("Connectivity", "Pushing profile update");
					syncUser();
					needUpdate = false;
				}
			}
		};
		application.getContext().registerReceiver(webConnectionReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	/**
	 * Returns a single instance of the User. This is a lazy implementation
	 * and will only create the single instance (if it does not exist) upon
	 * request.
	 * 
	 * @return myself The instance of the current user
	 */
	public static User getInstance() {
		if (myself == null) {
			loadUser();
		}
		return myself;
	}
	
	/**
	 * Initialises the user with a test setup. Use and edit this function if the 
	 * regular implementation of user has a dependency you need to change in order
	 * for proper testing. 
	 * 
	 */
	public void testSetup() {
		this.mComments = new ArrayList<String>();
		ioDisabled = true;
	};
	/**
	 * Returns the profile ID associated with this profile
	 * @return The unique profile ID
	 */
	public String getProfileID(){
		Log.w("MID", "Profile ID: " + this.mID);
		return this.mID;
	}

	/**
	 * This empties the local hot list of authored comments. This will not clear 
	 * any comments written to disk it will only clear the hot versions that have
	 * been put into the list.
	 * 
	 */
	public void clearLocalMyComments() {
		mComments.clear();
	}

	/**
	 * Messages to read and write dependent files
	 * @return id The ID of the file containing comment
	 */
	public String readInstallIDFile() {
		byte[] bytes = null;
		String id = null;
		if (mInstallation.exists()) {
			try {
				RandomAccessFile f = new RandomAccessFile(mInstallation, "r");
				bytes = new byte[(int) f.length()];
				f.readFully(bytes);
				f.close();
				id = new String(bytes);
				Log.w("User-readInstallFile()", "UUID = " + id);
			} catch (Exception e) {
				Log.w("User-readInstallFile()",
						"error reading install file for User.");
			}
		}
		return id;
	}


	/**
	 * Writes the users install files back to disk. This includes the users
	 * unique ID and their post count. This info needs to be stored as it is
	 * used to generate unique post ID's for any new comments the user makes.
	 * Their Post's ID's is a combination of their ID and their post count.
	 */
	
	public void writeInstallFiles() {
		try {
			FileOutputStream out = new FileOutputStream(mInstallation);
			String id = UUID.randomUUID().toString();
			out.write(id.getBytes());
			out.close();
			Log.w("User-writeInstallFiles()", "UUID = " + id);
		} catch (Exception e) {
			Log.w("User-writeInstallFiles()",
					"error writing install file for User.");
		}

		try {
			FileOutputStream out = new FileOutputStream(mPostCount);
			String count = Integer.valueOf(0).toString();
			out.write(count.getBytes());
			out.close();
			Log.w("User-writeInstallFiles()", "count = " + count);
		} catch (Exception e) {
			Log.w("User-writeInstallFiles()",
					"error writing postCount file for User.");
		}
	}

	/**
	 * Reads the users post count off disk. This number is used to track 
	 * how many comments the user has posted. Its most common use is to 
	 * be used in combination with the user ID to create unique ID's for
	 * a users posts.
	 * 
	 * @return postCount The post count of the user
	 */
	public String readPostCount() {
		byte[] bytes = null;
		String postCount = null;
		try {
			RandomAccessFile f = new RandomAccessFile(mPostCount, "r");
			bytes = new byte[(int) f.length()];
			f.readFully(bytes);
			f.close();
			postCount = new String(bytes);
			Log.w("User-readPostCount()", "postCount = " + postCount);
		} catch (Exception e) {
			Log.w("User-readPostCount()",
					"error reading postCount file for User.");
		}
		return postCount;
	}
	
	/**
	 * Write only the users post count back to disk.
	 */
	public void writePostCountFile(int count) {
		try {
			FileOutputStream out = new FileOutputStream(mPostCount);
			String outString = Integer.valueOf(count).toString();
			out.write(outString.getBytes());
			out.close();
			Log.w("User-writePostCountFile()", "outString = " + outString);
		} catch (Exception e) {
			Log.w("User-writePostCountFile()",
					"error writing postCount file for User.");
			e.printStackTrace();
		}
	}

	public void updatePostCountFile() {
		int postCount = 0;
		try {
			postCount = Integer.parseInt(this.readPostCount());
			postCount++;
			this.writePostCountFile(postCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  /**
   * Checks if local install files exist. This does a check to see
   * if the files which sound contain the info exist in the applications
   * file system. It does not check that the contents have anything useful 
   * in them.
   * 
   * @return boolean If the installation and post count exist
   */
	public boolean installFilesExist() {
		return (mInstallation.exists()) && (mPostCount.exists());
	}

	/**
	   * Adds a comment to the local hot list of User comments. Will notify
	   * any views watching the user class and write the list out to disk.
	   * 
	   * @param comment The comment to be added to the local list.
	   */
	public void addToMyComments(CommentModel comment) {
		String ID = generateIDString(comment);
		Log.w("MyComments", ID);
		mComments.add(ID);
		//this.notifyViews();
		this.writeUser();
		Log.w("User", "7");
	}

	/**
	 * Used to get a copy of the array of my comments. Used mostly to 
	 * display the list in a view.
	 * 
	 * @return this.mComments The ArrayList<CommentModel> of comments.
	 */
	public ArrayList<String> getMyComments() {
		return this.mComments;
	}
	
	/**
	 * Loads the user from memory if it exists. Else it attempts to pull
	 * it from elastisearch. If that fails it makes a default
	 * user. The assumption is that if the user's profile does not exist locally
	 * or online that it is either lost for good or the user is new.
	 */
	private static void loadUser(){
		
		User temp;
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		gson = builder.create();

		FileInputStream fis;
		try {
			fis = GeoTopicsApplication.getInstance().getContext().openFileInput(USER);
			InputStreamReader isr = new InputStreamReader(fis);
			Type type = new TypeToken<User>(){}.getType();
			temp = gson.fromJson(isr, type);
			myself = temp;
			Log.w("User", "Loaded User");
		} catch (FileNotFoundException e) {
			Log.w("User", "No file " + USER);
			//TODO: Try to grab it from an online source
			myself = new User();
		}
		
		
	}
	/**
	 * Writes the user class out to disk. This is used to store the users profile locally such that 
	 * it can be retrieved without Internet if needed.
	 */
	private void writeUser(){
		this.mID = 	Secure.getString(application.getContext().getContentResolver(),
	               Secure.ANDROID_ID);
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		gson = builder.create();
		
		if (!ioDisabled) {
			try {
				FileOutputStream fos = application.getContext().openFileOutput(
						USER, Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				Log.w("User", gson.toJson(this));
				gson.toJson(this, osw);
				osw.flush();
				osw.close();
				Log.w("User", "Saved: " + USER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks if the supplied ID is inside the users bookmarks. Most useful
	 * for the User Controller to decide if it needs to add a comment ID to
	 * the bookmarks array or remove it. Also used by the views to determine
	 * if a comment is bookmarked.
	 * @param ID The ID of the comment we are checking
	 * @return
	 */
	public boolean inBookmarks(CommentModel comment){
		String ID = generateIDString(comment);
		if(mBookMarks == null){
			Log.w("Bookmark", "NULL");
		}
		return mBookMarks.contains(ID);
	}
	/**
	 * Adds a comment ID to the bookmarks array. Does not check for 
	 * duplicates.
	 * @param ID The comment ID
	 */
	public void addBookmark(CommentModel comment){
		String ID = generateIDString(comment);
		Log.w("Bookmark", "Adding: " + ID);
		mBookMarks.add(ID);
		Log.w("User", "1");
		writeUser();
		
	}
	/**
	 * Removes a comment ID from the bookmarks array. Does not check to 
	 * see if it exists before removing just removes IF it exists.
	 * @param ID The comment ID
	 */
	public void removeBookmark(CommentModel comment){
		String ID = generateIDString(comment);
		Log.w("Bookmark", "Removing: " + ID);
		mBookMarks.remove(ID);
		Log.w("User", "2");
		writeUser();
	}
	
	/**
	 * Checks if the supplied ID is inside the users bookmarks. Most useful
	 * for the User Controller to decide if it needs to add a comment ID to
	 * the bookmarks array or remove it. Also used by the views to determin
	 * if a comment is bookmarked.
	 * @param ID The ID of the comment we are checking
	 * @return
	 */
	public boolean inFavourites(CommentModel comment){
		String ID = generateIDString(comment);
		return mFavourites.contains(ID);
	}
	/**
	 * Adds a comment ID to the bookmarks array. Does not check for 
	 * duplicates.
	 * @param ID The comment ID
	 */
	public void addFavourite(CommentModel comment){
		String ID = generateIDString(comment);
		Log.w("Favourites", "Adding: " + ID);
		mFavourites.add(ID);
		Log.w("User", "3");
		writeUser();
		
	}
	/**
	 * Removes a comment ID from the bookmarks array. Does not check to 
	 * see if it exists before removing just removes IF it exists.
	 * @param ID The comment ID
	 */
	public void removeFavourite(CommentModel comment){
		String ID = generateIDString(comment);
		Log.w("Favourites", "Removing: " + ID);
		mFavourites.remove(ID);
		Log.w("User", "4"); 
		writeUser();
	}
	
	/**
	 * Builds a special comment ID for simple storage. This is parentID:CommentID. This allows us to 
	 * store both as one string then break it apart to get it the individual parts back later.
	 * @param comment THe comment
	 * @return
	 */
	public String generateIDString(CommentModel comment){
		return comment.getmParentID()+":"+comment.getmEsID();
	}
	/**
	 * Breaks up an ID string and returns the parent ID portion. Format is ParentID:CommentID
	 * @param ID The full ID String
	 * @return the parent ID
	 */
	public String breakParentID(String ID){
		return this.tokenizeID(ID).get(0);
	}
	/**
	 * Breaks up an ID string and returns the comment ID portion. Format is ParentID:CommentID
	 * @param ID The full ID String
	 * @return the comment ID
	 */
	public String breakID(String ID){
		return this.tokenizeID(ID).get(1);
	}
	/**
	 * Tokenizes an ID string using ':' as the delimiter.
	 * @param ID The ID string
	 * @return An array list of the token strings
	 */
	private ArrayList<String> tokenizeID(String ID){
		ArrayList<String> parts = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(ID, ":");
		while (st.hasMoreTokens()) {
	         parts.add(st.nextToken());
	     }
		return parts;
	}
	
	public void setInitialLocation() {
		this.myLat = (double) 0;
		this.myLong = (double) 0;
	}

	/**
	 * Used to initialize the LocationManager that will help provide the application
	 * with the users current location. Establishes the best provider for such a task.
	 * @param void
	 * @return void
	 */
	public void setUpLocationServices() {
			Context context = application.getContext();
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	/**
	 * Checks if there are any location service providers enabled, ex. gps/network/MockProvider
	 * These providers are used to get the users current location. If there is no provider currently 
	 * enabled it returns false and a default location is used. If a provider is enabled it returns 
	 * True.
	 * @return True if a provider is enabled, False if no provider is enabled.
	 */
	public boolean isProviderAvailable() {
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_COARSE);
		provider = lm.getBestProvider(crit, true);
		if (provider == null || provider.equals(LocationManager.PASSIVE_PROVIDER)) {
			Log.d("PROVIDER_CHECK", "NO PROVIDER AVAILABLE");
			return false;
		}
		Log.d("PROVIDER_CHECK", "PROVIDER " + provider);
		return true;
	}
	
	/**
	 * Used to get the usersCurrentLocation, available at any time. The users Location
	 * is optionally used (by default) when a comment is created/edited. Users location 
	 * is also used any time the application calls a user-location based sorting function
	 * 
	 * If no provider is enabled it returns the users last stored location.
	 * @return User's Current/Last Known Location
	 */
	public Location getCurrentLocation() {
		Location temp =  new Location("userLocation");
		setUpLocationServices();
		if (isProviderAvailable()){
			Log.w("COMMENT_LOC", "IS AVAILABLE");
			this.setMyLastKnownLocation(lm.getLastKnownLocation(provider)); 
		}

		temp.setLatitude(this.myLat);
		temp.setLongitude(this.myLong);
		return temp;
	}
	
	/**
	 * Sets the users last known position to a specific location
	 * @param location The location to set the users last known locaiton to.
	 */
	public void setMyLastKnownLocation(Location location){
		//this.myLat = (double) 0;
		//this.myLong = (double) 0;
		Log.d("NULL_LOC", "Caught the fake provided gps");
		if (location != null) {
			this.myLat = location.getLatitude();
			this.myLong = location.getLongitude();
		}
	}

	/**
	 * Returns the array of strings representing the ID's necessary to get 
	 * the comments in my bookmarks from the web or the cache.
	 * @return Array of the IDs <String>
	 */
	public ArrayList<String> getMyBookmarks(){
		return this.mBookMarks;
	}

	
	/**
	 * Returns the array of strings representing the ID's necessary to get 
	 * the comments in my favourites from the web or the cache.
	 * @return Array of the IDs <String>
	 */
	public ArrayList<String> getMyFavourites(){
		return this.mFavourites;
	}

	/**
	 * Returns the profile picture associated with this profile. Used
	 * to display the profile pic in the UI.
	 * @return A profiles profile picture.
	 */
	public Bitmap getProfilePic() {
		return profilePic;
	}
	/**
	 * Sets a new profile picture. This is something a user might
	 * do for their own profile but should not be able to be done 
	 * to ones we do not own.
	 * @param profilePic The new profile pic
	 */
	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
		Log.w("User", "5");
	}

	/**
	 * Returns the contact information associated with this profile. Used
	 * to display the info in the UI.
	 * @return The profile uses contact info
	 */
	public String getContactInfo() {
		return contactInfo;
	}
	/**
	 * Sets a new contact info for the user. Should be a single string
	 * containing an e-mail address or a twitter handle, etc.
	 * @param contactInfo The new contact info.
	 */
	private void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	/**
	 * Gets the biography for the user profile. Used to display
	 * this info in the UI.
	 * @return Profile biography
	 */
	public String getBiography() {
		return biography;
	}
	/**
	 * Sets a new user biography. Should only be able to set
	 * your own biography not others.
	 * @param biography The new biography
	 */
	private void setBiography(String biography) {
		this.biography = biography;
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Updates the local profile with new information then sync's it online.
	 * @param userName The new username
	 * @param contactInfo The new contact Info
	 * @param bio The new Bio
	 * @param profile The new profile picture
	 */
	public void update(String userName, String contactInfo, String bio, Bitmap profile){
		this.setUserName(userName);
		this.setContactInfo(contactInfo);
		this.setBiography(bio);
		this.setProfilePic(profile);
		this.writeUser();
		this.syncUser();
	}
	/**
	 * This will sync the user class with the online storage. If web is unavailable then 
	 * flag the user for update once it comes online.
	 */
	private void syncUser(){
		ProfilePush pusher = new ProfilePush();
		if(application.isNetworkAvailable()){
			pusher.pushProfile(this);	
			needUpdate = false;
		}else{
			needUpdate = true;
		}
		
	}
	
}
