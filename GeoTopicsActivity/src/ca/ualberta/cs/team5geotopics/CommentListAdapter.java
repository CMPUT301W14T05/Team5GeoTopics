package ca.ualberta.cs.team5geotopics;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/*
 * This adapter will be used in the BrowseViews in order to inflate
 * the ListView. If we do not use this, then we have to use default 
 * ArrayAdapter which does not allow for anything other than a string on
 * the ListView. We need picture and such. We will also be able to 
 * recycle views and such.  
 * 
 */
public class CommentListAdapter extends ArrayAdapter<CommentModel> {
	private List<CommentModel> mCommentList;
	private int mLayoutResourceId;
	private Context mContext;
	private CommentListAdapter mAdapter;
	
	public CommentListAdapter(Context context, int layoutResourceId, List<CommentModel> comments){
		super(context, layoutResourceId, comments);
		this.mLayoutResourceId = layoutResourceId;
		this.mContext = context;
		this.mCommentList = comments;
		this.mAdapter = this;
	}
	
	/*
	 * holder class
	 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	 * http://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html
	 */
	
	public static class Holder{
		CommentModel comment;
		//various widgets in the row view for 
		//CommentModel
	}
	
	// http://stackoverflow.com/questions/5177056/overriding-android-arrayadapter
	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	}*/
	
}
