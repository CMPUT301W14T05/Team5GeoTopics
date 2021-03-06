package ca.ualberta.cs.team5geotopics;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team5geotopics.R;

/**
 * The view you see when you go to view the replies once you click a top level
 * comment and click a reply. It also updates replies as needed.
 */

public class ReplyLevelActivity extends BrowseActivity implements AView<AModel> {
	private TextView title;
	private TextView body;
	private ImageView image;
	private View divider;
	private String viewingParent;
	private String viewingID;
	private MenuItem favouriteItem;
	private TextView author;
	private TextView date;
	private TextView time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_level_activity);

		// Get the singletons we may need.
		this.application = GeoTopicsApplication.getInstance();
		this.myUser = User.getInstance();
		this.manager = CommentManager.getInstance();
		this.uController = new UserController();
		me = this;
		favouriteItem = (MenuItem) findViewById(R.id.action_favourite);

		b = getIntent().getExtras();
		Log.w("ReplyLevel", b.getString("ViewingParent"));
		Log.w("ReplyLevel", b.getString("ViewingComment"));
		viewingParent = b.getString("ViewingParent");
		viewingID = b.getString("ViewingComment");

		// Construct the model
		this.clm = new CommentListModel();

		// Construct the View
		this.myView = new BrowseView(this, R.layout.comment_list_item,
				clm.getList());
		// Register the adapter view with the model
		this.clm.addView(this.myView);
		// Register with the manager
		this.manager.addView(this);

		// Attach the list view to myView
		browseListView = (ListView) findViewById(R.id.reply_level_listView);
		browseListView.setAdapter(myView);

		// Find the Views
		title = (TextView) findViewById(R.id.reply_comment_title);
		body = (TextView) findViewById(R.id.reply_comment_body);
		image = (ImageView) findViewById(R.id.reply_comment_image);
		divider = (View) findViewById(R.id.reply_divider1);
		author = (TextView) findViewById(R.id.reply_author);
		date = (TextView) findViewById(R.id.reply_date);
		time = (TextView) findViewById(R.id.reply_time);

		// This takes us to the view profile screen
		author.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (application.isNetworkAvailable()) {
					Intent intent = new Intent(ReplyLevelActivity.this,
							InspectOtherProfilesActivity.class);
					Log.w("ProfileSearch",
							"Putting ID in: " + viewingComment.getAuthorID());
					intent.putExtra("ProfileID", viewingComment.getAuthorID());
					startActivity(intent);
				}else{
					Toast.makeText(me, "Unable to view profile without internet, Sorry!", 3).show();
				}
			}
		});
		
		webConnectionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (application.isNetworkAvailable()) {
					Log.w("Connectivity", "Have network");
					Log.w("Connectivity", "Refreshing an activity");
					manager.refresh(clm, me, viewingComment);
				}
			}
		};

	}

	@Override
	protected void onResume() {
		// Refresh our view
		viewingComment = this.manager.getComment(viewingParent, viewingID);
		manager.refresh(this.clm, this, viewingComment);
		viewingComment = this.manager.getComment(b.getString("ViewingParent"),
				b.getString("ViewingComment"));
		this.updateViewingComment(viewingComment);
		this.myView.notifyDataSetChanged();
		invalidateOptionsMenu();
		// Setup the listeners
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> myView, View view,
							int position, long arg3) {
						CommentModel selected = (CommentModel) browseListView
								.getItemAtPosition(position);
						if (!bookmark) {
							Intent intent = new Intent(ReplyLevelActivity.this,
									ReplyLevelActivity.class);
							intent.putExtra("ViewingComment",
									selected.getmEsID());
							intent.putExtra("ViewingParent",
									selected.getmParentID());
							uController.readingComment(selected);
							startActivity(intent);
						} else {
							uController.bookmark(selected);
							update(null);
						}
					}

				});
		
		registerReceiver(webConnectionReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		super.onResume();
	}

	public void update(AModel model) {
		this.myView.notifyDataSetChanged();
	}

	/**
	 * @return "ReplyLevel" The type of comment it is.
	 */
	public String getType() {
		return "ReplyLevel";
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(webConnectionReceiver);
		super.onPause();
	}

	private void updateViewingComment(CommentModel comment) {
		if (comment.getmEsID().equals(viewingComment.getmEsID())) {
			Log.w("ReplyLevel", "Updating viewing comment");
			Log.w("ReplyLevel", viewingComment.getmBody());
			Log.w("ReplyLevel", comment.getmBody());
			viewingComment = comment;
			if (viewingComment.isTopLevel()) {
				title.setText(viewingComment.getmTitle());
			} else {
				title.setVisibility(View.GONE);
				divider.setVisibility(View.GONE);
			}

			body.setText(viewingComment.getmBody());
			if (viewingComment.hasPicture()) {
				image.setImageBitmap(viewingComment.getPicture());
			} else {
				image.setVisibility(View.GONE);
			}
			Date date = comment.getDate();
			DateFormat dateFormat = android.text.format.DateFormat
					.getDateFormat(application.getContext());
			DateFormat timeFormat = android.text.format.DateFormat
					.getTimeFormat(application.getContext());

			author.setText("By " + viewingComment.getmAuthor());
			this.date.setText(dateFormat.format(date));
			time.setText(timeFormat.format(date));

		}
	}
}
