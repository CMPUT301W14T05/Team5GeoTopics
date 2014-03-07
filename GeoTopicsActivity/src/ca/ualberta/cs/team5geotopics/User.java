package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Context;

public class User extends AModel<AView> {
	private ArrayList<CommentModel> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	private ArrayList<CommentModel> mComments; // My created comments
	private boolean isLoaded = false;
	private static User myself = new User();
	
	private User() {
		this.mBookMarks = new ArrayList<CommentModel>();
		this.mFavorites = new ArrayList<CommentModel>();
		this.mComments = new ArrayList<CommentModel>();
	}
	
	public static User getInstance() {
		return myself;
	}
	
	public void addToMyComments(CommentModel comment, Context context) {
		mComments.add(comment);
		this.notifyViews();
		//this.writeComments("myComments", context);
	}
	
	public ArrayList<CommentModel> getMyComments() {
		return this.mComments;
	}
}
