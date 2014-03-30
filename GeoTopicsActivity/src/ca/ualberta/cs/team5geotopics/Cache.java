package ca.ualberta.cs.team5geotopics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Cache is what is responsible for saving and loading data to and from the
 * user's device. This file is the 'history' of the program.
 */

public class Cache extends AModel<AView> {
	private Context context;
	private GeoTopicsApplication application;
	private ArrayList<String> fileDir;
	private CommentListModel browseModel;
	private String path;

	boolean isLoaded;

	private static Cache myself;

	/**
	 * Cache constructor. Will load its necessary files from disk.
	 * 
	 * @return A cache object
	 */
	private Cache() {
		this.application = GeoTopicsApplication.getInstance();
		this.fileDir = new ArrayList<String>(); // this is a directory of the
												// cache files
		context = application.getContext();
		try {
			this.path = context.getFilesDir().getAbsolutePath();
		} catch (NullPointerException e) {
			// just go on. This is Null in the test
		}
		File historyFolder = new File(path, "history");
		historyFolder.mkdir();// makes a folder "history/" in our apps section
								// of internal storage

		isLoaded = false;
	}

	/**
	 * Returns the cache singleton. Only one cache object can exist at once.
	 * Will construct a new one if one does not currently exist.
	 * 
	 * @return The cache
	 */
	public static Cache getInstance() {
		if (myself == null)
			myself = new Cache();
		return myself;
	}

