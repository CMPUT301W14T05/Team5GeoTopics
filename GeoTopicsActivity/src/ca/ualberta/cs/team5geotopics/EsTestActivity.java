package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team5geotopics.R;
import com.google.gson.Gson;

public class EsTestActivity extends Activity {
	private TextView textView;
	private Button putIndexBtn;
	private Button createIndexBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_es_test);
		textView = (TextView) findViewById(R.id.es_test_textview);
		textView.setText("blank");
		//registerReceivers();
		setupBtns();
	}

	private void setupBtns() {
		putIndexBtn = (Button) findViewById(R.id.es_test_put_index_btn);
		final Context context = this;
		putIndexBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				CommentModel comment = new CommentModel(null, "test body",
						"test author", null);
				Gson gson = new Gson();
				String json = gson.toJson(comment);
				Bundle bundle = new Bundle();
				bundle.putString("index", "Cats");
				bundle.putString("type", "cute");
				bundle.putString("id", "9001");
				bundle.putString("json", json);

				Intent intent = new Intent(context, ca.ualberta.cs.team5geotopics.PutIndexService.class);
				intent.putExtras(bundle);
				Log.w("EsTestActivity", "starting service" + '\n' 
						+ "context: " + context.toString() + '\n'
						+ "ca.ualberta.cs.team5geotopics.PutIndexService.class");
				startService(intent);

			}
		});
		
		
		createIndexBtn = (Button)findViewById(R.id.es_test_create_index_btn);
		createIndexBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = "test";
				CreateIndexService.createIndex(getApplicationContext(), name);
			}
		});
	}
}
