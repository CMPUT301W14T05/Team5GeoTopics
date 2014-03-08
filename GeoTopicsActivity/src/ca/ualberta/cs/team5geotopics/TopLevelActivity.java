package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.team5geotopics.R;

public class TopLevelActivity extends BrowseActivity {
	private CommentListController commentListController;
	private IntentFilter topLevelFilter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_level_activity);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get the application
		application = GeoTopicsApplication.getInstance();
		
		//Construct the model
		this.clm = new CommentListModel();
		this.mCache = Cache.getInstance();
		
		//Set my view to the history cache
		//This is a temporary fix
		this.clm.setList(mCache.getHistory());
		//setting up controller
		this.topLevelFilter = new IntentFilter(CommentListController.RECIEVE_TOP_LEVEL_COMMENTS);
		this.topLevelFilter.addCategory(Intent.CATEGORY_DEFAULT);
		this.commentListController = new CommentListController();
		commentListController.addModel(this.clm);
		
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item, clm.getList());
		//Register the view with the model
		this.clm.addView(this.myView);
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
		
		GetTopLevel.getAll(getApplicationContext());
	}
	
	@Override
	protected void onResume(){
		//Reset the current viewing comment
		application.setCurrentViewingComment(viewingComment);
		myView.notifyDataSetChanged(); //Ensure the view is up to date.

		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				Intent intent = new Intent(TopLevelActivity.this, ReplyLevelActivity.class);
				application.setCurrentViewingComment((CommentModel)browseListView.getItemAtPosition(position));
				startActivity(intent);
			}
			
		});
		registerReceiver(commentListController, topLevelFilter);
		super.onResume();
	}

	
	@Override
	protected void onPause() {
		unregisterReceiver(commentListController);
		super.onPause();
	}
	public class CommentListController extends BroadcastReceiver {
		public static final String RECIEVE_TOP_LEVEL_COMMENTS = "ca.ualberta.cs.team5geotopics.ACTIONS.RECIEVE_COMMENTS";
		private static final String TOP_LEVEL_KEY = "NEW_TOP_LEVEL";
		private CommentListModel browseModel;

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.w("CommentListController", "in onRecieve");

			Bundle bundle = intent.getExtras();
			ArrayList<CommentModel> newTopLevel = bundle
					.getParcelableArrayList(TOP_LEVEL_KEY);
			Log.w("CommentListController", Integer.valueOf(newTopLevel.size())
					.toString());
			browseModel.refreshAddAll(newTopLevel);

		}

		public void addModel(CommentListModel browseModel) {
			Log.w("CommentListController", "adding model to controller");
			this.browseModel = browseModel;
		}
	}
}
