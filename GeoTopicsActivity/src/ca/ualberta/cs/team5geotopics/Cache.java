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
		try{
			this.path = context.getFilesDir().getAbsolutePath();
		}catch (NullPointerException e) {
			//just go on. This is Null in the test
		}
		File historyFolder = new File(path,"history");
		historyFolder.mkdir();//makes a folder "history/" in our apps section of internal storage		

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
		return this.fileDir.contains(filename);
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
		 * to disk. Right now this just replaces the file on disk with
		 * the last elasticsearch query result which shares that esID
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
			Log.w("Cache-write myCommentsData", jsonString);
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
	 * Registers a model with the cache. This is to ensure the loads get put
	 * into the right model.
	 * 
	 * @param listModel
	 *            The comment list model to register
	 */
	public void registerModel(CommentListModel listModel) {
		this.browseModel = listModel;
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
	public ArrayList<CommentModel> loadFromCache(String filename,
			final BrowseActivity currentActivity) {

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

			Runnable updateModel = new Runnable() {
				@Override
				public void run() {
					try {
						String jsonString = "";
						String line = in.readLine();
						while (line != null) {
							jsonString = jsonString.concat(line);
							line = in.readLine();
						}
						Type acmType = new TypeToken<ArrayList<CommentModel>>() {
						}.getType();
						ArrayList<CommentModel> commentList = gson.fromJson(
								jsonString, acmType);
						browseModel.addNew(commentList);
					} catch (NullPointerException e) {
						Log.w("Cache",
								"comments are null or model not registered");
					} catch (IOException e) {
						Log.w("Cache", "IO exception in the thread");
					}
				}
			};
			currentActivity.runOnUiThread(updateModel);

			String jsonString = "";
			String line = in.readLine();
			while (line != null) {
				jsonString = jsonString.concat(line);
				line = in.readLine();
			}
			Type acmType = new TypeToken<ArrayList<CommentModel>>(){}.getType();
			commentList = gson.fromJson(jsonString, acmType);
	    	

		} catch (FileNotFoundException e) {
			Log.w("Cache", "ERROR: File not found (loading cache)");
		} catch (IOException e) {
			Log.w("Cache", "ERROR: Java IO error reading cache file");
		}
		Log.w("Cache", "Loaded File");
		this.isLoaded = true;
		return commentList;
	}
}
