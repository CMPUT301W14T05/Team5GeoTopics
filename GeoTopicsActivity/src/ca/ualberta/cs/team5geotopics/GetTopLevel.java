package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class GetTopLevel extends IntentService {
	final private static String 
	 QUERY_STRING = "{\n" +
		            "   \"query\": {\n" +
		            "       \"match_all\": {}\n" +
		            "	}\n" +
		            "}";

		
	
	public GetTopLevel() {
		super("GetTopLevelService");
	}

	public static void getAll(Context context){
		Intent intent = new Intent(context, GetTopLevel.class);
		context.startService(intent);
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		final JestClient client = application.getClient();
		List<CommentModel> commentList = null;
		Search search = (Search) new Search.Builder(QUERY_STRING)
        // multiple index or types can be added.
        .addIndex("TopLevel")
        .addType("Comment")
        .build();
		try{
			JestResult result = client.execute(search);
			Log.w("GetAllService", result.getJsonString());
			commentList = result.getSourceAsObjectList(CommentModel.class);
			if(commentList.equals(null)){
				Log.w("GetAllService", "commentList is null");
			}
			Log.w("GetAllService", Integer.valueOf(commentList.size()).toString());
		}
		catch(Exception e){
			Log.w("GetAllService", e.toString());
		}
		
		client.shutdownClient();
//		
//		Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction(CommentReciever.ACCEPT_COMMENTS);
//        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("comments", (ArrayList<CommentModel>)commentList);
//        broadcastIntent.putExtras(bundle);
//        sendBroadcast(broadcastIntent);
	}

}
