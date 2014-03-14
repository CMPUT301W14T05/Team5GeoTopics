package ca.ualberta.cs.team5geotopics.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.TopLevelActivity;

import com.example.team5geotopics.R;

public class BrowseTopLevelTests extends ActivityInstrumentationTestCase2<TopLevelActivity> {
	
	public BrowseTopLevelTests(){
		super(TopLevelActivity.class);
	}
	
	public void testCommentView(){
		try {
			runTestOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 
			    	TopLevelActivity activity = getActivity();
			 		CommentListModel clm = activity.clm;
			 		
			 		clm.clearList();
			 		CommentModel comment = new CommentModel("1", "1", "Body", "Author", null, "Title");
			 		clm.add(comment);
			 		activity.update(comment);
			 		
			 		View view = activity.getWindow().getDecorView();
			 		
			 		//Find the views we want to assert exist
			 		TextView title = (TextView)activity.findViewById(R.id.top_level_title_list_item);
			 		TextView author = (TextView)activity.findViewById(R.id.top_level_author_list_item);
			 		TextView body = (TextView)activity.findViewById(R.id.top_level_body_list_item);
			 		TextView date = (TextView)activity.findViewById(R.id.top_level_date_list_item);
			 		TextView time = (TextView)activity.findViewById(R.id.top_level_time_list_item);
			 		ImageView picture = (ImageView)activity.findViewById(R.id.top_level_thumbnail);
			 		//Assert the views show up on screen.
			 		ViewAsserts.assertOnScreen(view, title);
			 		ViewAsserts.assertOnScreen(view, author);
			 		ViewAsserts.assertOnScreen(view, body);
			 		ViewAsserts.assertOnScreen(view, date);
			 		ViewAsserts.assertOnScreen(view, time);
			 		ViewAsserts.assertOnScreen(view, picture);
			    }
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void testCommentText(){
		try {
			runTestOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	TopLevelActivity activity = getActivity();
			 		CommentListModel clm = activity.clm;
			 		
			 		clm.clearList();
			 		CommentModel comment = new CommentModel("1", "1", "Body", "Author", null, "Title");
			 		clm.add(comment);
			 		activity.update(comment);
			 		
			 		View view = activity.getWindow().getDecorView();
			 		
			 		//Find the views we want to assert their text
			 		TextView title = (TextView)activity.findViewById(R.id.top_level_title_list_item);
			 		TextView author = (TextView)activity.findViewById(R.id.top_level_author_list_item);
			 		TextView body = (TextView)activity.findViewById(R.id.top_level_body_list_item);
			 		//Assert the views show up on screen.
			 		assertEquals("Comment should have the right title",  "Title", title.getText().toString());
			 		assertEquals("Comment should have the right author",  "Author", author.getText().toString());
			 		assertEquals("Comment should have the right body",  "Body", body.getText().toString());
			    }
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
