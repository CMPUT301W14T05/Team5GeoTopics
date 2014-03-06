package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;


public class ReplyLevelActivity extends BrowseActivity implements AView<CommentModel>
{
	private TextView title;
	private TextView body;
	private ImageView image;
	private View divider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_level_activity);

		// Remove the title and logo from the action bar
		// TODO: Look for a better way to do this, this feels like a hack.
		// Has to be a better way to do this in xml. (James)
		getActionBar().setDisplayShowTitleEnabled(false);
		// Gives us the left facing caret. Need to drop the app icon however OR
		// change it to something other than the android guy OR remove software back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		

		//Get the current viewing comment
		application = GeoTopicsApplication.getInstance();
		viewingComment = application.getCurrentViewingComment();
		//Construct the model
		this.clm = new CommentListModel();
		this.clm.setList(viewingComment.getReplies());
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item, clm.getList());
		//Register the adapter view with the model
		this.clm.addView(this.myView);
		//Register myself with the viewing comment
		this.viewingComment.addView(this);	
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.reply_level_listView);
		browseListView.setAdapter(myView);
		
		//Find the Views
		title = (TextView)findViewById(R.id.reply_comment_title);
		body = (TextView)findViewById(R.id.reply_comment_body);
		image = (ImageView)findViewById(R.id.reply_comment_image);
		divider = (View)findViewById(R.id.reply_divider1);
		
		this.update(viewingComment);
		
	}
	
	@Override
	protected void onResume(){
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				Intent intent = new Intent(ReplyLevelActivity.this, ReplyLevelActivity.class);
				application.setCurrentViewingComment((CommentModel)browseListView.getItemAtPosition(position));
				startActivity(intent);
			}
			
		});
		super.onResume();
	}
	
	public void update(CommentModel comment) {
		if(viewingComment.isTopLevel()) {
			title.setText(viewingComment.getmTitle());
		}else{
			title.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
		}
		
		body.setText(viewingComment.getmBody());
		if(viewingComment.hasPicture()){
			image.setImageBitmap(viewingComment.getPicture());
		}else{
			image.setVisibility(View.GONE);
		}
	}
}
