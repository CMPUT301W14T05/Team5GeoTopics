package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Cache is what is responsible for saving and loading data to and from the
 * user's device. This file is the 'history' of the program.
 */

public class Cache extends AModel<AView> {
	private CacheIO cacheIO;
	private Context context;
	private GeoTopicsApplication application;
	boolean isLoaded;

	private static Cache myself;

	/**
	 * Cache constructor. Will load its necessary files from disk.
	 * 
	 * @return A cache object
	 */
	private Cache() {
		this.application = GeoTopicsApplication.getInstance();
		
		context = application.getContext();
		try {
		cacheIO = new CacheIO(context.getFilesDir().getAbsolutePath());
		} catch (NullPointerException e) {
			// just go on. This is Null in the test
		}
		isLoaded = false;
	}

	public CacheIO getCacheIO(){
		return this.cacheIO;
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
		cacheIO.loadFileList();

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
			return this.cacheIO.getFileDir().contains(filename);
		} else {
			return this.cacheIO.getFileDir().contains("history.sav");
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

		cacheIO.loadFileList();

		if (this.repliesExist(mParentID)) {

			if (mParentID.equals("-1")) {
				commentList = this.cacheIO.load("history.sav");
			} else {
				commentList = this.cacheIO.load(mParentID);
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
	 * Updates the cache with a whole list of comments. This update assumes that
	 * the list of comments all have the SAME parent and that a version of the
	 * list exists in the cache.
	 * 
	 * @param updatedList
	 *            Updated list of comments
	 */
	public void updateCache(ArrayList<CommentModel> updatedList, String mParentID) {
		ArrayList<CommentModel> commentList;
		if (this.repliesExist(mParentID)) {
			Log.w("Cache", "Parent Exists");
			if (mParentID.equals("-1")) {
				Log.w("Cache", "Updating with a top level");
				commentList = this.cacheIO.load("history.sav");
			} else {
				Log.w("Cache", "Updating with a reply level");
				commentList = this.cacheIO.load(mParentID);
			}
			for (CommentModel comment : updatedList) {
				findAndReplace(commentList, comment);
			}
		}else{
			commentList = new ArrayList<CommentModel>();
			commentList.addAll(updatedList);
		}
		
		writeListToCache(commentList, mParentID);
	}

	/**
	 * Takes in a list of comments and searches it for a comment withe the same
	 * ID in the list. If we find one then we replace the list version with the
	 * supplied version. If we do not find it then we add it to the end.
	 * 
	 * @param commentList
	 *            The list we are searching
	 * @param comment
	 *            Version of the comment we want to replace in the list
	 */
	public void findAndReplace(ArrayList<CommentModel> commentList,
			CommentModel comment) {
		int i;
		String EsID = comment.getmEsID();
		boolean findFlag = false;

		for (i = 0; i < commentList.size(); i++) {
			if (commentList.get(i).getmEsID().equals(EsID)) {
				// We found a copy of it so lets replace it and
				// flag that we found it.
				commentList.set(i, comment);
				findFlag = true;
				Log.w("Cache", "Found a copy of it");
				break;
			}
		}
		// We did not find a copy of this comment
		// in its parents folder. Add it to the list then
		if (!findFlag) {
			Log.w("Cache", "Did not find a copy of the comment");
			commentList.add(comment);
		}
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
		cacheIO.loadFileList();

		Log.w("Cache", "Updating cache");
		// If the parent folder exists search it
		if (this.repliesExist(mParentID)) {
			Log.w("Cache", "Parent Exists");
			if (mParentID.equals("-1")) {
				Log.w("Cache", "Updating with a top level");
				commentList = this.cacheIO.load("history.sav");
			} else {
				Log.w("Cache", "Updating with a reply level");
				commentList = this.cacheIO.load(mParentID);
			}

			this.findAndReplace(commentList, comment);
		} else {
			// There was not folder for the parent so we
			// Create a new empty list and add the comment to it.
			Log.w("Cache", "No parent folder");
			commentList = new ArrayList<CommentModel>();
			commentList.add(comment);
		}
		writeListToCache(commentList, mParentID);
	}
	
	/**
	 * Takes a list of comment models and determines where to write them to disk (cache). 
	 * This is determined using the supplied parent ID
	 * @param commentList The list to write to the cache
	 * @param mParentID The ID of the parent to all the comments in the list
	 */
	public void writeListToCache(ArrayList<CommentModel> commentList, String mParentID){
		if (mParentID.equals("-1")) {
			Log.w("Cache", "Write top level");
			serializeAndWrite(commentList, "history.sav");
		} else {
			Log.w("Cache", "Write parent level: " + mParentID);
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

		cacheIO.replaceFileHistory(gson.toJson(commentList), filename);
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
			commentList = this.cacheIO.load(filename);
		} else {
			commentList = this.cacheIO.load("history.sav");
		}
		clm.addNew(commentList);
	}

}
