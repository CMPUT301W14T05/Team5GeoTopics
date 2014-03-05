package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

public class CommentListModel extends AModel<AView>{
	ArrayList<CommentModel> mComments;
	
	public CommentListModel(){
		this.mComments = new ArrayList<CommentModel>();
		
	}
	
	public ArrayList<CommentModel> getList() {
		return mComments;
	}
	
	public void add(CommentModel comment) {
		mComments.add(comment);
		this.notifyViews();
	}
	
	public void addAll(ArrayList<CommentModel> )
}
