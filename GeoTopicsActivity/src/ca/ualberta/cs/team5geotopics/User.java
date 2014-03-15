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
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// code adapted from http://android-developers.blogspot.ca/2011/03/identifying-app-installations.html

/*
 * User holds all the data that the individual user whom is using the application creates/modifies.
 * This includes top-level comments and replies and makes the view availible when the user
 * goes to "My Comments" from the start screen.
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

	/*
	 * This part here needs to be updated as we are currently both a singleton
	 * and not Will have to do some research into how we can solve this at a
	 * later time.
	 */
	// ***********************************************************************************

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
	}

	public static User getInstance() {
		if (myself == null) {
			myself = new User();
		}
		return myself;
	}

	public void testSetup() {
		this.mComments = new ArrayList<CommentModel>();
		ioDisabled = true;
	};

	public void clearLocalMyComments() {
		mComments.clear();
	}

	// ***********************************************************************************
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

	// Updates a comment currently stored in my comments
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
		this.saveMyComments();
	}

	public ArrayList<CommentModel> getMyComments() {
		return this.mComments;
	}

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
}
