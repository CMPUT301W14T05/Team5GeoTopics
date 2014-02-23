package ca.ualberta.cs.team5geotopics;

import java.util.List;

import android.content.Context;

public class BrowseCommentController {
	private CommentListAdapter mAdapter;
	
	public BrowseCommentController(Context context, int layoutResourceId, List<CommentModel> comments){
		super();
		this.mAdapter = new CommentListAdapter(context, layoutResourceId, comments);
	}
	
	public CommentListAdapter getAdapter(){
		return this.mAdapter;
	}
}
