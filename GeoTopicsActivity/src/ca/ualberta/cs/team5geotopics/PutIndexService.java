package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * This service encapsulates an ElasticSearch index command.
 * The ID of the ES index will the the string returned by user.readInstallIDFile() + user.readPostCount()
 * The Index of the ES index is dependent on the parameter index passed to:
 * public static void putComment(Context context, String index, CommentModel comment).
 * The type of the ES index will be the string returned by user.readInstallIDFile()
 * The bitmap is serialized into the proper format as discussed 
 * by: Zachary Jullion here: https://eclass.srv.ualberta.ca/mod/forum/discuss.php?d=319340
 * onHandleIntent will broadcast an intent to its receiver
 * detailing the result of the operation. 
 * @author Steven Myers
 *
 */
public class PutIndexService extends IntentService {
	private static final String LOG_TAG = "PutIndexService";
	private static Gson GSON = null;
	private static User USER = null;
	
	public PutIndexService() {
		super("PutIndexService");

	}

	/**
	 * Pushes a CommentModel to an ES webserver.
	 * @param context the application context
	 * @param index the ES index of the CommentModel.
	 * @param type  the ES type.
	 * @param comment the CommentModel to push
	 */
	public static void pushComment(Context context, String index, CommentModel comment) {
		if (GSON == null){
			constructGson();
		}
		if (USER == null){
			USER = new User(context);
		}
			
		Bundle bundle = new Bundle();
		bundle.putString("index", index);
		
		bundle.putString("id", comment.getmEsID());
		bundle.putString("type", comment.getmEsType());
		
		// serializing the CommentModel
		bundle.putString("comment", GSON.toJson(comment));
		
		// launch intent here
		Intent intent = new Intent(context, PutIndexService.class);
		intent.putExtras(bundle);
		context.startService(intent);
	}
	/**
	 * 	uses a JestClient to build an index and execute the build index.
	 * 	will broadcast the result of the operation to the CommentPusher 
	 *  BroadcastReciever.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		String exception = null;
		String eSid = null;
		
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		final JestClient client = application.getClient();
		
		// unbundle the information
		Bundle bundle = intent.getExtras();
		String jsonComment = bundle.getString("comment");
		Log.w(LOG_TAG, jsonComment);
		String index = bundle.getString("index");
		String type = bundle.getString("type");
		String id = bundle.getString("id");
		
		// build the index
		Index pushIndex = new Index.Builder(jsonComment).index(index)
					.type(type).id(id).build();
		
		// construct the index
		JestResult result = null;
		try {
			result = client.execute(pushIndex);
			Log.w(LOG_TAG, "result: " + result.getJsonString());
		} catch (Exception e) {
			Log.w(LOG_TAG, "exception: " + e.toString());
			exception = e.toString();
		}
		
		// if the post was successful the increment the value
		// stored in the post count file. 
		if(exception == null){
			User user = new User(getApplicationContext());
			user.updatePostCountFile();
		}
		
		client.shutdownClient();
	}
	
	
	/**
	 * taken from https://github.com/zjullion/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java
	 * written by Zachary Jullion 
	 * Constructs a Gson with a custom serializer / desserializer registered for Bitmaps.
	 */
	private static void constructGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		GSON = builder.create();
	}
}
