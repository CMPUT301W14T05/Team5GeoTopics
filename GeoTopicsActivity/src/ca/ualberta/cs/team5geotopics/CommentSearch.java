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
	private JestResult lastResult;
	protected Cache mCache = Cache.getInstance();
	
	// a simple match all query
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
		this.lastResult = new JestResult(this.gson);
	}
	
	// this method pulls all TopLevel comments
	public Thread pullTopLevel(BrowseActivity topLevelActivity){
		return this.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel", "history.sav");
	}
	
	public Thread pullReplies(BrowseActivity replyLevelActivity, String commentID){
		String query = getReplyQuery(commentID);
		return this.pull(replyLevelActivity, query, "ReplyLevel", commentID);
	}
	
	
	private String getReplyQuery(String parentID){
		return "{\n" + 					
				"\"filter\": {\n" + 
				"\"type\":{\n" + 
				"\"value\" : \"" + parentID +  "\"\n"  + 
				"}\n" +
				"}\n" +
				"}";
	}
	
	private Thread pull(final BrowseActivity topLevelActivity, final String queryDSL, final String index, final String commentID){
		Thread thread = new Thread(){
			public void run(){
				final Search search = (Search) new Search.Builder(queryDSL).addIndex(
						index).build();
				try{
					lastResult = client.execute(search);
					Log.w("CommentSearch", "result json string = " + lastResult.getJsonString());
				}
				catch (Exception e){
					e.printStackTrace();
				}
					
				
				client.shutdownClient();
				
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>(){}.getType();
				String lastResultJsonString = lastResult.getJsonString();
				
				//send comments pulled from Elasticsearch straight to disk for caching. (this can be placed on another thread if it slows things)
				
				
				final ElasticSearchSearchResponse<CommentModel> esResponse = gson.fromJson(lastResultJsonString, elasticSearchSearchResponseType);
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
						try{
							ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
							acm.addAll((ArrayList<CommentModel>) esResponse.getSources());
							browseModel.addNew(acm);
							
							String jsonString = gson.toJson(acm);
							mCache.replaceFileHistory(jsonString, commentID);
						}
						catch (NullPointerException e){
							// do nothing if the new comments are null
							Log.w("CommentSearch", "new comments are null");
						}
					}
				};
				topLevelActivity.runOnUiThread(updateModel);
			}
		};
		thread.start();
		return thread;
	}
	
	public JestResult returnResult(){
		return lastResult;
	}
	
}
