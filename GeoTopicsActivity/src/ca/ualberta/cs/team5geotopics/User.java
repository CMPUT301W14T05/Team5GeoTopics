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
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
	// string representing the user ID
	private String mID;
	private static final String INSTALLATION_ID = "INSTALLATION";
	private static final String POST_COUNT = "POSTCOUNT";
	private static final String MY_COMMENTS = "myComments.save";
	private File mInstallation;
	private File mPostCount;
	private ArrayList<CommentModel> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	private ArrayList<CommentModel> mComments; // My created comments
	private static User myself;
	private GeoTopicsApplication application;
	private boolean ioDisabled = false;
	private LocationManager lm;
	private Location mGeolocation;
	private String provider;

	private User() {
		this.application = GeoTopicsApplication.getInstance();
		this.mBookMarks = new ArrayList<CommentModel>();
		this.mFavorites = new ArrayList<CommentModel>();
		// this.mComments = new ArrayList<CommentModel>();
		loadMyComments();
		mInstallation = new File(application.getContext().getFilesDir(),
				INSTALLATION_ID);
		mPostCount = new File(application.getContext().getFilesDir(),
				POST_COUNT);
		setUpLocationServices();
		setInitialLocation();
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
			myself = new User();
		}
		return myself;
	}
	
	/**
	 * Initializes the user with a test setup. Use and edit this function if the 
	 * regular implementation of user has a dependency you need to change in order
	 * for proper testing. 
	 * 
	 */
	public void testSetup() {
		this.mComments = new ArrayList<CommentModel>();
		ioDisabled = true;
	};

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
	 * Updates the hot copy of my comments with new fields. Given a comment
	 * we will search the hot list of my comments for one with the same EsID
	 * and replace it with the updated one.
	 * 
	 * @param updatedComment The updated comment that we need to replace and old one
	 * with.
	 */
	public void updateMyComment(CommentModel updatedComment) {
		String commentId = updatedComment.getmEsID();
		int count = 0;
		for (CommentModel comment : mComments) {
			if (commentId.equals(comment.getmEsID())) {
				mComments.set(count, updatedComment);
				this.notifyViews(updatedComment);
				Log.d("User", "Updated a comment");
				this.saveMyComments();
				return;
			}
			count++;
		}
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
		mComments.add(comment);
		this.notifyViews();
		this.saveMyComments();
	}

	/**
	 * Used to get a copy of the array of my comments. Used mostly to 
	 * display the list in a view.
	 * 
	 * @return this.mComments The ArrayList<CommentModel> of comments.
	 */
	public ArrayList<CommentModel> getMyComments() {
		return this.mComments;
	}

	
	/**
	 * Writes the local list of my comments to disk. This function will 
	 * overwrite the whole file it will NOT append so make sure you have all
	 * the info you want in the mComments list before you call this function.
	 * Disk writing can be disabled with the ioDisabled flag.
	 */
	private void saveMyComments() {
		if (!ioDisabled) {
			try {
				Gson gson = new Gson();
				GsonBuilder builder = new GsonBuilder();
				builder.registerTypeAdapter(Bitmap.class,
						new BitmapJsonConverter());
				gson = builder.create();

				FileOutputStream fos = application.getContext().openFileOutput(
						MY_COMMENTS, Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				gson.toJson(mComments, osw);
				Log.w("User", gson.toJson(mComments));
				osw.flush();
				osw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads my comments from disk. This will create a new
	 * mComemnts list so anything currently assigned to this 
	 * variable will be lost.
	 */
	@SuppressWarnings("serial")
	private void loadMyComments() {
		Gson gson = new Gson();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		gson = builder.create();

		FileInputStream fis;
		try {
			fis = application.getContext().openFileInput(MY_COMMENTS);
			InputStreamReader isr = new InputStreamReader(fis);
			Type type = new TypeToken<ArrayList<CommentModel>>() {
			}.getType();
			mComments = gson.fromJson(isr, type);

		} catch (FileNotFoundException e) {
			Log.w("User", "No file");
			mComments = new ArrayList<CommentModel>();
		}
	}

	/**
	 * Used to retrieve a comment from the myComments array. Assumes that you 
	 * somehow know the comment already exists in the array. If it doesn't
	 * it returns null and you will get null pointer exceptions if you
	 * do not account for this.
	 * @param EsID The ID of the comment we want
	 * @return The comment OR null if not found.
	 */
	public CommentModel getMyComment(String EsID){
		for(CommentModel comment : mComments){
			if(comment.getmEsID().equals(EsID)){
				return comment;
			}
		}
		return null;
	}
	
	public void setInitialLocation() {
		this.mGeolocation = new Location("userLocation");
		this.mGeolocation.setLatitude(0);
		this.mGeolocation.setLongitude(0);
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
			//Criteria crit = new Criteria();
			//crit.setAccuracy(Criteria.ACCURACY_COARSE);
			//provider = lm.getBestProvider(crit, true);
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
		if (isProviderAvailable())
			this.mGeolocation = lm.getLastKnownLocation(provider); 
				
		return mGeolocation;
	}
	
}
