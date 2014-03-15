package ca.ualberta.cs.team5geotopics.test;

import io.searchbox.client.JestResult;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.Cache;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentSearch;
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
	}
	
	public void testPullTopLevel(){
		CommentListModel listModel = new CommentListModel();
		Cache cache = new Cache(mActivity.getApplicationContext());
		CommentSearch search = new CommentSearch(listModel, cache);
		
		Thread thread = search.pullTopLevel((BrowseActivity) mActivity);
		try{
			thread.join();
		} catch (InterruptedException e) {
			Log.w("EsTestPush", "Thread interrupt");
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
			Log.w("EsTestPush", "Thread interrupt");
		}
		
		JestResult result = search.returnResult();
		assertTrue("Result is not null", result != null);
		assertTrue("Result is successful", result.isSucceeded());
	}
}
