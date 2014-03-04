package ca.ualberta.cs.team5geotopics.test;



import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.team5geotopics.BrowseView;
import ca.ualberta.cs.team5geotopics.CommentModel;

import com.example.team5geotopics.R;

public class BrowseTopLevelTests extends ActivityInstrumentationTestCase2<BrowseView> {
	Activity mActivity;
	Instrumentation mInstrumentation;
	Intent mStartIntent;
	ListView mTopLevelListView;
	ArrayAdapter<CommentModel> mAdapter;
	
	public BrowseTopLevelTests(){
		super(BrowseView.class);
	}
	
	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception{
		super.setUp();
		mActivity = getActivity();
		assertNotNull(mActivity);
		mInstrumentation = getInstrumentation();
		assertNotNull(mInstrumentation);
		mTopLevelListView = (ListView)mActivity.findViewById(R.id.browse_top_level_listView);
		assertNotNull(mTopLevelListView);
		//this give warning
		mAdapter = (ArrayAdapter<CommentModel>) mTopLevelListView.getAdapter();
		assertNotNull(mAdapter);
	}
	/*
	 * this tests to see if we add a new TopLevel Comment to the QueueController
	 * in list and push the list, then the TopLevel Comment appears in the ListView
	 * for BrowseTopLevelView. At this point in the development the Comment is text only. 
	 * 
	 * This test mocks the functionality of the Web Service, ie, no real internet stuff.
	 * 
	 * This test requires the field BROWSE_TOP_LEVEL_TEST in the GeoTopicsApplication
	 * to be true at compile time of the App.
	 * 
	 * If this field is true then a list of mock comments will be loaded
	 * into the QueueController mTopLvlQueue.mIn list (this is done
	 * in actual code), which should then 
	 * notify the BrowseTopLevelView, where the list of mock comments would 
	 * be added to the list of comments in the View. 
	 * 
	 *
	 * 
	 * We are testing the model/view framework and the display on the 
	 * ListView for the BrowseTopLevelView. 
	 */
	@UiThreadTest
	public void testCreateTopLevelAppearsOnlyTextNoWeb(){
		assertEquals("there should be three elements in the adapter", 3, mAdapter.getCount());
		
		View view;
		// as for right now I've just implemented 
		// checking the the author, title and the body views.
		for(int i = 0; i < 3; i++){
			//http://stackoverflow.com/questions/11541114/unittesting-of-arrayadapter
			view = mAdapter.getView(i, null, null);
			TextView author = (TextView) view
	                .findViewById(R.id.top_level_author_list_item);

	        TextView title = (TextView) view
	                .findViewById(R.id.top_level_title_list_item);

	        TextView body = (TextView) view
	                .findViewById(R.id.top_level_body_list_item);
	        
	        ViewAsserts.assertOnScreen(mTopLevelListView.getRootView(), view);
	        ViewAsserts.assertOnScreen(view.getRootView(), author);
	        ViewAsserts.assertOnScreen(view.getRootView(), title);
	        ViewAsserts.assertOnScreen(view.getRootView(), body);
	        
		}
		
	}
}
