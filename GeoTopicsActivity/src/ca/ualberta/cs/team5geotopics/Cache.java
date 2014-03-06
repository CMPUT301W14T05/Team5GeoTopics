package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

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
		CommentModel tlc1 = new CommentModel("I am indestructable!!", "Superman", "Info about superman");
		CommentModel tlc2 = new CommentModel("I am a pansy", "Spiderman", "Info about spiderman");
		CommentModel tlc3 = new CommentModel("I can't feel my legs guys", "Professor X", "Info about Professor X");
		
		tlc1.addReply(new CommentModel("Not if I have Kryptonite!", "Anonymoose"));
		tlc2.addReply(new CommentModel("I am sure someone loves you", "Green Goblin"));
		
		mHistory.add(tlc1);
		mHistory.add(tlc2);
		mHistory.add(tlc3);
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
