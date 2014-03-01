package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

public class Cache extends AModel<AView>{
	private ArrayList<CommentModel> mHistory;
	private ArrayList<CommentModel> mBookMarks;
	private ArrayList<CommentModel> mFavorites;
	
	public Cache(){
		this.mHistory = new ArrayList<CommentModel>();
		this.mBookMarks = new ArrayList<CommentModel>();
		this.mFavorites = new ArrayList<CommentModel>();
	}
}
