package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//import ca.ualberta.cs.team5geotopics.TopLevelActivity.CommentListController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class GetTopLevel extends IntentService {
	final private static String COMMENT_KEY = "NEW_TOP_LEVEL";
	// this is the search DSL
	// http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-body.html
	final private static String QUERY_DSL = "{\n" + "   \"query\": {\n"
			+ "       \"match_all\": {}\n" + "	}\n" + "}";

	private static Gson GSON = null;
	
	public GetTopLevel() {
		super("GetTopLevelService");
	}

	// will call onHandleIntent(intent)
	public static void getAll(Context context) {
		if (GSON == null){
			constructGson();
		}
		Intent intent = new Intent(context, GetTopLevel.class);
		context.startService(intent);
	}

	// will broadcast an intent to the receiver registered to receive intents
	// with action "RECIEVE_TOP_LVL_COMMENTS".
	@Override
	protected void onHandleIntent(Intent intent) {
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		final JestClient client = application.getClient();
		ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		Search search = (Search) new Search.Builder(QUERY_DSL).addIndex(
				"TopLevel").build();
		try {
			JestResult result = client.execute(search);
			String jsonResponse = result.getJsonString();
			Log.w("GetAllService", jsonResponse);
			// code adapted from:
			// https://github.com/slmyers/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESClient.java
			// author: chenlei
			Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>(){}.getType();
			ElasticSearchSearchResponse<CommentModel> esResponse = GSON.fromJson(jsonResponse, elasticSearchSearchResponseType);
			commentList.addAll(esResponse.getSources());
			
			if (commentList.equals(null)) {
				Log.w("GetAllService", "commentList is null");
			}
			Log.w("GetAllService", Integer.valueOf(commentList.size())
					.toString());
		} catch (Exception e) {
			Log.w("GetAllService", e.toString());
		}

		client.shutdownClient();
		// broadcast intent containing bundle of commentmodels
		Intent resultIntent = new Intent();
		//resultIntent.setAction(CommentListController.RECIEVE_TOP_LEVEL_COMMENTS);
		resultIntent.addCategory(Intent.CATEGORY_DEFAULT);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(COMMENT_KEY, (ArrayList<? extends Parcelable>) commentList);
		resultIntent.putExtras(bundle);
		sendBroadcast(resultIntent);
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

