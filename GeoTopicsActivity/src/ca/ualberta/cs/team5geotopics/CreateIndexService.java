package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
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
		Toast.makeText(context, "launching createIndex service", 
				   Toast.LENGTH_LONG).show();
		Log.w("CreateIndexService", "in createIndex");
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("CreateIndexService", "in onHandleIntent");
		JestClient client = GeoTopicsApplication.getClient(getApplicationContext());
		String indexName = intent.getStringExtra("name");
		JestResult result = null;
		Exception exception = null;
		try {
			result = client.execute(new CreateIndex.Builder(indexName).build());
		} catch (Exception e) {
			exception = e;
			Log.w("CreateIndexService", e.toString());
		}
		
		if(exception == null){
			Log.w("CreateIndexService", result.getJsonString());
		}
		
		
	}

}
