package ca.ualberta.cs.team5geotopics.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentManager;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.ReplyLevelActivity;

public class InternetReconnectTests extends ActivityInstrumentationTestCase2<ReplyLevelActivity>  {
	
	public InternetReconnectTests() {
		super(ReplyLevelActivity.class);
	}
	
	/**
	 * This test is testing the functionality of loading comments on internet re-connect.
	 * Since the real app relies on the broadcast receivers to tell an activity when network
	 * connectivity changes this is tough to test. Thus we are going to assume that the broadcast
	 * Receiver works as it is android code and test the refresh method which is called when
	 * we re-gain Internet in order to get comments. 
	 * 
	 * Please ensure phone Internet is turned on for this test as it should work best
	 * that way.
	 */
	public void testRefreshOnInternetReconnect(){
		
		CommentManager manager = CommentManager.getInstance();
		
		CommentModel comment = new CommentModel("1", "1", "Body",
				"Author", null, null);
		comment.setmParentID("NoParent");
		comment.setmEsID("Random1");
		comment.setmEsType("ReplyLevel");
		
		manager.newReply(comment);
		
		CommentModel viewingComment = new CommentModel("1", "1",
				"ViewingBody", "ViewingAuthor", null, null);
		viewingComment.setmParentID(comment.getmEsID());
		viewingComment.setmEsID("Random2");
		viewingComment.setmEsType("ReplyLevel");
		
		manager.newReply(viewingComment);
		
		CommentModel replyVComment = new CommentModel("1", "1",
				"replyVCommentBody", "replyVCommentAuthor", null, null);
		
		replyVComment.setmParentID(viewingComment.getmEsID());
		replyVComment.setmEsID("Random3");
		replyVComment.setmEsType("ReplyLevel");
		
		
		manager.newReply(replyVComment);

		Intent intent = new Intent();
		intent.putExtra("ViewingParent", comment.getmEsID());
		intent.putExtra("ViewingComment", viewingComment.getmEsID());
		setActivityIntent(intent);

		ReplyLevelActivity activity = getActivity();
		CommentListModel clm = activity.clm;

		clm.clearList();
		
		assertTrue("Activities clm is currently empty", clm.getList().size() == 0);
		
		manager.refresh(clm, activity, viewingComment);
		
		//if it does then we assume the refresh pulled the comment properly
		assertTrue("Activities clm has 1 comment in it", clm.getList().size() == 1);
		
	}

}
