package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.Cache;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentSearch;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
import ca.ualberta.cs.team5geotopics.TopLevelActivity;


public class EsTestsB extends ActivityInstrumentationTestCase2<TopLevelActivity> {
	private Activity mActivity;
	
	public EsTestsB() {
		super(TopLevelActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		application.setContext(mActivity);
	}
	
	public void testPullTopLevel(){
		CommentListModel listModel = new CommentListModel();
		Cache cache = Cache.getInstance();
		CommentSearch search = new CommentSearch(listModel, cache);
		
		
		Thread thread = search.pullTopLevel((BrowseActivity) mActivity);
		try{
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPullTopLevel", "Thread interrupt");
		}
		
		JestResult result = search.returnResult();
		assertTrue("Result is not null", result != null);
		assertTrue("Result is successful", result.isSucceeded());
	}
	
	public void testPullReplies(){
		CommentListModel listModel = new CommentListModel();
		CommentSearch search = new CommentSearch(listModel);
		
		Thread thread = search.pullReplies((BrowseActivity) mActivity, "test id");
		try{
			thread.join();
		} catch (InterruptedException e){
			Log.w("EsTestPullReply", "Thread interrupt");
		}
		
		JestResult result = search.returnResult();
		assertTrue("Result is not null", result != null);
		assertTrue("Result is successful", result.isSucceeded());
	}
	
	public void testPullSingleCommentTopLevel(){
		
		CommentListModel listModel = new CommentListModel();
		CommentSearch search = new CommentSearch(listModel);
		
		Thread thread = search.pullComment("test id", "TopLevel");
		try{
			thread.join();
		}
		catch (InterruptedException e){
			Log.w("EsTestPullSingleTopLevel", "Thread interrupt");
		}
		
		JestResult result = search.returnResult();
		assertTrue("Result is not null", result != null);
		assertTrue("Result is successful", result.isSucceeded());
	}
	
	public void testPullSingleCommentReplyLevel(){
		
		CommentListModel listModel = new CommentListModel();
		CommentSearch search = new CommentSearch(listModel);
		
		Thread thread = search.pullComment("test id", "ReplyLevel");
		try{
			thread.join();
		}
		catch (InterruptedException e){
			Log.w("EsTestPullSingleTopLevel", "Thread interrupt");
		}
		
		JestResult result = search.returnResult();
		assertTrue("Result is not null", result != null);
		assertTrue("Result is successful", result.isSucceeded());
	}
}
