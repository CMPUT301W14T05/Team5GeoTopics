package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CommentListController {
	private CommentListModel browseModel;
	private JestClient client;
	private Gson gson;
	
	public CommentListController(CommentListModel listModel){
		this.browseModel = listModel;
		this.client = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.gson = builder.create();
	}
	
	public void getTopLevel(final BrowseActivity topLevelActivity){
		final String QUERY_DSL = "{\n" + "   \"query\": {\n"
				+ "       \"match_all\": {}\n" + "	}\n" + "}";
		
		Thread thread = new Thread(){
			public void run(){
				JestResult result = null;
				ArrayList<CommentModel> inComments = new ArrayList<CommentModel>();
				Search search = (Search) new Search.Builder(QUERY_DSL).addIndex(
						"TopLevel").build();
				try {
					result = client.execute(search);
					Log.w("CommentListController", "result json string = " + result.getJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				client.shutdownClient();
				
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>(){}.getType();
				final ElasticSearchSearchResponse<CommentModel> esResponse = gson.fromJson(result.getJsonString(), elasticSearchSearchResponseType);
				// zjullion https://github.com/slmyers/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java 
				
				Log.w("CommentListController", "we have this many responses: " + 
						Integer.valueOf(esResponse.getSources().size()).toString());
				
				Runnable updateModel = new Runnable(){
					@Override
					public void run() {
						browseModel.refreshAddAll( (ArrayList<CommentModel>) esResponse.getSources());
					}
				};
				topLevelActivity.runOnUiThread(updateModel);
			}
		};
		thread.start();
	}
	
	public void getReplies(final BrowseActivity replyLevelActivity, CommentModel comment){
		final String QUERY_DSL =	"{\n" + 
									"   \"query\": {\n" +
									"		\"filtered\": {\n" +
									"			\"query\": {\n" +
									"				\"match all\": {}\n" +
									"			\"filter\": {\n" +
									"				\"type\": {\n" +
									"					\"value\" : {\"" + comment.getmEsID() +  "\"}\n"  +
									"			}\n" +
									"		}\n" +
									"	}\n" +
									"}";
									
	}
}
