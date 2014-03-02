package ca.ualberta.cs.team5geotopics;

import com.google.gson.Gson;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PutIndexService extends IntentService {
	public PutIndexService() {
		super("PutIndexService");

	}

	public static void putComment(Context context, String index, String type,
			String id, CommentModel comment) {
		Bundle bundle = new Bundle();
		bundle.putString("index", index);
		bundle.putString("type", type);
		if(id != null){
			bundle.putString("id", id);
		}
		bundle.putString("comment", new Gson().toJson(comment));
		Intent intent = new Intent(context, PutIndexService.class);
		intent.putExtras(bundle);
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final JestClient client = GeoTopicsApplication
				.getClient(getApplicationContext());
		Bundle bundle = intent.getExtras();
		String jsonComment = bundle.getString("comment");
		String index = bundle.getString("index");
		String type = bundle.getString("type");
		String id = bundle.getString("id");
		
		Index pushIndex = null;
		if(!id.equals(null)){
			pushIndex = new Index.Builder(jsonComment).index(index)
					.type(type).id(id).build();
		}
		else{
			pushIndex = new Index.Builder(jsonComment).index(index)
					.type(type).build();
		}
		try {
			JestResult result = client.execute(pushIndex);
			Log.w("PutIndexService", "result: " + result.getJsonString());
		} catch (Exception e) {
			Log.w("PutIndexService", "exception: " + e.toString());
		}
		client.shutdownClient();
	}
}
