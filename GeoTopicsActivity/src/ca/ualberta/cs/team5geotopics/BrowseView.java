package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import com.example.team5geotopics.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public abstract class BrowseView extends Activity implements AView<Cache> {
	ListView mListView;

	// Not sure if we need this, might have been moved to the comment list model
	// ***************************************************************************
	private ArrayList<CommentModel> mCommentList;

	public ArrayList<CommentModel> getmCommentList() {
		return mCommentList;
	}

	public void setmCommentList(ArrayList<CommentModel> mCommentList) {
		this.mCommentList = mCommentList;
	}

	public void initCommentList() {
		mCommentList = new ArrayList<CommentModel>();
	}

	// ***************************************************************************

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCommentList();

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// this is the model that we will be using to modify view
		Cache cache = GeoTopicsApplication.getCache();
		// add this view to the list of views held in the cache
		cache.addView(this);

		// set up the ListView
		setUpListView();

		// now we set up the adapter for this listView
		setUpAdapter();

		// now we setUp an Item Listener on ListView
		// so that user can click a TopLevel Comment
		// in order to view replies
		setUpItemListener();
	}

	public void update(Cache cache) {

	}

	// http://stackoverflow.com/questions/4709870/setonitemclicklistener-on-custom-listview
	private void setUpItemListener() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				/*
				 * launch another browse view to browse the comment
				 */

			}

		});

	}

	private void setUpListView() {
		mListView = (ListView) findViewById(R.id.browse_top_level_listView);

	}

	//TODO: This code needs to be re-worked
	private void setUpAdapter() {
		BrowseCommentController controller = GeoTopicsApplication
				.getTopLevelController(this, R.layout.top_level_list_item,
						getmCommentList());
		CommentListAdapter adapter = controller.getAdapter();
		mListView.setAdapter(adapter);

	}
	
	//Creates the options menu using the layout in menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		   // Inflate the menu items for use in the action bar
		   MenuInflater inflater = getMenuInflater();
		   inflater.inflate(R.menu.browse_view, menu);
		   return super.onCreateOptionsMenu(menu);
	}

}
