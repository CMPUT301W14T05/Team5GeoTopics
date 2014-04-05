package ca.ualberta.cs.team5geotopics.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.ReplyLevelActivity;

import com.example.team5geotopics.R;

public class BrowseCommentRepliesTests extends
		ActivityInstrumentationTestCase2<ReplyLevelActivity> {

	public BrowseCommentRepliesTests() {
		super(ReplyLevelActivity.class);
	}

	public void testCommentView() {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {

					CommentModel comment = new CommentModel("1", "1", "Body",
							"Author", null, null);
					comment.setES("testParentId", "-1", "TopLevel");
					CommentModel viewingComment = new CommentModel("1", "1",
							"ViewingBody", "ViewingAuthor", null, null);
					viewingComment.setES("testReplyId", "testParentId", "ReplyLevel");

					Intent intent = new Intent();
					intent.putExtra("ViewingParent", comment.getmEsID());
					intent.putExtra("ViewingComment", viewingComment.getmEsID());
					setActivityIntent(intent);

					ReplyLevelActivity activity = getActivity();
					CommentListModel clm = activity.clm;

					clm.clearList();

					clm.add(comment);
					activity.update(comment);

					View view = activity.getWindow().getDecorView();

					// Find the views we want to assert exist
					// List view's
					TextView author = (TextView) activity
							.findViewById(R.id.top_level_author_list_item);
					TextView body = (TextView) activity
							.findViewById(R.id.top_level_body_list_item);
					TextView date = (TextView) activity
							.findViewById(R.id.top_level_date_list_item);
					TextView time = (TextView) activity
							.findViewById(R.id.top_level_time_list_item);
					ImageView picture = (ImageView) activity
							.findViewById(R.id.top_level_thumbnail);
					// Views for the currently expanded comment
					TextView viewingTitle = (TextView) activity
							.findViewById(R.id.reply_comment_title);
					TextView viewingBody = (TextView) activity
							.findViewById(R.id.reply_comment_body);
					ImageView viewingImage = (ImageView) activity
							.findViewById(R.id.reply_comment_image);
					// Assert the views show up on screen.
					ViewAsserts.assertOnScreen(view, author);
					ViewAsserts.assertOnScreen(view, body);
					ViewAsserts.assertOnScreen(view, date);
					ViewAsserts.assertOnScreen(view, time);
					ViewAsserts.assertOnScreen(view, picture);
					ViewAsserts.assertOnScreen(view, viewingTitle);
					ViewAsserts.assertOnScreen(view, viewingBody);
					ViewAsserts.assertOnScreen(view, viewingImage);

				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testCommentText() {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					CommentModel comment = new CommentModel("1", "1", "Body",
							"Author", null, null);
					comment.setES("testParentId", "-1", "TopLevel");
					CommentModel viewingComment = new CommentModel("1", "1",
							"ViewingBody", "ViewingAuthor", null, null);
					viewingComment.setES("testReplyId", "testParentId", "ReplyLevel");

					Intent intent = new Intent();
					intent.putExtra("ViewingParent", comment.getmEsID());
					intent.putExtra("ViewingComment", viewingComment.getmEsID());
					setActivityIntent(intent);

					ReplyLevelActivity activity = getActivity();
					CommentListModel clm = activity.clm;

					clm.clearList();
					clm.add(comment);
					activity.update(comment);

					

					// Find the views we want to assert their text
					TextView author = (TextView) activity
							.findViewById(R.id.top_level_author_list_item);
					TextView body = (TextView) activity
							.findViewById(R.id.top_level_body_list_item);
					// Views for the currently expanded comment
					TextView viewingBody = (TextView) activity
							.findViewById(R.id.reply_comment_body);
					// Assert the views show up on screen.
					assertEquals("Comment should have the right author",
							"Author", author.getText().toString());
					assertEquals("Comment should have the right body", "Body",
							body.getText().toString());
					assertEquals("Viewing comment should have the right body",
							"ViewingBody", viewingBody.getText().toString());
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
