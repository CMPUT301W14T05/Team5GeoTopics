//package ca.ualberta.cs.team5geotopics.test;
//
//import java.util.ArrayList;
//
//import android.graphics.Bitmap;
//import android.test.ActivityInstrumentationTestCase2;
//import android.util.Log;
//import ca.ualberta.cs.team5geotopics.Cache;
//import ca.ualberta.cs.team5geotopics.CommentListModel;
//import ca.ualberta.cs.team5geotopics.CommentModel;
//import ca.ualberta.cs.team5geotopics.InspectCommentActivity;
//
//public class CacheTests extends
//		ActivityInstrumentationTestCase2<InspectCommentActivity> {
//
//	public CacheTests() {
//		super(InspectCommentActivity.class);
//	}
//
//	public void testAddCommentToHistoryCache() {
//		Cache cache = Cache.getInstance();
//		// make sure the cache is cleared
//		cache.clearHistory();
//		//Add a comment to the history
//		CommentModel comment = new CommentModel("1", "1", "body", "Author", Bitmap
//				.createBitmap(10, 10, Bitmap.Config.ARGB_8888), "title");
//		cache.addToHistory(comment);
//		
//		//Get the history
//		ArrayList<CommentModel> history = cache.getHistory();
//		//Make sure its inside the list the cache keeps
//		assertTrue("The comment is in the cache", history.contains(comment));
//	}
//	
//	public void testGetRepliesFromHistory(){
//		Cache cache = Cache.getInstance();
//		// make sure the cache is cleared
//		cache.clearHistory();
//		//Create some dummy comments
//		CommentModel topLevel = new CommentModel("1", "1", "Tbody1", "Author1", null, "title");
//		topLevel.setmEsID("1234");
//		
//		CommentModel replyLevelOne = new CommentModel("1", "1", "Rbody1", "Author1", null);
//		replyLevelOne.setmParentID(topLevel.getmEsID());
//		
//		CommentModel replyLevelTwo = new CommentModel("1", "1", "Rbody2", "Author2", null);
//		replyLevelTwo.setmParentID(topLevel.getmEsID());
//		
//		CommentModel replyLevelThree = new CommentModel("1", "1", "Rbody3", "Author3", null);
//		replyLevelThree.setmParentID("4567");
//		
//		cache.addToHistory(topLevel);
//		cache.addToHistory(replyLevelOne);
//		cache.addToHistory(replyLevelTwo);
//		cache.addToHistory(replyLevelThree);
//		
//		assertTrue("The history cache has 4 comments", cache.getHistory().size() == 4);
//		
//		//Make sure its empty
//		CommentListModel clm = new CommentListModel();
//		assertTrue("Comment List Model is empty", clm.getList().isEmpty());
//		
//		Thread thread = cache.getReplies(clm,topLevel);
//		
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			Log.w("Tests", "Thread interrupt");
//		}
//		
//		//Make sure our comment list model has only 2 items in it
//		assertTrue("Comment List Model has 2 items", clm.getList().size() == 2);
//		//make sure the clm has the 2 replies but not the 3rd on in it.
//		assertTrue("Comment List Model has reply Level 1 in it", clm.getList().contains(replyLevelOne));
//		assertTrue("Comment List Model has reply Level 2 in it", clm.getList().contains(replyLevelTwo));
//	}
//	
//	public void testGetTopLevelFromHistory(){
//		Cache cache = Cache.getInstance();
//		// make sure the cache is cleared
//		cache.clearHistory();
//		//Create some dummy comments
//		CommentModel topLevel = new CommentModel("1", "1", "Tbody1", "Author1", null, "title");
//		topLevel.setmEsID("1234");
//		
//		CommentModel replyLevelOne = new CommentModel("1", "1", "Rbody1", "Author1", null);
//		replyLevelOne.setmParentID(topLevel.getmEsID());
//		
//		CommentModel replyLevelTwo = new CommentModel("1", "1", "Rbody2", "Author2", null);
//		replyLevelTwo.setmParentID(topLevel.getmEsID());
//		
//		CommentModel replyLevelThree = new CommentModel("1", "1", "Rbody3", "Author3", null);
//		replyLevelThree.setmParentID("4567");
//		
//		cache.addToHistory(topLevel);
//		cache.addToHistory(replyLevelOne);
//		cache.addToHistory(replyLevelTwo);
//		cache.addToHistory(replyLevelThree);
//		
//		assertTrue("The history cache has 4 comments", cache.getHistory().size() == 4);
//		
//		//Make sure its empty
//		CommentListModel clm = new CommentListModel();
//		assertTrue("Comment List Model is empty", clm.getList().isEmpty());
//		
//		Thread thread = cache.getTopLevel(clm);
//		
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			Log.w("Tests", "Thread interrupt");
//		}
//		
//		//Make sure our comment list model has only 2 items in it
//		assertTrue("Comment List Model has one items", clm.getList().size() == 1);
//		//make sure the clm has the 2 replies but not the 3rd on in it.
//		assertTrue("Comment List Model has top Level comment in it", clm.getList().contains(topLevel));
//
//	}
//
//}
