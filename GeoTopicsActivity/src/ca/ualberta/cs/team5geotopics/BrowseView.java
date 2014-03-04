package ca.ualberta.cs.team5geotopics;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class BrowseView  extends ArrayAdapter<CommentModel> implements AView<CommentListModel> {
	
	private List<CommentModel> mCommentList;
	private int mLayoutResourceId;
	private Context mContext;
	private DateFormat dateFormat; 
	private DateFormat timeFormat;
	
	public BrowseView(Context context, int layoutResourceId, List<CommentModel> mCommentList){
		super(context, layoutResourceId, mCommentList);
		this.mLayoutResourceId = layoutResourceId;
		this.mContext = context;
		this.mCommentList = mCommentList;
		this.dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
		this.timeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
	}
	
	/*
	 * holder class
	 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	 * http://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html
	 */
	
	public static class Holder{
		CommentModel comment;
		TextView title;
		TextView author;
		TextView body;
		TextView date;
		TextView time;
	}
	
	// http://stackoverflow.com/questions/5177056/overriding-android-arrayadapter
	// http://blog.ghatasheh.com/2012/11/android-array-adapter-viewholder.html
	
	/*
	 * This method returns the view associated with the row of the ListView the adapter 
	 * is registered to. Basically it fills our ListView with the appropriate widgets.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		boolean isTopLevel = false;
		View view = convertView;
		Holder holder = null;
		if(mCommentList.get(position).isTopLevel()){
			isTopLevel = true;
		}
		// we need to call the findViewById to get the views
		if(view == null){
			if(isTopLevel){
				// fill row with TopLevelComment layout
				view = LayoutInflater.from(mContext).inflate(R.layout.top_level_list_item,
															null, false);
				holder = new Holder();
				holder.comment = mCommentList.get(position);
				holder.title = (TextView)view.findViewById(R.id.top_level_title_list_item);
				holder.author = (TextView)view.findViewById(R.id.top_level_author_list_item);
				holder.body = (TextView)view.findViewById(R.id.top_level_body_list_item);
				holder.date = (TextView)view.findViewById(R.id.top_level_date_list_item);
				holder.time = (TextView)view.findViewById(R.id.top_level_time_list_item);
				view.setTag(holder);
			}
			else{
				//here we can set view to a reply_list_item or w/e
			}
		}
		// we don't need to call findViewById to get views, because we already did.
		else{
			holder = (Holder) view.getTag();
		}
		CommentModel comment = mCommentList.get(position);
		Date date = comment.getDate();
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
		holder.title.setText(comment.getmTitle());
		holder.body.setText(comment.getmBody());
		holder.author.setText("By " + comment.getmAuthor());
		holder.date.setText(dateFormat.format(date));
		holder.time.setText(timeFormat.format(date));
		
		return view;
		
	}

	@Override
	public void update(CommentListModel model) {
		this.notifyDataSetChanged();
	}
	
}
