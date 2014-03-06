package ca.ualberta.cs.team5geotopics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Cache extends AModel<AView> {
	private ArrayList<CommentModel> mHistory;
	private ArrayList<CommentModel> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	private ArrayList<CommentModel> mComments; // My created comments
	private boolean isLoaded = false;

	private static Cache myself = new Cache();

	private Cache() {
		this.mHistory = new ArrayList<CommentModel>();
		this.mBookMarks = new ArrayList<CommentModel>();
		this.mFavorites = new ArrayList<CommentModel>();
		this.mComments = new ArrayList<CommentModel>();
		this.dummyData();
	}

	public static Cache getInstance() {
		return myself;
	}

	public void addToMyComments(CommentModel comment, Context context) {
		mComments.add(comment);
		this.notifyViews();
		this.writeComments("myComments", context);
	}
	
	public void addToHistory(CommentModel comment, Context context) {
		mHistory.add(comment);
		this.notifyViews();
		this.writeComments("myHistory", context);
	}
	
	//Load the cache with dummy data
	private void dummyData() {
		Location l1 = new Location("l1");
		Location l2 = new Location("l2");
		Location l3 = new Location("l3");
		l1.setLatitude(0);
		l1.setLongitude(0.001);
		l2.setLatitude(0);
		l2.setLongitude(2);
		l3.setLatitude(0);
		l3.setLongitude(0.008);
		
		CommentModel tlc1 = new CommentModel("I am indestructable!!", "Superman", "Info about superman", l1);
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel tlc2 = new CommentModel("I am a pansy", "Spiderman", "Info about spiderman", l2);
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel tlc3 = new CommentModel("I can't feel my legs guys", "Professor X", "Info about Professor X", l3);
		
		tlc1.addReply(new CommentModel("Not if I have Kryptonite!", "Anonymoose"));
		tlc2.addReply(new CommentModel("I am sure someone loves you", "Green Goblin"));
		
		mHistory.add(tlc3);
		mHistory.add(tlc2);
		mHistory.add(tlc1);
	}

	// Stubb. Will write the my comments array to disk

	private void writeMyComments(Context context) {

	}
	
	/*
	 * I'm not too familiar with how to actually write arrays of Objects but I am going off
	 * of the following Article: 
	 * 
	 * Author: Kevin Tambascio
	 * URL: https://www.tambascio.org/kevin/android/gson-and-android/ (March 6th, 2014)
	 * 
	 * NOTE FOR JAMES: I figured if they are all using roughly the same code for writing 
	 * we could probably write on function to do the writing.
	 */
	private void writeComments(String name, Context context) {
		Gson gson = new Gson();
		String myCommentsData = gson.toJson(mComments);
		
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(name, Context.MODE_PRIVATE);
			fos.write(myCommentsData.getBytes());
		} catch (FileNotFoundException e) {
			/*
			 * handle the exception
			 */
		} catch (IOException e) {
			/*
			 * handle the exception
			 */
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				/*
				 * do sumthing
				 */
			}
		}
	}

	// Stubb. Will write the my history array to disk
	private void writeMyHistory(Context context) {
		
	}

	// Stubb. Will write the my bookmarks array to disk
	private void writeMyBookmarks() {

	}

	// Stubb. Will write the my favourites array to disk
	private void writeMyFavourites() {

	}
	
	public void loadCache(Context context) {
		if(!isLoaded) {
			this.loadAll(context);
		}
	}
	
	//Stubb. This will load all the caches from disk
	/*
	 * I'm not too familiar with how to actually write arrays of Objects but I am going off
	 * of the following Article: 
	 * 
	 * Author: Kevin Tambascio
	 * URL: https://www.tambascio.org/kevin/android/gson-and-android/ (March 6th, 2014)
	 * 
	 * 
	 * NOTE FOR JAMES: I figured this would at least lay out a base for you, I didn't do 
	 * this write on the assignment I don't think that's why I have little confidence in this code.
	 * the below only read myComments.
	 */
	@SuppressWarnings("unchecked")
	private void loadAll(Context context) {
	    Gson gson = new Gson(); //1
	    FileInputStream fis = null; 
	    try { 
	        fis = context.openFileInput("myComments"); //2
	        Type collectionType = new TypeToken<Collection<CommentModel>>(){}.getType(); //3 
	        List myComments= gson.fromJson(new InputStreamReader(fis), collectionType); //4
	        if(myComments != null) { 
	            this.mComments.addAll(myComments);
	        }
	    }
	    catch(JsonIOException e) { 
	    	/*
	    	 * Handle Exception
	    	 */
	    }
	    catch(JsonSyntaxException e) { 
	    	/*
	    	 * Handle Exception
	    	 */
	    }
	    catch (FileNotFoundException e) { 
	    	/*
	    	 * Handle Exception
	    	 */
	    }
	    finally { 
	        try { 
	            if(fis != null)
	                fis.close(); //5
	        }
	        catch (IOException e)  { 
		    	/*
		    	 * Handle Exception
		    	 */
	        }
	    }
	}
	
	public ArrayList<CommentModel> getHistory() {
		return this.mHistory;
	}
}
