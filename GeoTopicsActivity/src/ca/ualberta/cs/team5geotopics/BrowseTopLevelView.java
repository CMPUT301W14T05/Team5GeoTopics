package ca.ualberta.cs.team5geotopics;

import android.os.Bundle;
import android.widget.ListView;

import com.example.team5geotopics.R;

public class BrowseTopLevelView extends BrowseView implements AView<QueueModel>{
	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_top_level_view);
		
		// this is the model that we will be using to modify view
		QueueModel topLevelQ = GeoTopicsApplication.getQueueController().getmTopLvlQueue();
		// add this view to the list of views held in topLevelQ
		topLevelQ.addView(this);
		// update the view with Top Level Comments
		this.update(topLevelQ);
		
		// set up the ListView
		
		
		// now we set up the adapter for this listView
		
	}
	@Override
	public void update(QueueModel topLevelQ) {
		
		// this if statement will be true if we wish to test that the BrowseTopLevelView
		// will load the TopLevelComment(s) that are in the the in List for the QueueModel
		// that models comments being pushed and pulled from ElasticSearch.
		// essentially we want to have some TopLevelComments that we want to browse, but
		// we don't want to bother having to either: implement ElasticSearch, or go 
		// through the process of creating multiple fake comments in a cumbersome test.
		if(GeoTopicsApplication.BrowseTopLevelNoInternetTest()){
			for(int i = 1; i < 4; i++){
				// load up three TopLevelComments with null pictures and null location
				// tests only the text fields title, body, author.
				// TODO: set a constant date and check comment time in the BrowseTopLevelTests
				topLevelQ.addToIn(new TopLevelModel(null, "body" + Integer.valueOf(i).toString(),
													"title" + Integer.valueOf(i).toString(), 
													"author" + Integer.valueOf(i).toString(),
													null));
			}
		}
		/*
		 * I think ideally we would have the above code automatically handled by the 
		 * Queue controller, ie, we should only have to ever look inside the 
		 * topLevelQ.getIn() list to see if there are TopLevel Comments that need
		 * to be appended to the super.mCommentsList
		 */
		
		// now we have to add all of the  elements in the topLevelQ.getIn() 
		// to the super.mCommentsList
		super.getmCommentList().addAll(topLevelQ.getIn());
		
		// we may need to remove these comments from gopLevelQ.mIn
		
	}
	
	


}
