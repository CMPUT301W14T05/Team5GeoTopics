package ca.ualberta.cs.team5geotopics.test;



import com.example.team5geotopics.R;

import ca.ualberta.cs.team5geotopics.BrowseTopLevelView;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.StartActivity;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BrowseTopLevelTests extends ActivityInstrumentationTestCase2<BrowseTopLevelView> {
	Activity mActivity;
	Instrumentation mInstrumentation;
	Button mCreateNewComment;
	Intent mStartIntent;
	ListView mTopLevelListView;
	ArrayAdapter<CommentModel> mAdapter;
	
	public BrowseTopLevelTests(){
		super(BrowseTopLevelView.class);
	}
	
	protected void setUp(){
		super.setUp();
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
		mCreateNewComment = (Button)mActivity.findViewById(R.id.browse_top_lvl_new_cmnt);
		mTopLevelListView = (ListView)mActivity.findViewById(R.id.top_lvl_listView);
		mAdapter = (ArrayAdapter<CommentModel>) mTopLevelListView.getAdapter();
	}
	
	public final void testPreConditions(){
		assertNotNull(mActivity);
		assertNotNull(mInstrumentation);
		assertNotNull(mCreateNewComment);
		assertNotNull(mTopLevelListView);
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
		//make sure that the adapter has three elements
		assertTrue(mAdapter.getCount() == 3);
		
		View view;
		// as for right now I've just implemented 
		// checking th the author, title and the body views.
		for(int i = 1; i < 4; i++){
			//http://stackoverflow.com/questions/11541114/unittesting-of-arrayadapter
			view = mAdapter.getView(i, null, null);
			TextView author = (TextView) view
	                .findViewById(R.id.top_level_adapter_author);

	        TextView title = (TextView) view
	                .findViewById(R.id.top_level_adapter_title);

	        ImageView photo = (ImageView) view
	                .findViewById(R.id.top_level_adapter_picture);
	        
	        //Hopefully this will test to see that the view is in the adapter
	        ViewAsserts.assertOnScreen(mTopLevelListView, view);
	        
	        assertNotNull("View is null. ", view);
	        assertNotNull("Name TextView is null. ", author);
	        assertNotNull("Number TextView is null. ", title);
	        assertNotNull("Photo ImageView is null. ", photo);

	        assertEquals("Authors doesn't match.", "author" + Integer.toString(i), author.getText());
	        assertEquals("Titles doesn't match.", "test" + Integer.toString(i),
	        		title.getText());
		}
		
	}
}
