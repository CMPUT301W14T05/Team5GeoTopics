package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings.Secure;
import android.util.Log;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.UUID;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserIO {
	private transient File mInstallation;
	private transient File mPostCount;
	private transient GeoTopicsApplication application;
	private transient Context mContext;
	transient private static final String INSTALLATION_ID = "INSTALLATION";
	transient private static final String POST_COUNT = "POSTCOUNT";
	transient private static final String USER = "user.save";

	public UserIO(){
		this.application = GeoTopicsApplication.getInstance();
		mContext = application.getContext();
		this.mPostCount = new File(mContext
				.getFilesDir(), POST_COUNT);
		this.mInstallation = new File(mContext
				.getFilesDir(), INSTALLATION_ID);
	}

	public File getMInstallation() {
		return mInstallation;
	}

	public void setMInstallation(File mInstallation) {
		this.mInstallation = mInstallation;
	}

	public File getMPostCount() {
		return mPostCount;
	}

	public void setMPostCount(File mPostCount) {
		this.mPostCount = mPostCount;
	}

	/**
	 * Messages to read and write dependent files
	 * 
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
	 * Checks if local install files exist. This does a check to see if the
	 * files which sound contain the info exist in the applications file system.
	 * It does not check that the contents have anything useful in them.
	 * 
	 * @return boolean If the installation and post count exist
	 */
	public boolean installFilesExist() {
		return (mInstallation.exists()) && (mPostCount.exists());
	}

	/**
	 * Reads the users post count off disk. This number is used to track how
	 * many comments the user has posted. Its most common use is to be used in
	 * combination with the user ID to create unique ID's for a users posts.
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
	 * Loads the user from memory if it exists. Else it attempts to pull
	 * it from elastisearch. If that fails it makes a default
	 * user. The assumption is that if the user's profile does not exist locally
	 * or online that it is either lost for good or the user is new.
	 */
	public User loadUser(){
		
		User temp = null;
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
			Log.w("User", "Loaded User");
		} catch (FileNotFoundException e) {
			Log.w("User", "No file " + USER);
		}
		
		return temp;	
	}
	
	/**
	 * Writes the user class out to disk. This is used to store the users
	 * profile locally such that it can be retrieved without Internet if needed.
	 */
	public void writeUser(User user, boolean ioDisabled) {
		user.setmID(Secure.getString(mContext
				.getContentResolver(), Secure.ANDROID_ID));
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		gson = builder.create();

		if (!ioDisabled) {
			try {
				FileOutputStream fos = mContext
						.openFileOutput(USER, Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				Log.w("User", gson.toJson(this));
				gson.toJson(user, osw);
				osw.flush();
				osw.close();
				Log.w("User", "Saved: " + USER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}