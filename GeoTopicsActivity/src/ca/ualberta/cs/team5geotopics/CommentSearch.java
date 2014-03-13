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

public class CommentSearch {
	private CommentListModel browseModel;
	private JestClient client;
	private Gson gson;
	
	private final static String MATCH_ALL_QUERY =	"{\n" +
										  			"\"query\": {\n" +
										  			"\"match_all\": {}\n" +
										  			"}\n" +
										  			"}";
	
	
	
	public CommentSearch(CommentListModel listModel){
		this.browseModel = listModel;
		this.client = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.gson = builder.create();
	}
	
	public void pullTopLevel(BrowseActivity topLevelActivity){
		this.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel");
	}
	
	public void pullReplies(BrowseActivity replyLevelActivity, CommentModel comment){
		String query = getReplyQuery(comment.getmEsID());
		this.pull(replyLevelActivity, query, "ReplyLevel");
	}
	
	
	private String getReplyQuery(String parentID){
		return "{\n" + 					//closed
				"\"filter\": {\n" + //closed
				"\"type\":{\n" + //closed
				"\"value\" : \"" + parentID +  "\"\n"  + //closed
				"}\n" +
				"}\n" +
				"}";
	}
	
	private void pull(final BrowseActivity topLevelActivity, final String queryDSL, final String index){
		Thread thread = new Thread(){
			public void run(){
				JestResult result = null;
				ArrayList<CommentModel> inComments = new ArrayList<CommentModel>();
				Search search = (Search) new Search.Builder(queryDSL).addIndex(
						index).build();
				try {
					result = client.execute(search);
					Log.w("CommentSearch", "result json string = " + result.getJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				client.shutdownClient();
				
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>(){}.getType();
				final ElasticSearchSearchResponse<CommentModel> esResponse = gson.fromJson(result.getJsonString(), elasticSearchSearchResponseType);
				// zjullion https://github.com/slmyers/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java 
				
				try{
					Log.w("CommentSearch", "we have this many responses: " + 
						Integer.valueOf(esResponse.getSources().size()).toString());
				}catch(NullPointerException e){
					e.printStackTrace();
					Log.w("CommentSearch", "the hits are null");
				}
				
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
	
	
}
