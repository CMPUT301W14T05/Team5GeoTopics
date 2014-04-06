package ca.ualberta.cs.team5geotopics.test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentManager;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.MyBookmarksActivity;
import ca.ualberta.cs.team5geotopics.User;
import ca.ualberta.cs.team5geotopics.UserController;

public class BookmarksTests extends ActivityInstrumentationTestCase2<MyBookmarksActivity> {

	public BookmarksTests(){
		super(MyBookmarksActivity.class);
	}
	
	public void testSaveCommentAsBookmark(){
		UserController uController = new UserController();
		CommentManager manager = CommentManager.getInstance();
		User mUser = User.getInstance();
		CommentModel mComment;
		CommentModel retrievedComment;
		
		mComment = new CommentModel("1", "1", "Body", "Anon" , "Title",  null, "AID1234");
		mComment.setmParentID("werfdws123");
		mComment.setmEsID("wetvwre908");
		mComment.setmEsType("ReplyLevel");
		
		manager.newReply(mComment);
		
		assertTrue("Comment is not in bookmarks list", mUser.inBookmarks(mComment) == false);
		uController.bookmark(mComment); //Adds it to the list
		assertTrue("Comment is now in bookmarks list", mUser.inBookmarks(mComment) == true);
		
		//Search the cache for the comment we just added to the bookmarks list
		retrievedComment = manager.getComment(mComment.getmParentID(), mComment.getmEsID());
		assertTrue("Comment is in the cache", mComment.getmEsID().equals(retrievedComment.getmEsID()));
		
		uController.bookmark(mComment); //Removes it from the list
		
	}
}
