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

/**
 * CommentSearch is used with respect to ElasticSearch and is called upon to help in matching
 * string queries to/from the server for sorting. This helps in maintaining the cache by sending 
 * recently pulled comments from ES to the cache.
 */

public class CommentSearch {
	private CommentListModel browseModel;
	private JestClient client;
	private Gson gson;
	private JestResult lastResult;
	protected Cache mCache;
	
	// a simple match all query
	private final static String MATCH_ALL_QUERY =	"{\n" +
										  			"\"query\": {\n" +
										  			"\"match_all\": {}\n" +
										  			"}\n" +
										  			"}";
	
	
	/**
	 * CommentSearch creates a new instance of a search given a CommentListModel.
	 * @param listModel The CommentListModel to be searched.
	 */
	public CommentSearch(CommentListModel listModel){
		this.browseModel = listModel;
		this.client = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.gson = builder.create();
		this.lastResult = new JestResult(this.gson);
		this.mCache = Cache.getInstance();
	}
	
	/**
	 * This CommentSearch creates a new instance of the Search given both a CommentListModel and a Cache to be searched.
	 * @param listModel The CommentListModel to be searched.
	 * @param cache The cache of the user.
	 */
	public CommentSearch(CommentListModel listModel, Cache cache){
		this.browseModel = listModel;
		this.client = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.gson = builder.create();
		this.lastResult = new JestResult(this.gson);
		this.mCache = cache;
	}
	
	/**
	 * This method pulls all TopLevel comments
	 * @param topLevelActivity The top level comments activity.
	 * @return this.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel", "history.sav") The data to be pushed to history.sav.
	 */
	public Thread pullTopLevel(BrowseActivity topLevelActivity){
		return this.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel", "history.sav");
	}
	
	/**
	 * This method pulls all reply comments. 
	 * @param replyLevelActivity The reply level comments activity.
	 * @param commentID The ID of the root comment.
	 * @return this.pull(replyLevelActivity, query, "ReplyLevel", commentID) The reference to the reply given the root.
	 */
	public Thread pullReplies(BrowseActivity replyLevelActivity, String commentID){
		String query = getReplyFilter(commentID);
		return this.pull(replyLevelActivity, query, "ReplyLevel", commentID);
	}
	
	/**
	 * Gets the filter type for the replies.
	 * @param parentID The ID of the parent comment.
	 * @return String A string of data given the reply filter.
	 */
	private String getReplyFilter(String parentID){
		return "{\n" + 					
				"\"filter\": {\n" + 
				"\"type\":{\n" + 
				"\"value\" : \"" + parentID +  "\"\n"  + 
				"}\n" +
				"}\n" +
				"}";
	}
	
	/**
	 * Only time sort at the moment
	 * @param type The type of sort.
	 * @return String The string showing the sorted data.
	 */
	private String getSortQuery(String type){
		if(type.equals("time")){
		return 	"\"sort\" : [\n" +
				"{\"epochTime\" : {\"order\" : \"asc\", \"mode\" : \"avg\"}}\n" +
				"],\n";
		}
		
		return "ERROR";
	}
	
	/**
	 * Pulls the data from ElasticSearchResponse and sets it up for sorting on a new thread.
	 * @param topLevelActivity An instance of the activity showing top-level comments.
	 * @param queryDSL The query to be searched.
	 * @param index The index of the comment.
	 * @param commentID The ID of the comment.
	 * @return thread The thread which the search will be ran on.
	 */
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
							mCache.replaceFileHistory(jsonString, commentID); //send the retrieved comments to be saved on the disk
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
	
	/**
	 * Returns the result of the search.
	 * @return lastResult The last result of the search.
	 */
	public JestResult returnResult(){
		return lastResult;
	}
	
}
