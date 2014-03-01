package ca.ualberta.cs.team5geotopics;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PutIndexService extends IntentService{
	
	
	public PutIndexService() {
		super("PutIndexService");
		
	}

	public static void putComment(Context context, Bundle bundle){
		Intent intent = new Intent(context, PutIndexService.class);
		intent.putExtras(bundle);
		context.startService(intent);
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		JestClient client = GeoTopicsApplication.getClient();
		
		Bundle bundle = intent.getExtras();
		String[] args = bundle.getStringArray("argList");
		String jsonComment = bundle.getString("jsonComment");
		//for clarity
		String index = args[0];
		String type = args[1];
		String id = args[2];
		
		Index indexToPush = new Index.Builder(jsonComment).index(index).type(type).id(id).build();
		
		try {
			client.execute(indexToPush);
		} catch (Exception e) {
			Log.w("putComment", e.toString());
		}
	}
}
