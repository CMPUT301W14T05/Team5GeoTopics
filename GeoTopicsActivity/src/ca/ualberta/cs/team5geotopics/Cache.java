package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.location.Location;

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

	public void addToMyComments(CommentModel comment) {
		mComments.add(comment);
		this.notifyViews();
		this.writeMyHistory();
	}
	
	public void addToHistory(CommentModel comment) {
		mHistory.add(comment);
		this.notifyViews();
		this.writeMyComments();
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
	private void writeMyComments() {

	}

	// Stubb. Will write the my history array to disk
	private void writeMyHistory() {

	}

	// Stubb. Will write the my bookmarks array to disk
	private void writeMyBookmarks() {

	}

	// Stubb. Will write the my favourites array to disk
	private void writeMyFavourites() {

	}
	
	public void loadCache() {
		if(!isLoaded) {
			this.loadAll();
		}
	}
	
	//Stubb. This will load all the caches from disk
	private void loadAll() {
		
	}
	
	public ArrayList<CommentModel> getHistory() {
		return this.mHistory;
	}
}
