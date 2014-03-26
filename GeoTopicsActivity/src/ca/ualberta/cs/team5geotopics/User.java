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

import android.content.Context;
import android.graphics.Bitmap;
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
	private static final String MY_BOOKMARKS = "myBookmarks.save";
	private File mInstallation;
	private File mPostCount;
	private ArrayList<String> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	private ArrayList<String> mComments; // My created comments
	private static User myself;
	private GeoTopicsApplication application;
	private boolean ioDisabled = false;

	private User() {
		this.application = GeoTopicsApplication.getInstance();
		this.mBookMarks = new ArrayList<String>();
		this.mFavorites = new ArrayList<CommentModel>();
		loadMyComments();
		loadMyBookmarks();
		mInstallation = new File(application.getContext().getFilesDir(),
				INSTALLATION_ID);
		mPostCount = new File(application.getContext().getFilesDir(),
				POST_COUNT);
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
		this.mComments = new ArrayList<String>();
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
		this.saveMyComments();
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
	 * Writes the local list of my comments to disk. This function will 
	 * overwrite the whole file it will NOT append so make sure you have all
	 * the info you want in the mComments list before you call this function.
	 * Disk writing can be disabled with the ioDisabled flag.
	 */
	public void saveMyComments() {
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
	 * Writes the local list of my boomarks to disk. This function will 
	 * overwrite the whole file it will NOT append so make sure you have all
	 * the info you want in the mComments list before you call this function.
	 * Disk writing can be disabled with the ioDisabled flag.
	 */
	public void saveBookmarks() {
		if (!ioDisabled) {
			try {
				Gson gson = new Gson();
				GsonBuilder builder = new GsonBuilder();
				builder.registerTypeAdapter(Bitmap.class,
						new BitmapJsonConverter());
				gson = builder.create();

				FileOutputStream fos = application.getContext().openFileOutput(
						MY_BOOKMARKS, Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				gson.toJson(mBookMarks, osw);
				Log.w("Bookmakrs", gson.toJson(mBookMarks));
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
			Type type = new TypeToken<ArrayList<String>>() {
			}.getType();
			mComments = gson.fromJson(isr, type);

		} catch (FileNotFoundException e) {
			Log.w("User", "No file");
			mComments = new ArrayList<String>();
		}
	}
	
	/**
	 * Loads my bookmarks from disk. This will create a new
	 * mComemnts list so anything currently assigned to this 
	 * variable will be lost.
	 */
	@SuppressWarnings("serial")
	private void loadMyBookmarks() {
		Gson gson = new Gson();

		FileInputStream fis;
		try {
			fis = application.getContext().openFileInput(MY_BOOKMARKS);
			InputStreamReader isr = new InputStreamReader(fis);
			Type type = new TypeToken<ArrayList<String>>() {
			}.getType();
			mBookMarks = gson.fromJson(isr, type);

		} catch (FileNotFoundException e) {
			Log.w("Bookmarks", "No file");
			mBookMarks = new ArrayList<String>();
		}
	}
	
	/**
	 * Checks if the supplied ID is inside the users bookmarks. Most useful
	 * for the User Controller to decide if it needs to add a comment ID to
	 * the bookmarks array or remove it. Also used by the views to determin
	 * if a comment is bookmarked.
	 * @param ID The ID of the comment we are checking
	 * @return
	 */
	public boolean inBookmarks(CommentModel comment){
		String ID = generateIDString(comment);
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
		saveBookmarks();
		
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
		saveBookmarks();
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
}
