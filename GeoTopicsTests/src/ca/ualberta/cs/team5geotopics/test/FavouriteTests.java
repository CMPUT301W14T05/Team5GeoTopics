package ca.ualberta.cs.team5geotopics.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cs.team5geotopics.CommentManager;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.MyFavouritesActivity;
import ca.ualberta.cs.team5geotopics.User;
import ca.ualberta.cs.team5geotopics.UserController;

import com.example.team5geotopics.R;

public class FavouriteTests extends ActivityInstrumentationTestCase2<MyFavouritesActivity> {
	
	public FavouriteTests(){
		super(MyFavouritesActivity.class);
	}
	
	public void testSaveCommentAsFavourit(){
		UserController uController = new UserController();
		CommentManager manager = CommentManager.getInstance();
		User mUser = User.getInstance();
		CommentModel mComment;
		
		mComment = new CommentModel("1", "1", "Body", "Anon" , "Title",  null, "AID1234");
		mComment.setmParentID("werfdws123");
		mComment.setmEsID("wetvwre908");
		mComment.setmEsType("ReplyLevel");
		
		manager.newReply(mComment);
		
		assertTrue("Comment is not in favourites list", mUser.inFavourites(mComment) == false);
		uController.favourite(mComment); //Adds it to the list
		assertTrue("Comment is now in favourties list", mUser.inFavourites(mComment) == true);
		uController.favourite(mComment); //Removes it from the list
	}
	
	public void testFavouritView() {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {

					UserController uController = new UserController();
					CommentManager manager = CommentManager.getInstance();
					User mUser = User.getInstance();
					CommentModel mComment;
					
					mComment = new CommentModel("1", "1", "Body", "Anon" , "Title",  null, "AID1234");
					mComment.setmParentID("werfdws123");
					mComment.setmEsID("wetvwre908");
					mComment.setmEsType("ReplyLevel");
					
					manager.newReply(mComment);
				
					uController.favourite(mComment); //Adds it to the list

					Intent intent = new Intent();
					setActivityIntent(intent);

					MyFavouritesActivity activity = getActivity();
					
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
					// Assert the views show up on screen.
					ViewAsserts.assertOnScreen(view, author);
					ViewAsserts.assertOnScreen(view, body);
					ViewAsserts.assertOnScreen(view, date);
					ViewAsserts.assertOnScreen(view, time);
					ViewAsserts.assertOnScreen(view, picture);
					
					uController.favourite(mComment); //Removes it from the list

				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testFavouritText() {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {

					UserController uController = new UserController();
					CommentManager manager = CommentManager.getInstance();
					User mUser = User.getInstance();
					CommentModel mComment;
					
					mUser.clearLocalMyFavrouties();
					
					mComment = new CommentModel("1", "1", "Body", "Anon" , "Title",  null, "AID1234");
					mComment.setmParentID("werfdws123");
					mComment.setmEsID("wetvwre908");
					mComment.setmEsType("ReplyLevel");
					
					manager.newReply(mComment);
				
					uController.favourite(mComment); //Adds it to the list

					Intent intent = new Intent();
					setActivityIntent(intent);

					MyFavouritesActivity activity = getActivity();
					
					View view = activity.getWindow().getDecorView();

					// Find the views we want to assert their text
					TextView author = (TextView) activity
							.findViewById(R.id.top_level_author_list_item);
					TextView body = (TextView) activity
							.findViewById(R.id.top_level_body_list_item);
					// Views for the currently expanded comment
					// Assert the views show up on screen.
					assertEquals("Comment should have the right author",
							"Anon", author.getText().toString());
					assertEquals("Comment should have the right body", "Body",
							body.getText().toString());
					
					uController.favourite(mComment); //Removes it from the list

				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
