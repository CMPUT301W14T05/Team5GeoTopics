package ca.ualberta.cs.team5geotopics.test;

import org.w3c.dom.Comment;

import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;
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
import android.widget.ListView;

public class BrowseTopLevelTests extends ActivityInstrumentationTestCase2<BrowseTopLevelView> {
	Activity mActivity;
	Instrumentation mInstrumentation;
	Button mCreateNewComment;
	Intent mStartIntent;
	ListView mTopLevelListView;
	ArrayAdapter<Comment> mAdapter;
	
	public BrowseTopLevelTests(){
		super(BrowseTopLevelView.class);
	}
	
	protected void setUp(){
		super.setUp();
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
		mCreateNewComment = (Button)mActivity.findViewById(ca.ualberta.cs.team5geotopics.R.id.newCommentBtn);
		mTopLevelListView = (ListView)mActivity.findViewById(ca.ualberta.cs.team5geotopicsR.id.topLevelListView);
		mAdapter = mTopLevelListView.getAdapter();
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
	 * This test requires the field BROWSE_TOP_LEVEL_TEST  
	 * and requires BROWSE_TOP_LEVEL_NO_INTERNET 
	 * (Both of these fields should be in GeoTopicsApplication)
	 * to be true at compile time.
	 * 
	 * If these fields are true then a list of mock comments will be loaded
	 * to the QueueController and pushed to the CommentAdapter. During the run time
	 * of the activity. This way we can test that we can view TopLevel Comments
	 * without having to worry about WebService stuff, or tediously creating comments
	 * which is another test.
	 * The list should contain three comments created with this constructor:
	 * public Comment(String title, String body, String author, Location loc, Bitmap pic, String type)
	 * 
	 * i,e, comment1 = new Comment("test1", "body1", "author1", null, null, "TopLevel")
	 * and so on for comment2 and comment3. Then put/add this list where it would be if 
	 * these comments were just loaded off the web. We are testing to be sure that these comments
	 * will be loaded into the ArrayAdapter automatically through some type of notifyAll()/updateAll()
	 * mechanic. 
	 * 
	 * the
	 */
	@UiThreadTest
	public void testCreateTopLevelAppearsOnlyTextNoWeb(){
		//make sure that the adapter has three elements
		assertTrue(mAdapter.getCount() == 3);
		
		View view;
		// as for right now I've just implemented 
		// checking th the author, title and the body views.
		for(int i = 0; i < 3; i++){
			//http://stackoverflow.com/questions/11541114/unittesting-of-arrayadapter
			view = mAdapter.getView(i, null, null);
			TextView author = (TextView) view
	                .findViewById(ca.ualberta.cs.team5geotopics.R.id.topLevelAdapterAuthor);

	        TextView title = (TextView) view
	                .findViewById(ca.ualberta.cs.team5geotopics.R.id.topLevelAdapterTitle);

	        ImageView photo = (ImageView) view
	                .findViewById(ca.ualberta.cs.team5geotopics.R.id.topLevelAdapterPicture);
	        
	        //hopfully this will test to see that the view is in the adapter
	        ViewAsserts.assertOnScreen(mTopLevelListView, view);
	        
	        assertNotNull("View is null. ", view);
	        assertNotNull("Name TextView is null. ", author);
	        assertNotNull("Number TextView is null. ", title);
	        assertNotNull("Photo ImageView is null. ", photo);

	        assertEquals("Authors doesn't match.", "author" + Integer.toString(i), author.getText());
	        assertEquals("Numbers doesn't match.", "test" + Integer.toString(i),
	        		title.getText());
		}
		
	}
}
