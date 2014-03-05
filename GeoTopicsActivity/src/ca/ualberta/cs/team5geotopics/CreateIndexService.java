package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


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
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		final JestClient client = application.getClient();
		String index = intent.getStringExtra("name");
		try {
			JestResult result = client.execute(new CreateIndex.Builder(index).build());
			Log.w("CreateIndexService", result.getJsonString());
		} catch (Exception e) {
			Log.e("CreateIndexService", e.toString());
		}
		client.shutdownClient();
	}
}