	/**
	 * Loads the file directory list from disk for the cache.
	 * 
	 */
	public void loadFileList() {
		File file = new File(path + "/history", "files.sav");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(fis));
				String jsonString = ""; // empty string
				String line = in.readLine();
				while (line != null) {
					jsonString = jsonString.concat(line);
					line = in.readLine();
				}
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				this.fileDir = gson.fromJson(jsonString, type);
			} catch (IOException e) {
				Log.w("Cache", "IO exception in reading fileDir");
			}
		}

	}

	/**
	 * Saves the file directory back to disk.
	 * 
	 */
	public void saveFileList() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.fileDir);
		replaceFileHistory(jsonString, "files.sav");
	}

	/**
	 * Used to determine if any replies are currently cached at the file
	 * location.
	 * 
	 * @param filename
	 *            The location on disk to search for replies
	 * @return True if replies exist at said location.
	 */
	public boolean repliesExist(String filename) {
		// returns true if there are replies in the cache
		if (!filename.equals("-1")) {
			return this.fileDir.contains(filename);
		} else {
			return this.fileDir.contains("history.sav");
		}

	}

	/**
	 * Replaces the current file history with a new one.
	 * 
	 * @param jsonString
	 *            The file history we are replacing the old one with in json
	 *            format
	 * @param filename
	 *            The location of the file history we are replacing
	 */
	public void replaceFileHistory(String jsonString, String filename) {
		/*
		 * this will save the serialized comments retrieved from elasticsearch
		 * to disk. Right now this just replaces the file on disk with the last
		 * elasticsearch query result which shares that esID
		 */
		if (!this.fileDir.contains(filename) && !filename.equals("files.sav")) {
			this.fileDir.add(filename);
			saveFileList();// saves to disk (may be a source of slowness)
			Log.w("Cache", "added file to fileDir: " + filename);
		}
		FileOutputStream fos = null;
		try {
			File file = new File(path + "/history", filename);
			fos = new FileOutputStream(file);
			fos.write(jsonString.getBytes());
			Log.w("Cache", jsonString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileDir.add(filename);
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This will search the cache and return the requested comment. The EsID of
	 * the parent is necessary as we catalogue comments based on their parent ID
	 * and thus we need this to find the location in the file system to look.
	 * Will return null if we cannot find the comment in the cache.
	 * 
	 * @param mParent
	 *            The EsID of the parent Comment.
	 * @param EsID
	 *            The EsID of the comment we want.
	 * @return The comment we requested from the cache, null if not found.
	 */
	public CommentModel loadComment(String mParentID, String EsID) {
		ArrayList<CommentModel> commentList;

		this.loadFileList();

		if (this.repliesExist(mParentID)) {

			if (mParentID.equals("-1")) {
				commentList = load("history.sav");
			} else {
				commentList = load(mParentID);
			}

			for (CommentModel comment : commentList) {
				if (comment.getmEsID().equals(EsID)) {
					return comment;
				}
			}
		}
		return null;

	}

	/**
	 * Will take a comment and either update a current version of it or add it
	 * to the cache.
	 * 
	 * @param comment
	 *            The comment to add/update in the cache
	 */
	public void updateCache(CommentModel comment) {
		ArrayList<CommentModel> commentList;
		String mParentID = comment.getmParentID();
		String EsID = comment.getmEsID();
		int i;
		boolean findFlag = false;

		this.loadFileList();
		// If the parent folder exists search it
		if (this.repliesExist(mParentID)) {
			if (mParentID.equals("-1")) {
				Log.w("Cache", "Updating with a top level");
				commentList = load("history.sav");
			} else {
				commentList = load(mParentID);
			}
			// Search the lost
			for (i = 0; i < commentList.size(); i++) {
				if (commentList.get(i).getmEsID().equals(EsID)) {
					// We found a copy of it so lets replace it and
					// flag that we found it.
					commentList.set(i, comment);
					findFlag = true;
					break;
				}
			}
			// We did not find a copy of this comment
			// in its parents folder. Add it to the list then
			if (!findFlag) {
				commentList.add(comment);
			}
		} else {
			// There was not folder for the parent so we
			// Create a new empty list and add the comment to it.
			commentList = new ArrayList<CommentModel>();
			commentList.add(comment);
		}
		if(mParentID.equals("-1")){
			serializeAndWrite(commentList, "history.sav");
		}else{
			serializeAndWrite(commentList, mParentID);
		}
	}

	/**
	 * This will serialize the array list using JSON then write it to the
	 * appropriate place on disk.
	 * 
	 * @param commentList
	 *            The list to serialise and write to disk
	 * @param filename
	 *            The file to save the list too
	 */
	public void serializeAndWrite(ArrayList<CommentModel> commentList,
			String filename) {
		Gson gson = new Gson();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		gson = builder.create();
		
		replaceFileHistory(gson.toJson(commentList), filename);
	}

	/**
	 * Loads from the cache at the given filename location. Puts the results
	 * into the comment list model supplied.T he file name is the EsID of the
	 * parent comment for which you are looking for the list of replies. If we
	 * are looking for top levels then supply filename as null.
	 * 
	 * @param filename
	 *            The location to read.
	 * @param clm
	 *            The comment list model we need to populate.
	 */
	public void loadFromCache(String filename, CommentListModel clm) {
		ArrayList<CommentModel> commentList;
		if (!filename.equals("-1")) {
			commentList = load(filename);
		} else {
			commentList = load("history.sav");
		}
		clm.addNew(commentList);
	}

	/**
	 * Loads comments from cache. Comments loaded get put into the registered
	 * comment list model. Thus registration must precede the load.
	 * 
	 * @param filename
	 *            The filename where we are to load comments from
	 * @param currentActivity
	 *            The activity requesting the load
	 * @return ArrayList<CommentModel>
	 */
	public ArrayList<CommentModel> load(String filename) {

		ArrayList<CommentModel> commentList = null;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		final Gson gson = builder.create();

		FileInputStream fis = null;

		try {
			File file = new File(path + "/history", filename);
			fis = new FileInputStream(file);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					fis));
			try {
				String jsonString = "";
				String line = in.readLine();
				while (line != null) {
					jsonString = jsonString.concat(line);
					line = in.readLine();
				}
				Type acmType = new TypeToken<ArrayList<CommentModel>>() {
				}.getType();
				commentList = gson.fromJson(jsonString, acmType);
			} catch (NullPointerException e) {
				Log.w("Cache", "comments are null or model not registered");
			} catch (IOException e) {
				Log.w("Cache", "IO exception in the thread");
			}

		} catch (FileNotFoundException e) {
			Log.w("Cache", "ERROR: File not found (loading cache)");
		} catch (IOException e) {
			Log.w("Cache", "ERROR: Java IO error reading cache file");
		}
		return commentList;
	}
}
