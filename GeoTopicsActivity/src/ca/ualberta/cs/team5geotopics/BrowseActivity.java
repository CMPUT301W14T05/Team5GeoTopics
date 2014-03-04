package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class BrowseActivity extends Activity{
	
	private BrowseView myView;
	// Changed this to public static to test adding functionality
	public static CommentListModel clm;
	private ListView browseListView;
	private CommentModel viewingComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Setup the text box's
		//--------------------------------------------------------------
		//Get the current viewing comment
		viewingComment = GeoTopicsApplication.getCurrentViewingComment();
		//Find all the views we need to alter
		TextView title = (TextView)findViewById(R.id.browse_title);
		TextView commentTitle = (TextView) findViewById(R.id.comment_title);
		TextView commentBody = (TextView)findViewById(R.id.comment_body);
		//Find the 2 dividers
		View divider1 = (View)findViewById(R.id.divider1);
		View divider2 = (View)findViewById(R.id.divider2);
		//Find the Image
		ImageView image = (ImageView)findViewById(R.id.comment_image);
		
		if(viewingComment == null){
			//Disable all the views we don't need
			commentTitle.setVisibility(View.GONE);
			divider1.setVisibility(View.GONE);
			divider2.setVisibility(View.GONE);
			commentBody.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
		}else{
			title.setText("REPLIES");
			//Check and set the comment title. Not all comments have them.
			if(viewingComment.hasTitle()){
				commentTitle.setText((String)viewingComment.getmTitle());
			}else{
				commentTitle.setVisibility(View.GONE);
			}
			
			commentBody.setText(viewingComment.getmBody());
		}
		
		//Construct the model
		this.clm = new CommentListModel();
		
		//REMOVE THIS AFTER TESTING
		//Fill the model with test data
		ArrayList<CommentModel> replies = new ArrayList<CommentModel>();
		/* just made James' comment a reply to all comments (probably to itself as well, though I'm not actually sure how this compiles)*/
		replies.add(new CommentModel("This is a body", "James", "This is a much longer Title that will cut off ",replies)); 
		this.clm.add(replies.get(0));
		this.clm.add(new CommentModel("This is a my body", "Tyler", "This is a my Title",replies));
		this.clm.add(new CommentModel("This is a Really Really Really Really Really Really Really Really(8)" +
				" Really Really(10) Really Really(12) Really Really(14) Really Really Really Really Really Really(20) Really" +
				" Really Really Really Really Really Really Really Really Really Really" +
				" Really Really Really Really Really Really Really Really Really(40) Really Really Really Really Really Really" +
				" Really Really Really Really Really Really Really Really Really Really Really Really Really Really Really" +
				" Really(62) long body", "Matt", "Title, this is.",replies));
		//***********************************************************************************************************
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.top_level_list_item, clm.getList());
		//Register the view with the model
		this.clm.addView(this.myView);
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.browse_top_level_listView);
		browseListView.setAdapter(myView);
		
	}

	@Override
	protected void onResume(){
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				Intent intent = new Intent(BrowseActivity.this, BrowseActivity.class);
				GeoTopicsApplication.setCurrentViewingComment((CommentModel)browseListView.getItemAtPosition(position));
				startActivity(intent);
			}
			
		});
		super.onResume();
	}
	

		//Creates the options menu using the layout in menu.
		public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate the menu items for use in the action bar
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.browse_view, menu);    
		    return super.onCreateOptionsMenu(menu);
		}
		
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			// User clicks new comment button.
			case R.id.new_top_level_comment:
				Intent intent = new Intent(this, InspectCommentActivity.class);
				startActivity(intent);
				break;
			case R.id.action_sort:
				showDialog(0);
				break;
			default:
				return super.onOptionsItemSelected(item);
			}
		return true;
		}
		
		// This creates the dialog for taking sorting
		@Override
		protected Dialog onCreateDialog(int i) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			// User has selected sorting options
			if(i == 0){
			String options[] = new String[5];
			
			options[0] = "Sort by proximity to me";
			options[1] = "Sort by proximity to location";
			options[2] = "Sort by proximity to picture";
			options[3] = "Sort by scoring system";
			options[4] = "Sort by time";
			
			
			builder.setTitle("Select Option")
					.setItems(options, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which option to sort by?
							if(which == 0){ //Proximity to me
							}
							if(which == 1){ //Proximity to location
							}
							if(which == 2){ //Proximity to picture
							}
							if(which == 3){ //Scoring system
							}
							if(which == 4){ //Time
							}
						}
					});
			return builder.create();
		}
			return null;
		}
		
		
		
}
