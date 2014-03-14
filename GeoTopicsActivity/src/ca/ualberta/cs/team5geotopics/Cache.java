package ca.ualberta.cs.team5geotopics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Cache extends AModel<AView> {
	private ArrayList<CommentModel> mHistory;
	private Context context;
	private GeoTopicsApplication application;
	boolean isLoaded;

	private static Cache myself = new Cache();

	private Cache() {
		this.mHistory = new ArrayList<CommentModel>();
		this.application = GeoTopicsApplication.getInstance();
		context = application.getContext();
		isLoaded = false;
	}

	public static Cache getInstance() {
		return myself;
	}
	
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
	}
	
	/*//Removed the write because it will make add to history hard to test
	public void replaceHistory(ArrayList<CommentModel> mHistory) {
		this.mHistory = mHistory;
		this.notifyViews();
		Log.w("Cache-write myCommentsData", "Replace History First");
		this.isLoaded = true;
	}*/
	
	/*this will save the serialized comments retrieved from elasticsearch to disk
	 * right now this just replaces the file on disk with the last elasticsearch query result.
	 * TODO: introduce file system tree
	 */
	public void replaceHistory(String jsonString) {
		Log.w("Cache-write myCommentsData", "Replace History First");
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput("history.sav", Context.MODE_PRIVATE);
			fos.write(jsonString.getBytes());
			Log.w("Cache-write myCommentsData", jsonString);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

	//Removed the write because it will make add to history hard to test
	public void addToHistory(CommentModel comment) {
		if(!mHistory.contains(comment)) {
			this.mHistory.add(comment);
			this.notifyViews();
		}
	}
	
	public void writeMyHistory() {
		writeComments("history.sav", mHistory);
	}

	public ArrayList<CommentModel> getHistory() {
		return this.mHistory;
	}
	
	public boolean isCacheLoaded() {
		return isLoaded;
	}
	
	public void loadCache(){
		if (!isLoaded){
			Log.w("Cache","Loading File");
			this.mHistory = loadFromCache("history.sav");
		}else{
			Log.w("Cache","Loaded");
		}
	}
	
	//Populate a comment list model with replies
	//I return the thread in case you ever want to wait on it to finish.
	//This is mostly to ensure the test cases pass as they some times execute faster 
	//the thread.
	//- James
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

	/*
	 * Author: Kevin Tambascio URL:
	 * https://www.tambascio.org/kevin/android/gson-and-android/ (March 6th,
	 * 2014)
	 */
	private void writeComments(final String name,
			final ArrayList<CommentModel> savedList) {

		Thread thread = new Thread() {

			@Override
			public void run() {
				// -----------------------------------------------------
				/*
				 * use of GraphAdapterBuilder adapted from
				 * http://stackoverflow.com
				 * /questions/10036958/the-easiest-way-to
				 * -remove-the-bidirectional-recursive-relationships by Jesse
				 * Wilson taken 2014-03-07
				 */
				GsonBuilder gsonBuilder = new GsonBuilder();
				new GraphAdapterBuilder().addType(CommentModel.class)
						.registerOn(gsonBuilder);
				Gson gson = gsonBuilder.create();
				// -----------------------------------------------------

				String myCommentsData;

				FileOutputStream fos = null;
				try {
					fos = context.openFileOutput(name, Context.MODE_PRIVATE);
					for (int i = 0; i < mHistory.size(); i++) {
						myCommentsData = gson.toJson(mHistory.get(i)) + "\n"; 
						// delineate comment model elements with newline
						fos.write(myCommentsData.getBytes());
						Log.w("Cache-write myCommentsData", myCommentsData);
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.start();
	}

	public ArrayList<CommentModel> loadFromCache(String filename) {
	    Gson gson = new Gson(); 
	    ArrayList<CommentModel> resultList = new ArrayList<CommentModel>();
	    FileInputStream fis = null; 
	    try { 
	        fis = context.openFileInput(filename); 
	        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	        String line = in.readLine();
	        while (line != null) {
				resultList.add(gson.fromJson(line, CommentModel.class));
				line = in.readLine();
	        }
	    }
	    catch(JsonSyntaxException e) { 
	    	e.printStackTrace();
	    	//TODO: print from system.err stream in LogCat
	    }
	    catch (FileNotFoundException e) { 
	    	e.printStackTrace();
	    } 
	    catch (IOException e) {
			e.printStackTrace();
		}
	    finally { 
	        try { 
	            if(fis != null)
	                fis.close();
	        }
	        catch (IOException e)  { 
	        	e.printStackTrace();
	        }
	    }
	    Log.w("Cache","Loaded File");
	    this.isLoaded = true;
		return resultList;
	}
	
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
