package ca.ualberta.cs.team5geotopics;



import io.searchbox.client.JestResult;
import android.graphics.Bitmap;
import com.google.gson.GsonBuilder;


/**
 * CommentSearch is used with respect to ElasticSearch and is called upon to help in matching
 * string queries to/from the server for sorting. This helps in maintaining the cache by sending 
 * recently pulled comments from ES to the cache.
 */

public class CommentSearch {
	private CommentPull commentPull = new CommentPull();
	private CommentListModel browseModel;
	protected Cache mCache;
	// a simple match all query
	private final static String MATCH_ALL_QUERY =	"{\n" +
													"\"from\" : 0, \"size\" : 100,\n" +
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
		commentPull.setClient(GeoTopicsApplication.getInstance().getClient());
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		commentPull.setGson(builder.create());
		commentPull.setLastResult(new JestResult(this.commentPull.getGson()));
		this.mCache = Cache.getInstance();
	}
	
	/**
	 * This CommentSearch creates a new instance of the Search given both a CommentListModel and a Cache to be searched.
	 * @param listModel The CommentListModel to be searched.
	 * @param cache The cache of the user.
	 */
	public CommentSearch(CommentListModel listModel, Cache cache){
		this.browseModel = listModel;
		commentPull.setClient(GeoTopicsApplication.getInstance().getClient());
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		commentPull.setGson(builder.create());
		commentPull.setLastResult(new JestResult(this.commentPull.getGson()));
		this.mCache = cache;
	}
	
	/**
	 * This method pulls all TopLevel comments
	 * @param topLevelActivity The top level comments activity.
	 * @return this.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel", "history.sav") The data to be pushed to history.sav.
	 */
	public Thread pullTopLevel(BrowseActivity topLevelActivity){
		return commentPull.pull(topLevelActivity, MATCH_ALL_QUERY, "TopLevel", "history.sav", browseModel, mCache);
	}
	
	/**
	 * This method pulls all reply comments. 
	 * @param replyLevelActivity The reply level comments activity.
	 * @param commentID The ID of the root comment.
	 * @return this.pull(replyLevelActivity, query, "ReplyLevel", commentID) The reference to the reply given the root.
	 */
	public Thread pullReplies(BrowseActivity replyLevelActivity, String commentID){
		String query = getReplyFilter(commentID);
		return commentPull.pull(replyLevelActivity, query, "ReplyLevel", commentID, browseModel, mCache);
	}
	
	/**
	 * Gets the filter type for the replies.
	 * @param parentID The ID of the parent comment.
	 * @return String A string of data given the reply filter.
	 */
	public String getReplyFilter(String parentID){
		return "{\n" + 					
				"\"filter\": {\n" + 
				"\"type\":{\n" + 
				"\"value\" : \"" + parentID +  "\"\n"  + 
				"}\n" +
				"}\n" +
				"}";
	}
	
	/**
	 * Pulls a single comment, that is specified by it's id (mEsId).
	 * @param id the mEsId of the CommentModel
	 * @param index the index in which the target model resides (TopLevel or ReplyLevel)
	 * @return returns a thread that is primarily used for testing. The commentModel field in 
	 * the CommentPull class will contain the model pulled from the server.
	 */
	public Thread pullComment(String id, String index){
		return commentPull.pullComment(id, index, this.getReplyFilter(id));
	}
	
	/**
	 * Returns the result of the search.
	 * @return lastResult The last result of the search.
	 */
	public JestResult returnResult(){
		return commentPull.getLastResult();
	}
	
}
