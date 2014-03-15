package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.team5geotopics.R;


public class ReplyLevelActivity extends BrowseActivity implements AView<AModel> {
	private TextView title;
	private TextView body;
	private ImageView image;
	private View divider;
	private Activity me;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_level_activity);
		//Set the Viewing Comment
		Bundle b = getIntent().getExtras();
		viewingComment = b.getParcelable("ViewingComment");
		//Setup the Model
		//this.clm.setList(viewingComment.getReplies());
		
		//Get the singletons we may need.
		this.application = GeoTopicsApplication.getInstance();
		this.application.setContext(getApplicationContext());
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();
		this.mCache = Cache.getInstance();
		me = this;
		
		//Construct the model
		this.clm = new CommentListModel();
		
		//Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item, clm.getList());
		//Register the adapter view with the model
		this.clm.addView(this.myView);
		
		//Register myself with the viewing comment
		//TODO: Figure out why this is not working
		//this.viewingComment.addView(this);	
		
		//Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.reply_level_listView);
		browseListView.setAdapter(myView);
		
		//Find the Views
		title = (TextView)findViewById(R.id.reply_comment_title);
		body = (TextView)findViewById(R.id.reply_comment_body);
		image = (ImageView)findViewById(R.id.reply_comment_image);
		divider = (View)findViewById(R.id.reply_divider1);
		
		
		//Update myself
		this.update(viewingComment);
		
	}
	
	@Override
	protected void onResume(){
		handleCommentLoad();
		this.update(viewingComment);
		myView.notifyDataSetChanged(); //Ensure the view is up to date.
		
		browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myView, View view, int position,
					long arg3) {
				CommentModel selected = (CommentModel) browseListView
						.getItemAtPosition(position);
				//Add this to the cache
				//mCache.addToHistory(selected);
				Intent intent = new Intent(ReplyLevelActivity.this, ReplyLevelActivity.class);
				intent.putExtra("ViewingComment",selected);
				me.startActivity(intent);
			}
			
		});
		super.onResume();
	}
	
	public void update(AModel model) {
		this.myView.notifyDataSetChanged();
		Log.w("NewComment", "Notified list");
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

	public String getType(){
		return "ReplyLevel";
	}
}
