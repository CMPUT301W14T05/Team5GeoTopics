package ca.ualberta.cs.team5geotopics;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.team5geotopics.R;

public class BrowseTopLevelView extends BrowseView{
	
	ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_top_level_view);
		
		// this is the model that we will be using to modify view
		QueueModel topLevelQ = GeoTopicsApplication.getQueueController().getmTopLvlQueue();
		// add this view to the list of views held in topLevelQ
		topLevelQ.addView(this);
		
		
		
		// this if statement will be true if we wish to test that the BrowseTopLevelView
		// will load the TopLevelComment(s) that are in the the in List for the QueueModel
		// that models comments being pushed and pulled from ElasticSearch.
		// essentially we want to have some TopLevelComments that we want to browse, but
		// we don't want to bother having to either: implement ElasticSearch, or go 
		// through the process of creating multiple fake comments in a cumbersome test.
		if(GeoTopicsApplication.BrowseTopLevelNoInternetTest()){
			topLevelQ.testInit();
		}
		
		// update the view with Top Level Comments
		this.update(topLevelQ);
		
		// set up the ListView
		setUpListView();
		
		// now we set up the adapter for this listView
		setUpAdapter();
		
		// now we setUp an Item Listener on ListView
		// so that user can click a TopLevel Comment
		// in order to view replies
		setUpItemListener();
	}
	
	
	



	@Override
	public void update(QueueModel topLevelQ) {
		
		// now we have to add all of the  elements in the topLevelQ.getIn() 
		// to the super.mCommentsList
		super.getmCommentList().addAll(topLevelQ.getIn());
		
		// we may need to remove these comments from gopLevelQ.mIn
		
	}
	
	private void setUpListView() {
		mListView = (ListView) findViewById(R.id.browse_top_level_listView);
		
	}
	
	/*
	 * In this method we register the ListView with an adapter (or vice-versa w/e).
	 */
	private void setUpAdapter() {
		BrowseCommentController controller = GeoTopicsApplication.getTopLevelController(this, 
				                             R.layout.top_level_list_item, getmCommentList());
		CommentListAdapter adapter = controller.getAdapter();
		mListView.setAdapter(adapter);
		
	}
	
	//http://stackoverflow.com/questions/4709870/setonitemclicklistener-on-custom-listview
	private void setUpItemListener() {
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				/*
				 * launch BrowseCommentReplies
				 */
				
			}
			
		});
		
	}

}
