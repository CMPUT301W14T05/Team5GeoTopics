package ca.ualberta.cs.team5geotopics;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class EsTestActivity extends Activity {
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_es_test);
		textView = (TextView) findViewById(R.id.ea_test_textview);
	}
	
	//http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
	public class IndexReciever extends BroadcastReceiver{
		public final static String ACTION_TEXT_CAPITALIZED= "ca.ualberta.cs.team5geotopcs.PutIndexService";
		final String OUTPUT_TEXT = "OUTPUT_TEXT";
		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(OUTPUT_TEXT);
			textView.setText(result);
		}
		
	}
}
