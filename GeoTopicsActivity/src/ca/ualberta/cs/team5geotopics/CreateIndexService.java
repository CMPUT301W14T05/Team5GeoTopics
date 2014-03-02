package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class CreateIndexService extends IntentService {
	private Handler mMainThreadHandler = null;
	private static JestClient mClient = null;
	private String mIndexName = null;
	private JestResult mResult = null;
	private Exception mException = null;

	public CreateIndexService() {
		super("CreateIndexClass");
		mMainThreadHandler = new Handler();

	}

	public static void createIndex(Context context, String name) {
		Intent intent = new Intent(context, CreateIndexService.class);
		intent.putExtra("name", name);
		Toast.makeText(context, "launching createIndex service",
				Toast.LENGTH_LONG).show();
		Log.w("CreateIndexService", "in createIndex");
		mClient = GeoTopicsApplication.getClient(context);
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("CreateIndexService", "in onHandleIntent");

		mMainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					mResult = mClient.execute(new CreateIndex.Builder(
							mIndexName).build());
				} catch (Exception e) {
					mException = e;
					Log.w("CreateIndexService", e.toString());
				}

			}
		});
	}

}
