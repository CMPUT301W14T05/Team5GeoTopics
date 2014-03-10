package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;

// code adapted from http://android-developers.blogspot.ca/2011/03/identifying-app-installations.html

public class User extends AModel<AView> {
	// string representing the user ID
	private String mID;
	private static final String INSTALLATION_ID = "INSTALLATION";
	private static final String POST_COUNT = "POSTCOUNT";
	private File mInstallation;
	private File mPostCount;
	private ArrayList<CommentModel> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	private ArrayList<CommentModel> mComments; // My created comments
	private boolean isLoaded = false;
	private static User myself = new User();
	private GeoTopicsApplication application;

	/*This part here needs to be updated as we are currently both a singleton and not
	 * Will have to do some research into how we can solve this at a later time.
	 */
	//***********************************************************************************
	/*
	public User(Context context) {
		mInstallation = new File(context.getFilesDir(), INSTALLATION_ID);
		mPostCount = new File(context.getFilesDir(), POST_COUNT);
	}
	*/
	
	private User() {
		this.application = GeoTopicsApplication.getInstance();
		this.mBookMarks = new ArrayList<CommentModel>();
		this.mFavorites = new ArrayList<CommentModel>();
		this.mComments = new ArrayList<CommentModel>();
		mInstallation = new File(application.getContext().getFilesDir(), INSTALLATION_ID);
		mPostCount = new File(application.getContext().getFilesDir(), POST_COUNT);
	}

	public static User getInstance() {
		return myself;
	}
	//***********************************************************************************
	/*
	 * messages to read and write dependent files
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

	public boolean installFilesExist() {
		return (mInstallation.exists()) && (mPostCount.exists());
	}

	public void addToMyComments(CommentModel comment) {
		mComments.add(comment);
		this.notifyViews();
		//this.writeComments("myComments");
	}

	public ArrayList<CommentModel> getMyComments() {
		return this.mComments;
	}
}
