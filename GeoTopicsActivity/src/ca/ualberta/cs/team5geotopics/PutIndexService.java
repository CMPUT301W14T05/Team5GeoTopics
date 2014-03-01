package ca.ualberta.cs.team5geotopics;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class PutIndexService extends IntentService{
	
	
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
		JestClient client = GeoTopicsApplication.getClient(getApplicationContext());
		Log.w("PutIndexService", "inHandleIntent");
		
		Bundle bundle = intent.getExtras();
		String[] args = bundle.getStringArray("argList");
		String jsonComment = bundle.getString("json");
		if(jsonComment == null){
			Log.w("json", "jsonComment is null in onHandleIntent");
		}
		
		//for clarity
		String index = bundle.getString("index");
		Log.w("PutIndexService" , "attempting to push to index: " + index);
		String type = bundle.getString("type");
		String id = bundle.getString("id");
		
		Index indexToPush = new Index.Builder(jsonComment).index(index).type(type).id(id).build();
		JestResult result = null;
		Exception exception = null;
		try {
			result = client.execute(indexToPush);
		} catch (Exception e) {
			Log.w("PutIndexService", "exception: " + e.toString());
			exception = e;
		}
		
		if(exception == null){
			Log.w("PutIndexService", "result: " + result.getJsonString());
		}
	}
}
