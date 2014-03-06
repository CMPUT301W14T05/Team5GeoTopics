package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.team5geotopics.R;


public class EsTestActivity extends Activity {
	protected TextView textView;
	private Button putIndexBtn;
	private Button createIndexBtn;
	private Button getTopLevelBtn;
	private CommentReciever commentReciever;
	protected ArrayList<CommentModel> commentList;
	private IntentFilter filter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_es_test);
		textView = (TextView) findViewById(R.id.es_test_textview);
		textView.setText("blank");
		// registerReceivers();
		setupBtns();
		filter = new IntentFilter(CommentReciever.ACCEPT_COMMENTS);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		commentReciever = new CommentReciever();
		registerReceiver(commentReciever, filter);
		commentList = new ArrayList<CommentModel>();
	}

	private void setupBtns() {
		putIndexBtn = (Button) findViewById(R.id.es_test_put_index_btn);
		final Context context = this;

		putIndexBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				CommentModel comment = new CommentModel(null, "body", "author",
						null, "title");
				PutIndexService.pushComment(getApplicationContext(), "TopLevel", comment);

			}
		});

		createIndexBtn = (Button) findViewById(R.id.es_test_create_index_btn);
		createIndexBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = "cats";
				CreateIndexService.createIndex(getApplicationContext(), name);
			}
		});
		
		getTopLevelBtn = (Button) findViewById(R.id.es_get_top_level);
		getTopLevelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetAllService.getAll(getApplicationContext());
			}
		});
	}
	
	public class CommentReciever extends BroadcastReceiver{
		public static final String ACCEPT_COMMENTS = "cs.ualberta.cs.team5geotopics.ACCEPT_COMMENTS";
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			List<CommentModel> comments = (List<CommentModel>) (bundle.getSerializable("comments"));
			if(comments.size() == 0){
				Log.w("GetAllService", "commentList is empty");
			}
			if(comments.get(0).equals(null)){
				Log.w("GetAllService", "element is null");
			}
			commentList.addAll(comments);
			textView.setText(Integer.valueOf(commentList.size()).toString());
		}
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(commentReciever);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(commentReciever);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		registerReceiver(commentReciever, filter);
		super.onResume();
	}
}
