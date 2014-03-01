package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class CreateIndexService extends IntentService {

	public CreateIndexService() {
		super("CreateIndexClass");

	}

	public static void createIndex(Context context, String name) {
		Intent intent = new Intent(context, CreateIndexService.class);
		intent.putExtra("name", name);
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		JestClient client = GeoTopicsApplication.getClient();
		String indexName = intent.getStringExtra("name");
		Index indexToPush = new Index.Builder(indexName).build();
		JestResult result = null;
		Exception exception = null;
		try {
			result = client.execute(indexToPush);
		} catch (Exception e) {
			exception = e;
			Log.w("createIndex", e.toString());
			Toast.makeText(getApplicationContext(), (String)e.toString(), 
					   Toast.LENGTH_LONG).show();
		}
		
		if(exception == null){
			Toast.makeText(getApplicationContext(), (String)result.getJsonString(), 
					   Toast.LENGTH_LONG).show();
		}
		
	}

}
