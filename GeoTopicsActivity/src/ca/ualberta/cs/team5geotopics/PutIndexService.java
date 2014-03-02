package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PutIndexService extends IntentService {
	protected Index pushIndex = null;
	protected JestResult result = null;
	protected JestClient client = null;
	protected Exception exception = null;

	public PutIndexService() {
		super("PutIndexService");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.w("PutIndexService", "in onStart() PutIndexService.");
		super.onStart(intent, startId);
	};

	@Override
	public void onCreate() {
		Log.w("PutIndexService", "in onCreate() PutIndexService.");
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		client = GeoTopicsApplication.getClient(getApplicationContext());
		Log.w("PutIndexService", "inHandleIntent");

		Bundle bundle = intent.getExtras();
		String jsonComment = bundle.getString("json");
		if (jsonComment == null) {
			Log.w("json", "jsonComment is null in onHandleIntent");
		}

		
		String index = bundle.getString("index");
		String type = bundle.getString("type");
		String id = bundle.getString("id");
		
		
		Log.w("PutIndexService", "attempting to push to index: " + index);
		this.pushIndex = new Index.Builder(jsonComment).index(index).type(type)
				.id(id).build();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					result = client.execute(pushIndex);
					Log.w("PutIndexService", "result: " + result.getJsonString());
				} catch (Exception e) {
					Log.w("PutIndexService", "exception: " + e.toString());
					exception = e;
				}

			}
		}).start();
		
		

	}
}
