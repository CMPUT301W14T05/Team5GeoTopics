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

public class Cache extends AModel<AView> {
	//private ArrayList<CommentModel> mHistory;
	private Context context;
	private GeoTopicsApplication application;
	private ArrayList<String> fileDir;
	private CommentListModel browseModel;
	private String path;
	
	boolean isLoaded;
	
	private static Cache myself = new Cache();

	private Cache() {
		//this.mHistory = new ArrayList<CommentModel>();
		this.application = GeoTopicsApplication.getInstance();
		this.fileDir = new ArrayList<String>(); // this is a directory of the cache files
		context = application.getContext();
		this.path = context.getFilesDir().getAbsolutePath();
		File historyFolder = new File(path,"history");
		historyFolder.mkdir();//makes a folder "history/" in our apps section of internal storage		
		isLoaded = false;
	}

	public static Cache getInstance() {
		return myself;
	}
	//The repeated input code can probably be made into it's own method. 
	public void loadFileList(){
		File file = new File(path+"/history", "files.sav");
		if (file.exists()){
			try{
				FileInputStream fis = new FileInputStream(file);
				final BufferedReader in = new BufferedReader(new InputStreamReader(fis));
				String jsonString = ""; //empty string
				String line = in.readLine();
				while (line != null) {
					jsonString = jsonString.concat(line);
					line = in.readLine();
				}
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<String>>(){}.getType();
				this.fileDir = gson.fromJson(jsonString, type);
			}catch (IOException e){
				Log.w("Cache", "IO exception in reading fileDir");
			}
		}
		
	}
	
	public void saveFileList(){
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.fileDir);
		replaceFileHistory(jsonString, "files.sav");
	}
	/*
	public void updateComment(CommentModel updatedComment){
		String commentId = updatedComment.getmEsID();
		for(CommentModel comment : mHistory){
			if(commentId.equals(comment.getmEsID())){
				comment.setmAuthor(updatedComment.getmAuthor());
				comment.setmTitle(updatedComment.getmTitle().toString());
				comment.setmBody(updatedComment.getmBody());
				comment.setmPicture(updatedComment.getmPicture());
				comment.setLat(updatedComment.getLat());
				comment.setLon(updatedComment.getLon());
			}
		}
	}

	public void clearHistory() {
		mHistory.clear();
		//TODO: insert clear for fileDir
	}
	*/
	public boolean repliesExist(String filename) {
		//returns true if there are replies in the cache
		return this.fileDir.contains(filename);
	}
	
	public void replaceFileHistory(String jsonString, String filename) {
		/*this will save the serialized comments retrieved from elasticsearch to disk
		 * right now I think this just replaces the file on disk with the last elasticsearch query result which shares that esID
		 */
		if (!this.fileDir.contains(filename) && !filename.equals("files.sav")){
			this.fileDir.add(filename);
			saveFileList();//saves to disk (may be a source of slowness)
			Log.w("Cache", "added file to fileDir: "+filename);
		}
		FileOutputStream fos = null;
		try {
			File file = new File(path+"/history",filename);
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
/*
	//Removed the write because it will make add to history hard to test
	public void addToHistory(CommentModel comment) {
		if(!mHistory.contains(comment)) {
			this.mHistory.add(comment);
			this.notifyViews();
		}
	}

	public ArrayList<CommentModel> getHistory() {
		return this.mHistory;
	}
	
	public boolean isCacheLoaded() {
		return isLoaded;
	}
	*/
	//Populate a comment list model with replies
	//I return the thread in case you ever want to wait on it to finish.
	//This is mostly to ensure the test cases pass as they some times execute faster 
	//the thread.
	//- James
	/*
	public Thread getReplies(final CommentListModel clm, CommentModel parent) {
		final String mEsID = parent.getmEsID();
		clm.clearList();
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				for(CommentModel comment : mHistory){
					if(!comment.isTopLevel()){
						if(comment.getmParentID().equals(mEsID))
							clm.add(comment);
					}
				}
			}
		};
		thread.start();
		return thread;
	}
	*/
	/*
	//Populate a comment list model with top level comments
		public Thread getTopLevel(final CommentListModel clm) {
			clm.clearList();
			Thread thread = new Thread(){
				@Override
				public void run() {
					for(CommentModel comment :mHistory){
						if(comment.isTopLevel()){
							Log.w("Tests", comment.getmBody());
							clm.add(comment);
						}
					}
				}
			};	
			thread.start();
			return thread;
		}
	*/	
		public void registerModel (CommentListModel listModel){
			this.browseModel = listModel;
		}

	public void loadFromCache(String filename, final BrowseActivity currentActivity) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		final Gson gson = builder.create();
		
		
	    FileInputStream fis = null; 
	    
	    try { 
	    	File file = new File(path+"/history",filename);
	    	fis = new FileInputStream(file);
	    	final BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	    	
	    	Runnable updateModel = new Runnable(){
				@Override
				public void run() {
					try{
						String jsonString = ""; //empty string
						String line = in.readLine();
						while (line != null) {
							jsonString = jsonString.concat(line);
							line = in.readLine();
						}
						Type acmType = new TypeToken<ArrayList<CommentModel>>(){}.getType();
						ArrayList<CommentModel> commentList = gson.fromJson(jsonString, acmType);
						browseModel.addNew(commentList);
					}
					catch (NullPointerException e){
						Log.w("Cache","comments are null or model not registered");
					} catch (IOException e)
					{
						Log.w("Cache","IO exception in the thread");
					}
				}
			};
			currentActivity.runOnUiThread(updateModel);
	    	
	    } catch (FileNotFoundException e) {
	    	Log.w("Cache","ERROR: File not found (loading cache)");
	    } catch (IOException e) {
	    	Log.w("Cache","ERROR: Java IO error reading cache file");
		} 
	    Log.w("Cache","Loaded File");
	    this.isLoaded = true;
	}
	
	/*	public void loadCache(){
	if (!isLoaded){
		Log.w("Cache","Loading File");
		this.mHistory = loadFromCache("history.sav", this);
	}else{
		Log.w("Cache","Loaded");
	}
 }*/
	
	//Removed the write because it will make add to history hard to test
/*	public void replaceHistory(ArrayList<CommentModel> mHistory) {
		this.mHistory = mHistory;
		this.notifyViews();
		Log.w("Cache-write myCommentsData", "Replace History First");
		this.isLoaded = true;
	}*/
	
	
	/*
	//This is the commented out version that I tried to thread
	//-James


	public void loadFromCache(final String filename, final ArrayList<CommentModel> resultList) {
			
		Thread thread = new Thread() {

			@Override
			public void run() {
		Gson gson = new Gson();
		FileInputStream fis = null;
		try {
			fis = context.openFileInput(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String line = in.readLine();
			while (line != null) {
				resultList.add(gson.fromJson(line, CommentModel.class));
				line = in.readLine();
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			// TODO: print from system.err stream in LogCat
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			}
		};

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.w("Threaded load", e);
		}
		Log.w("Cache","Loaded File");
		this.isLoaded = true;
	}
	*/
}
