package ca.ualberta.cs.team5geotopics;


import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class CacheIO {
	private ArrayList<String> fileDir;
	private String path;

	public CacheIO(String absolutePath) {
		this.path = absolutePath;
		this.fileDir = new ArrayList<String>();
		File historyFolder = new File(path, "history");
		historyFolder.mkdir();// makes a folder "history/" in our apps section
								// of internal storage
	}

	public ArrayList<String> getFileDir() {
		return fileDir;
	}

	public void setFileDir(ArrayList<String> fileDir) {
		this.fileDir = fileDir;
	}
	
	/**
	 * Saves the file directory back to disk.
	 */
	public void saveFileList(String path) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.fileDir);
		replaceFileHistory(jsonString, "files.sav");
	}

	/**
	 * Replaces the current file history with a new one.
	 * @param jsonString The file history we are replacing the old one with in json format
	 * @param filename The location of the file history we are replacing
	 */
	public void replaceFileHistory(String jsonString, String filename) {
		if (!this.fileDir.contains(filename) && !filename.equals("files.sav")) {
			this.fileDir.add(filename);
			saveFileList(this.path);
			Log.w("Cache", "added file to fileDir: " + filename);
		}
		FileOutputStream fos = null;
		try {
			File file = new File(path + "/history", filename);
			fos = new FileOutputStream(file);
			fos.write(jsonString.getBytes());
			Log.w("Cache", "Writing this to disk");
			Log.w("Cache", jsonString);
		} catch (FileNotFoundException e) {
			Log.w("Cache", "File not found");
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
	 * Loads the file directory list from disk for the cache.
	 */
	public void loadFileList() {
		File file = new File(this.path + "/history", "files.sav");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				this.fileDir = gson.fromJson(isr, type);
			} catch (IOException e) {
				Log.w("Cache", "IO exception in reading fileDir");
			}
		}
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
			File file = new File(this.path + "/history", filename);
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			try {
				Type acmType = new TypeToken<ArrayList<CommentModel>>() {
				}.getType();
				commentList = gson.fromJson(isr, acmType);
			} catch (NullPointerException e) {
				Log.w("Cache", "comments are null or model not registered");
			}

		} catch (FileNotFoundException e) {
			Log.w("Cache", "ERROR: File not found (loading cache)");
		} catch (IOException e) {
			Log.w("Cache", "ERROR: Java IO error reading cache file");
		}
		return commentList;
	}
}