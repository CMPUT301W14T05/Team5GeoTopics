package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public abstract class BrowseView extends Activity{
	private ArrayList<CommentModel> mCommentList;

	public ArrayList<CommentModel> getmCommentList() {
		return mCommentList;
	}

	public void setmCommentList(ArrayList<CommentModel> mCommentList) {
		this.mCommentList = mCommentList;
	}
	
	
	
	

}
