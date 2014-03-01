package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.team5geotopics.R;
import com.google.gson.Gson;

public class EsTestActivity extends Activity {
	private TextView textView;
	private PutIndexReciever putReciever;
	private CreateIndexReciever createIndexReciever;
	private Button putIndexBtn;
	private Button createIndexBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_es_test);
		textView = (TextView) findViewById(R.id.es_test_textview);
		textView.setText("blank");
		registerReceivers();
		setupBtns();
	}

	private void setupBtns() {
		putIndexBtn = (Button)findViewById(R.id.es_test_put_index_btn);
		putIndexBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent putIndexServiceIntent = new Intent(EsTestActivity.this, PutIndexService.class);
				String[] args = {"TestLevel","TestType","1"};
				CommentModel comment = new CommentModel(null, "test body", "test author", null);
				Gson gson = new Gson();
				String json = gson.toJson(comment);
				Bundle bundle = new Bundle();
				bundle.putStringArray("args", args);
				bundle.putString("json", json);
				startService(putIndexServiceIntent);
				
			}
		});
		
		
		createIndexBtn = (Button)findViewById(R.id.es_test_create_index_btn);
		createIndexBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createIndexIntent = new Intent(EsTestActivity.this, CreateIndexService.class);
				String name = "test";
				createIndexIntent.putExtra("name", name);
				startService(createIndexIntent);
				
			}
		});
	}

	@Override
	protected void onPause() {

		/* we should unregister BroadcastReceiver here */
		unregisterReceiver(putReciever);
		unregisterReceiver(createIndexReciever);
		super.onPause();
	}

	@Override
	protected void onResume() {

		/* we register BroadcastReceiver here */
		registerReceivers();
		super.onResume();
	}
	
	// http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
	private void registerReceivers() {
		/* create filter for exact intent what we want from other intent */
		IntentFilter putIndxIntentFilter = new IntentFilter(
				PutIndexReciever.ACTION_TEXT_CAPITALIZED);
		putIndxIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		/* create new broadcast receiver */
		putReciever = new PutIndexReciever();
		/* registering our Broadcast receiver to listen action */
		registerReceiver(putReciever, putIndxIntentFilter);

		IntentFilter createIndexRecieverIntentFilter = new IntentFilter(
				CreateIndexReciever.ACTION_TEXT_CAPITALIZED);
		createIndexRecieverIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		createIndexReciever = new CreateIndexReciever();
		registerReceiver(createIndexReciever, createIndexRecieverIntentFilter);

	}

	// http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
	public class PutIndexReciever extends BroadcastReceiver {
		public PutIndexReciever() {
			super();
		}

		public final static String ACTION_TEXT_CAPITALIZED = "ca.ualberta.cs.team5geotopcs.PutIndexService";
		final String OUTPUT_TEXT = "OUTPUT_TEXT";

		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(OUTPUT_TEXT);
			textView.setText(result);
		}

	}

	// http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
	public class CreateIndexReciever extends BroadcastReceiver {
		public final static String ACTION_TEXT_CAPITALIZED = "ca.ualberta.cs.team5geotopcs.CreateIndexService";
		final String OUTPUT_TEXT = "OUTPUT_TEXT";

		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(OUTPUT_TEXT);
			textView.setText(result);
		}

	}

}
